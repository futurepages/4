package org.futurepages.menta.core.template;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Esta classe representa uma pagina. Seguindo o paradigma de blocos do
 * mentatemplates, uma pagina pode ser composta por varios blocos, os quais
 * tambem sao paginas. Uma pagina tambem pode derivar de outra, revelando assim
 * um conceito de heranca entre paginas. <br>
 * Alem disso, o atributo path pode ser um expressao regular. Isso traz grandes
 * possibilidades, e usaremos este mecanismo para dar suporte a convencoes.
 *
 * @author Davi Luan Carneiro
 */
public class Page {

    private String path,  view;
    private Map<String, Object> blocks = new HashMap<String, Object>();
    private Pattern pattern;

    /**
     * Constroi um Page apenas usando a pagina jsp. Geralmente usado para os
     * pages base, ou para pages que sao blocos de outros.
     *
     * @param view
     *            Pagina jsp
     */
    public Page(String view) {
        this.view = view;
    }

    /**
     * @param path
     *            O path que devera ser usado para o acesso a esse page
     * @param view
     *            Pagina jsp
     */
    public Page(String path, String view) {
        this(view);
        this.path = path;
    }

    /**
     * Utiliza o conceito de heranca, para construir um novo page. So nao herda
     * o path, que e exclusivo de cada page.
     * Atencao: alteracoes em tempo de execucao de classes pai nao irao alterar
     * as classes filhas. A heranca tem o fim de facilitar a configuracao, eliminando
     * a necessidade de codificacoes repetitivas.
     *
     * @param pageSuper
     *            Page pai
     */
    public Page(Page pageSuper) {
        this(pageSuper.getView());
        blocks.putAll(pageSuper.getBlocks());
        this.pattern = pageSuper.getPattern();
    }

    /**
     * Muito usado para pages que herdam de um page base.
     *
     * @param path
     *            O path que devera ser usado para o acesso a esse page
     * @param pageSuper
     *            Page pai
     */
    public Page(String path, Page pageSuper) {
        this(pageSuper);
        this.path = path;
    }

    /**
     * @param view
     *            Pagina jsp
     * @param listener
     *            Classe do listener
     */
    public Page(String view, Class listener) {
        this(view);
        this.pattern = Pattern.compile(path);
    }

    /**
     * @param path
     *            O path que devera ser usado para o acesso a esse page
     * @param view
     *            Pagina jsp
     */
    public Page(String path, String view, boolean withRule) {
        this(path, view);
		if(withRule){
			this.pattern = Pattern.compile(path);
		}
    }

    /**
     * @param pageSuper
     *            Page pai
     * @param listener
     *            Classe do listener
     */
    public Page(Page pageSuper, Class listener) {
        this(pageSuper);
    }

    /**
     * @param path
     *            O path que devera ser usado para o acesso a esse page
     * @param pageSuper
     *            Page pai
     * @param listener
     *            Classe do listener
     */
    public Page(String path, Page pageSuper, Class listener) {
        this(path, pageSuper);
        this.pattern = pageSuper.getPattern();
    }

    /**
     * @return Todos os blocos desse page
     */
    public Map<String, Object> getBlocks() {
        return blocks;
    }

    /**
     * @param id
     * @return O bloco desejado (que e uma instancia de Page)
     */
    public Page getBlock(String id) {
        return (Page) blocks.get(id);
    }

    /**
     * @param id
     * @param page
     * Page que sera setado para o bloco especificado
     */
    public void setBlock(String id, Page page) {
        blocks.put(id, page);
    }

    /**
     * @param id
     * @return String que representa o StringBlock
     */
    public String getStringBlock(String id) {
        return blocks.get(id) + "";
    }

	public boolean isWithRule(){
		return pattern != null;
	}

    /**
     * @param id
     * @param stringBlock
     * StringBlock a ser setado
     */
    public void setStringBlock(String id, String stringBlock) {
        blocks.put(id, stringBlock);
    }

    public String getPath() {
        return path;
    }

    private Pattern getPattern(){
        return this.pattern;
    }
//
//    public void setPath(String path) {
//        this.path = path;
//    }

//    public void setView(String view) {
//        this.view = view;
//    }

    public String getView() {
        return view;
    }


    /**
     * Verifies if the pattern matches the given patch
     * 
     * @param path - the input patch
     * @return true if matches
     */
    public boolean patternMatches(String path) {
        return (pattern == null) ? false : this.pattern.matcher(path).matches();
    }
}