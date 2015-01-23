package org.futurepages.util.html;


import org.futurepages.util.StringUtils;


/**
 *
 * @author Leandro
 */
public class RichTextTagReplacer extends HtmlTagReplacer {

	private boolean styles;
	private boolean lists;
	private boolean image;
	private boolean anchor;
	private boolean table;

	private RichTextTagReplacer() {}

	public RichTextTagReplacer(boolean styles, boolean lists, boolean image, boolean anchor, boolean table) {
		this.styles = styles;
		this.lists = lists;
		this.image = image;
		this.anchor = anchor;
		this.table = table;
	}

	/**
	 * Ao construir-se o replacer, serão adicionados ao mapa as tags a serem
	 * tratadas de acordo com os parâmetros.
	 *
	 * Formas de tratar as tags:
	 * 1) KEEP    - Mantém toda a estrutura da tag, modificando somente o
	 *				identificador passado como parâmetro.
	 *    -> ex.: in = <a href="#">  keep("a","b")  ==>  <b href="#">
	 * 2) REDUCE  - reduz a estrutura de atributos da tag
	 *     -> ex.: in = <a href="#">  reduce("a","b")  ==>  <b>
	 * 3) REPLACE - substitui toda a tag pelo conteúdo passado por parâmetro
	 *     -> ex.: in = <a href="#">  replace("a","b")  ==>  b
	 *
	 */
	@Override
	void init() {
		reduce("br");
		if (styles) {
			keep("div");
			keep("p");
			keep("strong", "strong");
			keep("em");

			keep("i"   , "em");
			keep("cite", "em");
			keep("var" , "em");
			keep("code", "em");
			keep("samp", "em");
			keep("kbd" , "em");
			keep("dfn" , "em");

			keep("u", "span " + STYLE_UNDERLINE, "span"); //@TODO questionável!! Poderia terminar acontecendo de um span ficar com dois styles não?
			keep("address", "em");
			keep("b", "strong");
			keep("big", "strong");
			keep("h1");
			keep("h2");
			keep("h3");
			keep("h4");
			keep("h5");
			keep("h6");

		} else {
			reduce("div","p");
			reduce("p");
			reduce("strong");

			reduce("em");
			reduce("i"   , "em");
			reduce("cite", "em");
			reduce("var" , "em");
			reduce("code", "em");
			reduce("samp", "em");
			reduce("kbd" , "em");
			reduce("dfn" , "em");

			reduce("u", "span " + STYLE_UNDERLINE, "span");
			reduce("address", "em");
			reduce("b", "strong");
			reduce("big", "strong");
			replace("h1", P_STRONG_OPEN , P_STRONG_CLOSE);
			replace("h2", P_STRONG_OPEN , P_STRONG_CLOSE);
			replace("h3", P_STRONG_OPEN , P_STRONG_CLOSE);
			replace("h4", P_STRONG_OPEN , P_STRONG_CLOSE);
			replace("h5", P_STRONG_OPEN , P_STRONG_CLOSE);
			replace("h6", P_STRONG_OPEN , P_STRONG_CLOSE);
		}

		if (lists) {
			if (styles) {
				keep("ul");
				keep("ol");
				keep("li");
				keep("blockote");
				keep("cite");
				keep("var");
				keep("code");
				keep("samp");
				keep("kbd");
				keep("dfn");
			} else {
				reduce("ul");
				reduce("ol");
				reduce("li");
				reduce("blockote");
				reduce("cite");
				reduce("var");
				reduce("code");
				reduce("samp");
				reduce("kbd");
				reduce("dfn");
			}
		} else { // !lists
			reduce("ul", "p");
			reduce("ol", "p");
			replace("li", PLAIN_LI, BREAK_LINE);
		}

		if (image) {
			keep("object");
			keep("embed");
			keep("param");
			keep("iframe");
			if(styles){
				keep("img");
			} else {
				reduce("img", attrs("src","alt"));
			}
		}

		if (anchor) {
			if(styles){
				keep("a");
			} else {
				reduce("a",attrs("href","target"));
			}
		} else {
			reduce("a", "span "+STYLE_UNDERLINE, "span");
		}

		if (table) {
			if (styles) {
				keep("table");
				keep("caption");
				keep("tbody");
				keep("thead");
				keep("tfoot");
				keep("tr");
				keep("th");
				keep("td");
			} else {
				reduce("table");
				reduce("caption");
				reduce("tbody");
				reduce("thead");
				reduce("tfoot");
				reduce("tr", attrs("rowspan"));
				reduce("th", attrs("colspan"));
				reduce("td", attrs("colspan"));
			}
		} else { //!table
			if (styles) {
				keep("tr",    "p");
				keep("th",    "p");
				keep("thead", "p");
				keep("tfoot", "p");
			} else {
				reduce("tr",    "p");
				reduce("th",    "p");
				reduce("thead", "p");
				reduce("tfoot", "p");
			}
			replace("td", PLAIN_LI, BREAK_LINE);
		}
	}

	/**
	 *
	 * @param tag tanto de abertura como de fechamento, com os limitadores <>
	 * @return retorna a tag convertida de acordo com os parâmetros passados.
	 */
	@Override
	public String treated(String tag) {
		if(StringUtils.isEmpty(tag)){
			return "";
		}
		boolean isClosing = isClosingTag(tag);
		String[] tagParts = tagParts(tag, isClosing);
		String treated ;
		TagReplacement tagRep = getReplacement(tagParts[0]);
		if (tagRep == null) {
			treated = "";
		}else{
			treated = treat(tagParts, isClosing, tagRep);
		}
		return treated;
	}


	@Override
	public String beforeTreatment(String html) {
		if(!styles){
			return noStylesText(html);
		} else {
			return noTrashText(html);
		}
	}

}