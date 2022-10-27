package org.futurepages.util.html;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.futurepages.formatters.brazil.SmartTextFormatter;
import org.futurepages.util.Is;
import org.futurepages.util.The;
import static org.futurepages.util.html.HtmlRegex.*;
import org.futurepages.util.iterator.string.IterableString;
import org.futurepages.util.iterator.string.MatchedToken;

/**
 *
 * @author Leandro
 *
 *	<span style="white-space: pre;"> </span> é gerado a cada tab dado dentro de um parágrafo.
 *	utilizar algum tipo de substituição por text-indent
 *	------------------------------------------------
 *	<cite> e <var> sao mantidos quando list é true
 *
 */
public class AlternativeHtmlTagReplacer extends HtmlTagReplacer {


	private String host;
	private boolean styles;
	private boolean textAlign;
	private boolean objects;

	private AlternativeHtmlTagReplacer() {
	}


	public AlternativeHtmlTagReplacer(String host, boolean styles) {
		this.styles = styles;
		this.textAlign = styles;
		this.host=host;
	}
	public AlternativeHtmlTagReplacer(String host, boolean styles,boolean textAlign, boolean objects) {
		this(host,styles,textAlign);
		this.objects = objects;
	}

	//caso nao queira textAlign: ficará styles = true e textAlign = false.
	public AlternativeHtmlTagReplacer(String host, boolean styles, boolean textAlign) {
		this.styles = styles;
		this.host=host;
		this.textAlign=textAlign;
	}

	public String getHost() {
		return host;
	}

	public boolean isStyles() {
		return styles;
	}

	public boolean isTextAlign() {
		return textAlign;
	}

	/**
	 * Se internalAnchors == null , significa que não manteremos os links (serão eliminados)
	 */
	public void init() {

		//STYLES GERAIS
		reduce("br");
		reduce("strong");
		reduce("em");
		reduce("i", "em");

		reduce("u", "span " + STYLE_UNDERLINE, "span");
		reduce("address", "em");
		reduce("b", "strong");
		reduce("big", "strong");
		replace("h1", P_STRONG_OPEN, P_STRONG_CLOSE);
		replace("h2", P_STRONG_OPEN, P_STRONG_CLOSE);
		replace("h3", P_STRONG_OPEN, P_STRONG_CLOSE);
		replace("h4", P_STRONG_OPEN, P_STRONG_CLOSE);
		replace("h5", P_STRONG_OPEN, P_STRONG_CLOSE);
		replace("h6", P_STRONG_OPEN, P_STRONG_CLOSE);

		if(styles){
			keep("div", "p");
			keep("p");
		}else{
			reduce("div", "p");
			reduce("p");
		}

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

		if(objects){
			keep("img");
			keep("iframe");
			keep("object");
		}else{

		}

		//âncoras
		if (host != null) {
			reduce("a", attrs("href", "target", "title"));
		} else {
			reduce("a", "span " + STYLE_UNDERLINE, "span");
		}

		//tabelas
		reduce("tr", "p");
		reduce("th", "p");
		reduce("thead", "p");
		reduce("tfoot", "p");
		replace("td", PLAIN_LI, BREAK_LINE);
	}

	/*
	 * Validar url das imagens.
	 * Se a imagem não apontar para os hosts certos, removê-las.
	 */
	@Override
	public String beforeTreatment(String htmlContent) {
		htmlContent = noTrashText(htmlContent);
		if(!objects){
			htmlContent = htmlContent.replaceAll(tagAndContentPattern("style") , "");
			htmlContent = replaceInTags(htmlContent, attrPattern("class"), "");
		}
		htmlContent = htmlContent.replaceAll(spanWithStylePropertiePattern("font-weight","bold"),tagWithContentReplacement("strong"));    //estilizados com negrito
		htmlContent = htmlContent.replaceAll(spanWithStylePropertiePattern("text-decoration","underline"),tagWithContentReplacement("u"));//estilizados com sublinhado
		if(!styles){
			htmlContent = replaceInTags(htmlContent, attrPattern("style"), "");
		}else {
			//indentação de parágrafo.
			htmlContent = htmlContent.replaceAll("<p><span style=\"white-space: pre;\"> </span>","<p style=\"text-indent: 30px\">");
		}
		return htmlContent;

	}

	@Override
	public String afterTreatment(String treatedHtml) {
		if(!objects){
			treatedHtml = anchorTreatment(treatedHtml);
			treatedHtml = styleTreatment(treatedHtml);
		}
		return treatedHtml;
	}
	
	public String anchorTreatment(String content) {
		if (!Is.empty(host)) {
			Pattern aTagsPattern = Pattern.compile(HtmlRegex.tagAndContentPattern("a"));
			IterableString iter = new IterableString(aTagsPattern, content);
			StringBuilder sb = new StringBuilder();
			StringBuilder sbUrlOuts = new StringBuilder();
			String end = content;

			for (MatchedToken token : iter) {
				sb.append(token.getBefore());
				String treatedAnchor = treatedAnchor(token.getMatched());
				sb.append(treatedAnchor);
				end = token.getAfter();
				sbUrlOuts.append(token.getBefore());
				sbUrlOuts.append(The.sequence('#', treatedAnchor.length())); //marca as tags a com seus conteúdos
			}

			sb.append(end);
			sbUrlOuts.append(end);

			//Procura de urls avulsas (fora da tag 'a')
			end = sbUrlOuts.toString();
			iter = new IterableString(getCompiledUrlPattern(), end);
			int offset = 0;
			for (MatchedToken token : iter) {
				String foundURL = token.getMatched();
				if(!foundURL.contains("://")){
					//noinspection HttpUrlsUsage
					foundURL = "http://"+foundURL;
				}
				String treatedAnchor = treatedAnchor(The.concat("<a href=\"",foundURL,"\">",foundURL,"</a>"));
				sb.replace(token.getStart()+offset, token.getEnd()+offset ,
				   treatedAnchor
				);
				offset = offset + treatedAnchor.length() + token.getBefore().length();

			}

			content = sb.toString();
		}

		return content;
	}
	
	public String styleTreatment(String content) {
		if (styles) {
			Pattern tagsPatternP = pTagPattern();
			IterableString iterP = new IterableString(tagsPatternP, content);
			StringBuilder sbP = new StringBuilder();
			String endP = content;

			for (MatchedToken tokenP : iterP) {
				sbP.append(tokenP.getBefore());
				sbP.append(treatedP(tokenP.getMatched()));
				endP = tokenP.getAfter();
			}

			sbP.append(endP);

			content = sbP.toString();
		}

		return content;
	}
	
/**
 *
 * @param tag tanto de abertura como de fechamento, com os limitadores <>
 * @return retorna a tag convertida
 */
	@Override
	public String treated(String tag) {
		if (Is.empty(tag)) {
			return "";
		}
		boolean isClosing = isClosingTag(tag);
		String[] tagParts = tagParts(tag, isClosing);
		String treated;
		TagReplacement tagRep = getReplacement(tagParts[0]);
		if (tagRep == null) {
			treated = "";
		} else {
			treated = treat(tagParts, isClosing, tagRep);
		}
		return treated;
	}

	public String treatedAnchor(String tagA) {

		Pattern linkPattern = aTagPattern();
		Matcher matcher = linkPattern.matcher(tagA);
		StringBuilder sb = new StringBuilder();
		if (matcher.find()) {
			String parteAbertura = matcher.group(1);
			String mg3 = matcher.group(3);
			String conteudo = mg3.trim();
			String espacoInicio;
			String espacoFinal;
			if (!mg3.isEmpty()) {
				espacoInicio = (mg3.substring(0, 1).trim().equals("")?" ":"");
				espacoFinal = (mg3.substring(mg3.length()-1).trim().equals("")?" ":"");
			}
			else {
				espacoInicio=" ";
				espacoFinal="";
			}
			String parteFechar = matcher.group(4);

			IterableString iter = new IterableString(Pattern.compile(HtmlRegex.attrsPattern()), parteAbertura);

			sb.append(espacoInicio).append("<a ");
			for (MatchedToken tokenOpen : iter) {
				String[] group = tokenOpen.getMatched().split("\\s*=", 2);
				String attr = group[0].trim();
				String url = group[1].trim().replaceAll("(\")", "");
				if (attr.equalsIgnoreCase("href")) {
					if (url.startsWith(host)) {
						sb.append("href=\"").append(url.replaceAll(host, "")).append("\" ");
					} else {
						if (!url.startsWith("javascript")) {
							sb.append("href=\"").append(url).append("\" ").append(TARGET_BLANK).append(" ");
						}
					}
					if (attr.equalsIgnoreCase("href") && url.equalsIgnoreCase(conteudo)) {
						conteudo = SmartTextFormatter.shortUrl(conteudo);
						if (!tagA.contains("title=\"")) {
							sb.append("title=\"").append(url).append("\" ");
						}
					}
				} else if (styles && !attr.equalsIgnoreCase("target") && !attr.equalsIgnoreCase("title")) {
					sb.append(tokenOpen.getMatched());
				}

				if (attr.equalsIgnoreCase("title") && !sb.toString().contains("title=\"")) {
					sb.append(tokenOpen.getMatched());
				}
			}
			sb.append(">").append(conteudo).append(parteFechar).append(espacoFinal);

			return sb.toString();
		} else {
			Matcher matcherOutro = Pattern.compile("(?i)(?s)(<a>)(.*?)(</a>)").matcher(tagA);
			if (matcherOutro.find()) {
				return matcherOutro.group(2);
			} else {
				return "";
			}
		}
	}
	
	private static Pattern P_TAG_PATTERN = null;
	private static Pattern P_TAG_ATTRS = null;
	private static Pattern A_TAG_PATTERN = null;

	protected static Pattern pTagPattern(){
		if(P_TAG_PATTERN==null){
			P_TAG_PATTERN = Pattern.compile("(?i)(?s)(<p(\\s+).*?>)(.*?)(</p>)");
		}
		return P_TAG_PATTERN;
	}

	protected static Pattern aTagPattern(){
		if(A_TAG_PATTERN==null){
			A_TAG_PATTERN = Pattern.compile("(?i)(?s)(<a(\\s+).*?>)(.*?)(</a>)");
		}
		return A_TAG_PATTERN;
	}
	
	protected static Pattern pTagAttrs() {
		if (P_TAG_ATTRS == null) {
			P_TAG_ATTRS = Pattern.compile("(<p)([^>]*)(>)");
		}
		
		return P_TAG_ATTRS;
	}

	public String treatedP(String tagP) {

		Pattern pTagPattern = pTagPattern();
		Matcher matcher = pTagPattern.matcher(tagP);
		StringBuilder sb = new StringBuilder();
		if (matcher.find()) {
			String parteAbertura = matcher.group(1);
			String conteudo = matcher.group(3).trim();
			String parteFechar = matcher.group(4);

			IterableString iter = new IterableString(Pattern.compile(HtmlRegex.attrsPattern("style")), parteAbertura);

			sb.append("<p ");
			for (MatchedToken tokenOpen : iter) {
				String[] group = tokenOpen.getMatched().split("\\s*=",2);
				String attrValue = group[1].trim().replaceAll("(\")", "");
				// aqui colocar outros atributos do style que devem ser mantidos
				IterableString iterValue = new IterableString(Pattern.compile(HtmlRegex.attrValuesPattern(textAlign? new String[]{"padding-left","text-align"} : new String[]{"padding-left"})), attrValue);
				String value="";
				for (MatchedToken tokenValueAttr : iterValue) {
					value=The.concat(value,tokenValueAttr.getMatched(),";");
				}
				if(!Is.empty(value)){
					sb.append("style=\"").append(value).append("\" ");
				}
			}
			Matcher m = pTagAttrs().matcher(tagP);

			if (m.find()) {
				sb.append(paragraphAttrs(m.group(2)));
			}

			sb.append(" >").append(conteudo).append(parteFechar);

			return sb.toString();
		}
		else{
			return tagP;
		}
	}

	protected String paragraphAttrs(String pAttrs) {
		return "";
	}
}
