package org.futurepages.util.template.simpletemplate.template;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.futurepages.util.template.simpletemplate.expressions.exceptions.ExpressionException;
import org.futurepages.util.template.simpletemplate.expressions.parser.Parser;
import org.futurepages.util.template.simpletemplate.template.builtin.tags.TemplateTag;
import org.futurepages.util.template.simpletemplate.template.builtin.tags.TemplateTagInitializer;
import org.futurepages.util.template.simpletemplate.template.exceptions.TemplateException;
import org.futurepages.util.template.simpletemplate.template.exceptions.TemplateNestingException;
import org.futurepages.util.template.simpletemplate.util.MyStack;
import org.futurepages.util.template.simpletemplate.util.Tuple;
import org.futurepages.util.template.simpletemplate.util.Wrap;

/**
 *
 * @author thiago
 */
public class TemplateParser {

	// TODO: Muito cuidado ao mudar os parênteses dessas RegEx, pois elas definem os grupos.
	// No código é usado índices para estes inteiros e eles aumentam na ordem em que os
	// parênteses vão aparecendo.
	// Numeração dos grupos:                                                1         2
	private static final Pattern openPattern   = Pattern.compile("<\\!\\-\\-(\\w+)\\s*(.*?)\\s*\\-\\->");
	private static final int TAG_NAME_GROUP = 1;
	private static final int TAG_ATTRS_GROUP = 2;

	private static final String end = "end";
	private static final String _else = "else";

	//private static final Pattern expPattern = Pattern.compile("\\$\\{([^\\}]*)}");
	private static final Pattern expPattern = Pattern.compile("\\$\\{([^${]*)\\}");

	// precisa dizer o que faz?
	public static TemplateBlockBase compile(String input)  throws TemplateException {
		TemplateTagInitializer.instance();

		try {
			allBlocksMatched(input);
		} catch (TemplateNestingException ex) {
			throw new TemplateException(ex.getMessage(), ex.getLine());
		}

		return mountTemplateTree(input);
	}

	// Monta a árvore que representa o template
	private static TemplateBlockBase mountTemplateTree(String input) throws TemplateException {

		// Uma classe Wrap para um inteiro, foi utilizado para poder passar
		// o parâmetro desse inteiro por referência, já que java não permite
		// a passagem de tipos primitivos por referência.
		Wrap<Integer> lastLength = new Wrap(0);

		int initialBufferSize = (int)((float)input.length() * 1.3f);

		TemplateBlockBase base = new TemplateBlockBase(initialBufferSize);
		Matcher tagMatcher = openPattern.matcher(input);
		MyStack<AbstractTemplateBlock> stack = new MyStack<AbstractTemplateBlock>();
		MyStack<Boolean> usingElse = new MyStack<Boolean>();

		stack.push(base);
		usingElse.push(false);

		mountTemplateTreeAux(input, lastLength, tagMatcher, stack, usingElse);

		return base;
	}

	// @TODO: Ignorar comentários html. Os cometários serão caracterizados como tendo um espaço depois de abrir a tag
	// <!-- espaço antes daprimeira palavra -->
	// Faz o trabalho sujo de mountTemplateTree.
	private static void mountTemplateTreeAux(String input, Wrap<Integer> lastLength, Matcher tagMatcher, MyStack<AbstractTemplateBlock> stack, MyStack<Boolean> usingElse) throws TemplateException {

		try {
			// Procura o próximo bloco.
			while (tagMatcher.find()) {
				// Obtem, mas não remove, o bloco que está no topo da pilha.
				AbstractTemplateBlock block = stack.peek();

				// @TODO: Pode ser que não haja este trexo "estático". Verifica isto!
				// Copiar tudo que esteja entre "lastLength" e o bloco encontrado, para
				// um bloco estático
				String text = input.substring(lastLength.getValue(), tagMatcher.start());
				if (!text.isEmpty()) {
					TemplateStatic sttc = new TemplateStatic();
					sttc.setContent(text);

					// Adiciona o bloco estático ao bloco que está altualmente no topo da pilha
					if (!usingElse.peek()) {
						block.append(sttc);
					} else {
						block.appendToElse(sttc);
					}
				}

				// Atualiza do valor de lastLength
				lastLength.setValue(tagMatcher.end());

				// Objtêm o nome[1] e os atributos[2] da tag.
				String [] expressions = new String[] {tagMatcher.group(TAG_NAME_GROUP), tagMatcher.group(TAG_ATTRS_GROUP)};

				boolean startsWithEnd = expressions[0].startsWith(end);
				boolean isElse = expressions[0].equals(_else);

				// Se não é o fechamento de um bloco,
				// crie um novo bloco, correspondente ao nome da tag (expressions[0])
				// e empilhe este novo bloco na pilha.
				if (!startsWithEnd && !isElse) {
					TemplateBlock tb = new TemplateBlock();

					TemplateTag tag = TemplateTag.getByTagName(expressions[0]);
					tb.setTag(tag.getNewInstance());

					// @TODO: Fazer a validação das exceções aqui lançadas
					tb.setParams(tag.evalExpression(expressions[1]));

					if (!usingElse.peek()) {
						block.append(tb);
					} else {
						block.appendToElse(tb);
					}

					stack.push(tb);
					usingElse.push(false);
				} else if (isElse) {
					usingElse.replaceTop(true);
				} else { // Caso contrário, remova um bloco que esteja em cima da pilha.
					stack.pop();
					usingElse.pop();
				}
			}

			String text = input.substring(lastLength.getValue());

			if (!text.isEmpty()) {
				// Adicione o resto do template através de um bloco estátio ao bloco que
				// restou na pilha.
				TemplateStatic sttc = new TemplateStatic();
				sttc.setContent(text);

				AbstractTemplateBlock block = stack.pop();
				block.append(sttc);
			}
		} catch (ExpressionException ex) {

			throw new TemplateException(ex.getMessage(), lineCounter(input.substring(0, tagMatcher.start())), ex.show());

		} catch (TemplateException ex) {
			int lines_before = lastLength.getValue() > 0
					? lineCounter(input.substring(0, lastLength.getValue()))
					: 0;
			int lines = lastLength.getValue() > 0
					? (lines_before + (ex.getLine() > 1 ? ex.getLine() -1 : 0))
					: (ex.getLine());

			throw new TemplateException(ex.getMsg(), lines, ex.getError());
		}
	}

	private static Tuple<String, Integer> t(String s, int i) {
		return new Tuple<String, Integer>(s, i);
	}

	// @TODO: Ignorar comentários html. Os cometários serão caracterizados como tendo um espaço depois de abrir a tag
	// <!-- espaço antes daprimeira palavra -->

	// Método que verifica a integridade dos blocos,
	// vendo se estão todos devidadamente aninhados e
	// fechados.
	public static void allBlocksMatched(String input) throws TemplateNestingException  {
		MyStack<Tuple<String, Integer>> stack = new MyStack<Tuple<String, Integer>>();
		Matcher tagMatcher = openPattern.matcher(input);

		while (tagMatcher.find()) {
			// Objtêm o nome[1] e os atributos[2] da tag.
			String [] expressions = new String[] {tagMatcher.group(TAG_NAME_GROUP), tagMatcher.group(TAG_ATTRS_GROUP)};

			if (!expressions[0].startsWith(end) && !expressions[0].equals(_else)) {
				stack.push(t(expressions[0], lineCounter(input.substring(0, tagMatcher.start()))));
			} else if (expressions[0].equals(_else)) {
				if (stack.isEmpty()) {
					// Estou colocando uma tag else sem que tenha nenhuma tag aberta.
					throw new TemplateNestingException("Template nesting error: " + tagMatcher.group(), lineCounter(input.substring(0, tagMatcher.start())));
				} else if (stack.peek().getA().equals(_else)) {
					// Estou colocando uma tag else logo após outra tag else.
					throw new TemplateNestingException("Template nesting error: " + tagMatcher.group(), lineCounter(input.substring(0, tagMatcher.start())));
				}
				stack.push(t(expressions[0], lineCounter(input.substring(0, tagMatcher.start()))));
			} else if (!stack.isEmpty() && checkStack(stack, expressions[0])) {
				stack.pop();
			} else if (stack.isEmpty()) {
				// Estou fechando alguma tag mas não tem nenhuma tag aberta.
				throw new TemplateNestingException("Template nesting error: " + tagMatcher.group(), lineCounter(input.substring(0, tagMatcher.start())));
			} else { // stack.peek().getA().equals(expressions[0].substring(end.length()))
				// A tag que estou fechando não bate com a tag que está aberta.
				throw new TemplateNestingException("Template nesting error: " + tagMatcher.group(), stack.pop().getB());
			}
		}

		if (!stack.isEmpty()) {
			// Ficou faltando fechar alguma tag.
			throw new TemplateNestingException("Template nesting error: " + stack.peek().getA(), stack.pop().getB());
		}
	}

	protected static boolean checkStack(MyStack<Tuple<String, Integer>> stack, String currentBlock) {
		if (currentBlock.startsWith(end)) {
			if (stack.peek().getA().equals(currentBlock.substring(end.length()))) {
				return true;
			} else {
				Tuple<String, Integer> elseBlock = stack.pop();
				return elseBlock.getA().equals(_else) && stack.peek().getA().equals(currentBlock.substring(end.length()));
			}
		}

		return false;
	}

	protected static int lineCounter(String str) {
		int c = str.isEmpty() ? 0 : 1;

		if (c > 0) {
			for (int i = 0, len = str.length(); i < len; i++) {
				if (str.charAt(i) == '\n') {
					c += 1;
				}
			}
		}

		return c;
	}

	public static Object []getContent(String content) throws TemplateException {
		ArrayList<Object> cont = new ArrayList<Object>();

		Matcher m = expPattern.matcher(content);
		int lastIndex = 0;

		try {
			while (m.find()) {
				cont.add(content.substring(lastIndex, m.start()));

				Tuple<String, Integer> t = expressionContent(m.group());
				cont.add(new Parser(t.getA().substring(2, t.getA().length()-1)).parse());

				lastIndex = m.end() - t.getB();
			}
		} catch (ExpressionException ex) {
			throw new TemplateException(ex.getMessage(), lineCounter(content.substring(0, m.start())), ex.show());
		}

		cont.add(content.substring(lastIndex));

		Object array [] = new Object[cont.size()];

		cont.toArray(array);

		return array;
	}

	public static Tuple<String, Integer> expressionContent(String str) {
		int j = str.length() - 1;

		for (int i = str.length() - 1; i >= 0; i--) {
			char ch = str.charAt(i);

			if (ch == '}') {
				j = i;
			}
		}

		return new Tuple<String, Integer>(str.substring(0, j + 1), str.length() - 1 - j);
	}
}
