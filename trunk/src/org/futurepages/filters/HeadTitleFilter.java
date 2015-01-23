package org.futurepages.filters;

import org.futurepages.core.action.Action;
import org.futurepages.core.action.AsynchronousManager;
import org.futurepages.core.config.Params;
import org.futurepages.core.control.InvocationChain;
import org.futurepages.core.filter.Filter;
import org.futurepages.core.output.Output;
import org.futurepages.util.Is;
import org.futurepages.util.The;


/**
 * Define um texto padrão para o head-title da página
 * de consequência da action (e suas innerActions)
 *
 * Caso 1: Não necessita de WebContainer, coloca-se no head.jsp:  <title>Nome do App ${headTitle}</title>
 * HEAD_TITLE: "Slogan"
 *		- na página inicial (default), o title fica: "Nome do App - Slogan"
 *		- nas demais páginas: "Nome do App - Path - Página Interna"
 *
 * Caso 2: Necessita de Webcontainer, não necessita especificar o <title> no head. o WebContainer gerará.
 * HEAD_TITLE: "Slogan"
 * PRETTY_HEAD_TITLE: Nome do App
 *		- na página inicial (default), o title fica: "Nome do App - Slogan"
 *		- nas demais páginas: "Página Interna - Path Reverso = App"
 * @author leandro
 */
public class HeadTitleFilter implements Filter {

    public static final String SEPARATOR = " - ";

	public static String prettyTitle = "";

	public static String globalTitle = "";

	public static void itsPretty(String itsPrettyTitle) {
		prettyTitle = itsPrettyTitle;
	}

	public static boolean isPretty() {
		return !Is.empty(prettyTitle);
	}

	public static String getPrettyTitle() {
		return prettyTitle;
	}

	public static String getGlobalTitle() {
		return globalTitle;
	}

    private String headTitle;   //chave do input do objeto que sofrerá a injeção

    public HeadTitleFilter() {
		setGlobalTitle();
	}

	public static void setGlobalTitle() {
		globalTitle = Params.get("GLOBAL_HEAD_TITLE");
	}

    public HeadTitleFilter(String headTitle) {
			this.headTitle = headTitle;
    }

	@Override
    public String filter(InvocationChain chain) throws Exception {
        if(!(AsynchronousManager.isAsynchronousAction(chain) )){
			String titleValue = "";
			Output output = chain.getAction().getOutput();
			//@Deprecated
			if(!isPretty()){
				titleValue = Is.empty(headTitle)? globalTitle : headTitle;
			}else{
				titleValue = headTitle;
			}
			output.setValue(Action.HEAD_TITLE, titleValue);
        }
        return chain.invoke();
    }
	
	@Override
    public void destroy() {
    }
}