package org.futurepages.util.templatizer.expressions.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.futurepages.util.templatizer.expressions.exceptions.BadExpression;
import org.futurepages.util.templatizer.expressions.exceptions.ExpectedExpression;
import org.futurepages.util.templatizer.expressions.exceptions.FunctionDoesNotExists;
import org.futurepages.util.templatizer.expressions.exceptions.Unexpected;
import org.futurepages.util.templatizer.expressions.function.Function;
import org.futurepages.util.templatizer.expressions.function.FunctionsRegister;
import org.futurepages.util.templatizer.expressions.operators.core.Operator;
import org.futurepages.util.templatizer.expressions.operators.logical.And;
import org.futurepages.util.templatizer.expressions.operators.logical.Equals;
import org.futurepages.util.templatizer.expressions.operators.logical.GreaterEqualsThan;
import org.futurepages.util.templatizer.expressions.operators.logical.GreaterThan;
import org.futurepages.util.templatizer.expressions.operators.logical.LessEqualsThan;
import org.futurepages.util.templatizer.expressions.operators.logical.LessThan;
import org.futurepages.util.templatizer.expressions.operators.logical.Not;
import org.futurepages.util.templatizer.expressions.operators.logical.NotEquals;
import org.futurepages.util.templatizer.expressions.operators.logical.Or;
import org.futurepages.util.templatizer.expressions.operators.logical.Xor;
import org.futurepages.util.templatizer.expressions.operators.numerical.Add;
import org.futurepages.util.templatizer.expressions.operators.numerical.Diff;
import org.futurepages.util.templatizer.expressions.operators.numerical.Div;
import org.futurepages.util.templatizer.expressions.operators.numerical.Mod;
import org.futurepages.util.templatizer.expressions.operators.numerical.Mult;
import org.futurepages.util.templatizer.expressions.operators.numerical.Negative;
import org.futurepages.util.templatizer.expressions.operators.numerical.Positive;
import org.futurepages.util.templatizer.expressions.operators.numerical.Power;
import org.futurepages.util.templatizer.expressions.operators.unevaluable.Comma;
import org.futurepages.util.templatizer.expressions.operators.unevaluable.LParenthesis;
import org.futurepages.util.templatizer.expressions.operators.unevaluable.RParenthesis;
import org.futurepages.util.templatizer.expressions.primitivehandle.Const;
import org.futurepages.util.templatizer.expressions.primitivehandle.Null;
import org.futurepages.util.templatizer.expressions.tree.Exp;
import org.futurepages.util.templatizer.expressions.tree.Identifier;
import org.futurepages.util.templatizer.expressions.tree.Literal;
import org.futurepages.util.templatizer.expressions.tree.Token;
import org.futurepages.util.templatizer.util.Tuple;


/**
 *
 * @author thiago
 */
public class TokensToExp {
	
	private static final HashMap<String, Class<? extends Operator>> ops;
	
	static {
		ops = new HashMap<String, Class<? extends Operator>>();
		
		/*
		 * - e + não são adicionados ao mapa. Pois são tratados como casos especiais.
		 */
		ops.put("!", Not.class);
		ops.put("*", Mult.class);
		ops.put("**", Power.class);
		ops.put("/", Div.class);
		ops.put("%", Mod.class);

		ops.put("<", LessThan.class);
		ops.put("<=", LessEqualsThan.class);
		ops.put(">", GreaterThan.class);
		ops.put(">=", GreaterEqualsThan.class);
		ops.put("==", Equals.class);
		ops.put("!=", NotEquals.class);
		ops.put("^", Xor.class);
		ops.put("&&", And.class);
		ops.put("||", Or.class);

		// UNEVALUABLE
		ops.put("(", LParenthesis.class);
		ops.put(")", RParenthesis.class);
		ops.put(",", Comma.class);
	}
	
	private static interface PositiveNegative {
		public Exp signal();
		public Exp operator();
	}
	
	private static final PositiveNegative [] PN = new PositiveNegative[] {
		new PositiveNegative() {
			@Override
			public Exp signal() {
				return new Positive();
			}
			@Override
			public Exp operator() {
				return new Add();
			}
		},
		new PositiveNegative() {
			@Override
			public Exp signal() {
				return new Negative();
			}
			@Override
			public Exp operator() {
				return new Diff();
			}
		}
	};
	
	private static final int POSITIVE = 0;
	private static final int NEGATIVE = 1;

	// ------------------------------------------------------------ END STATICS

	private String expression;
	private List<Tuple<String, Integer>> tokens;
	private List<Exp> exps;
	private boolean executed = false;
	
	public TokensToExp(List<Tuple<String, Integer>> tokens, String expression) {
		this.tokens = tokens;
		this.expression = expression;
	}

	public List<Exp> convert() throws Unexpected, ExpectedExpression, BadExpression, FunctionDoesNotExists {
		if (!executed) {
			exps= new ArrayList<Exp>();
			List<String> unprocessed = new ArrayList<String>();

			for (int i = 0, len = tokens.size(); i < len; i++) {
				String token = tokens.get(i).getA();
				Exp exp = idenfityExp(token, i);

				exps.add(exp);

				if (!ok(exp)) {
					unprocessed.add(token);
				}
			}

			resolveUnprocesed(unprocessed);

			tryToOptimizePositiveAndNegativeSignals();

			executed = false;
		}

		return exps;
	}
	
	protected Exp idenfityExp(String str, int i) throws FunctionDoesNotExists {
		Object o;
		
		// Casos especiais, para tratamento posterior
		if (str.equals("+")) {
			return null;
		} else if (str.equals("-")) {
			return null;
		}
		
		if (ok(o = getOperator(str))) {
			return (Operator)o;
		} else if (ok(o = getLiteral(str))) {
			return (Exp)o;
		} else if (ok(o = getFunction(str, i))) {
			return (Exp)o;
		} else if (ok(o = getIndentifier(str))) {
			return (Exp)o;
		}
		
		return null;
	}
	
	protected boolean ok(Object o) {
		return o != null;
	}
	
	protected Operator getOperator(String s) {
		Class<? extends Operator> opC = ops.get(s);

		if (opC != null) {
			try {
				Operator op = opC.newInstance();
				return op;
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}

		return null;
	}
	
	private Exp getLiteral(String str) {
		Object o;
		Exp exp = null;
		
		if (ok(getNull(str))) {
			return new Literal(Const.NULL);
		} else if (ok(o = getBoolean(str))) {
			return new Literal(o);
		} else if (ok(o = getNumber(str))) {
			return new Literal(o);
		} else if (ok(o = getString(str))) {
			return new Literal(o);
		}
		
		return null;
	}

	private Object getFunction(String str, int i) throws FunctionDoesNotExists {
		if (tokens.size() -1 > i && tokens.get(i + 1).getA().equals("(")) {
			Class<? extends Function> fclass = FunctionsRegister.instance().getFunctionClass(str);

			if (fclass == null) {
				throw new FunctionDoesNotExists(expression, tokens.get(i).getB(), "Function \"", str, "\" does not exists");
			}

			try {
				return fclass.newInstance();
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}
		
		return null;
	}

	private Object getIndentifier(String str) {
		return new Identifier(str);
	}

	// @TODO: Lançar exceção de fim de string
	private String getString(String str) {
		char s = str.charAt(0);
		
		switch (s) {
			case '\'':
			case '"':
				char l = str.charAt(str.length() - 1);

				if (s == l) {
					return str.substring(1, str.length() - 1);
				}
			default:
				return null;
		}
	}
	
	private Boolean getBoolean(String str) {
		if (str.equals("true")) {
			return true;
		} else if (str.equals("false")) {
			return false;
		}
		
		return null;
	}
	
	private Null getNull(String str) {
		return str.equals("null") ? Const.NULL : null;
	}
	
	public static Number getNumber(String str) {
		int len = str.length(), lastIdx = len - 1, numDots = 0;
		int isLong = 0, isDouble = 0, isFloat = 0;
		
		char ch0 = str.charAt(0);

		switch (ch0) {
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
			case '0':
			case '.':
				break;
			default:
				return null;
		}

		for (int i = 0; i < len; i++) {
			char ch = str.charAt(i);

			switch (ch) {
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':
				case '0':
					break;
			
				// LONG
				case 'l':
				case 'L':
					if (i < lastIdx) {
						throw new NumberFormatException(str);
					}

					isLong += 1;
					break;
				
				// FLOAT
				case 'f':
				case 'F':
					if (i < lastIdx) {
						throw new NumberFormatException(str);
					}

					isFloat += 1;
					break;
				
				// DOUBLE
				case 'd':
				case 'D':
					if (i < lastIdx) {
						throw new NumberFormatException(str);
					}

					isDouble += 1;
					break;
				
				// DOT
				case '.':
					if (numDots > 1) {
						throw new NumberFormatException(str);
					}

					numDots += 1;
					break;
				
				default:
					throw new NumberFormatException(str);
			}
		}
		
		if (numDots > 0) {
			if (!(isLong > 0)) {
				if (isFloat > 0) {
					return Float.valueOf(str);
				} else { // Double > 0
					return Double.valueOf(str);
				}
			} else {
				throw new NumberFormatException(str);
			}
		} else {
			if (isLong > 0) {
				return Long.valueOf(str.substring(0, lastIdx));
			} else if (isDouble > 0) {
				return Double.valueOf(str);
			} else if (isFloat > 0) {
				return Float.valueOf(str);
			} else {
				return Integer.valueOf(str);
			}
		}
	}

	// @TODO: muito código repetido
	private void resolveUnprocesed(List<String> unprocessed) throws Unexpected, ExpectedExpression, BadExpression {

		if (unprocessed.size() > 0) {
			for (int i = 0, u = 0, len = exps.size(); i < len; i++) {
				Exp exp = exps.get(i);

				if (exp == null) {
					String op = unprocessed.get(u++);
					int prevIdx = i - 1, nextIdx = i + 1;
					int signal = op.equals("+") ? POSITIVE : NEGATIVE;
					PositiveNegative pn = PN[signal];

					if (i > 0) {
						Exp p = exps.get(prevIdx);

						if (p instanceof Operator && !(p instanceof RParenthesis)) {
							if (nextIdx < len) {
								Exp n = exps.get(nextIdx);

								if (n instanceof Token || n instanceof LParenthesis) {
									exps.set(i, pn.signal());
								} else {
									throw new Unexpected(expression, tokens.get(i).getB(), "Unexpected expression after ", i + 1, "º token ", op); // operador no lugar errado
								}
							} else {
								throw new ExpectedExpression(expression, tokens.get(i).getB(), "Expected expression after ", i + 1, "º token ", op); // faltando elemento à direita do +/-
							}
						} else { // p instaceof Token || p is ")"
							if (nextIdx < len) {
								Exp n = exps.get(nextIdx);

								if (n instanceof Token || n instanceof LParenthesis || n instanceof Function) {
									exps.set(i, pn.operator());
								} else {
									throw new Unexpected(expression, tokens.get(i).getB(), "Bad expression after ", i + 1, "º token ", op); // operador no lugar errado
								}
							} else {
								throw new ExpectedExpression(expression, tokens.get(i).getB(), "Expected expression after ", i + 1, "º token ", op); // faltando elemento à direita do +/-
							}
						}

					} else if (nextIdx < len) { // i == 0
						Exp n = exps.get(nextIdx);

						if (n instanceof Token || n instanceof LParenthesis || n instanceof Function) {
							exps.set(i, pn.signal());
						} else {
							throw new Unexpected(expression, tokens.get(i).getB(), "Unexpected expression after ", i + 1, "º token ", op); // operador no lugar errado
						}
					} else {
						throw new ExpectedExpression(expression, tokens.get(i).getB(), "Missing expression after ", i + 1, "º token ", op); // faltando elemento à direita do +/-
					}
				}
			}
		}
	}

	private void tryToOptimizePositiveAndNegativeSignals() {
		List<Exp> temp = new ArrayList<Exp>();
		
		for (int i = 0, len = exps.size(); i < len; i++) {
			Exp exp = exps.get(i);
			int n = i + 1;
			
			if (exp instanceof Negative || exp instanceof Positive) {
				Exp next = exps.get(n);

				if ((next instanceof Literal) && (((Literal)next).getVal() instanceof Number)) {
					Literal l;
					
					if (exp instanceof Positive) {
						l = new Literal(Positive.execute(((Literal)next).getVal()));
					} else { // exp intanceof Negative
						l = new Literal(Negative.execute(((Literal)next).getVal()));
					}

					temp.add(l);

					i = n;
				} else {
					temp.add(exp);
				}
			} else {
				temp.add(exp);
			}
		}
		
		exps.clear();
		exps.addAll(temp);
	}
}
