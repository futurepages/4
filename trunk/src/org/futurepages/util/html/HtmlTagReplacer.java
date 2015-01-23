package org.futurepages.util.html;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import org.futurepages.util.iterator.string.IterableString;
import org.futurepages.util.iterator.string.MatchedToken;
import static org.futurepages.util.html.HtmlRegex.*;

/**
 *
 * @author Leandro
 */
public abstract class HtmlTagReplacer {

	public static String PLAIN_LI = "&nbsp; - ";
	public static String STYLE_UNDERLINE = "style=\"text-decoration:underline;\"";
	public static String TARGET_BLANK= "target=\"_blank\"";
	public static String BREAK_LINE = "<br/>";
	public static String P_STRONG_OPEN  = "<p><strong>";
	public static String P_STRONG_CLOSE = "</strong></p>";
	public Pattern NON_WORD_REGEX_PATTERN;
	
	private Map<String, TagReplacement> tagsToCare = new HashMap<String, TagReplacement>();

	public HtmlTagReplacer() {
		NON_WORD_REGEX_PATTERN = Pattern.compile("\\W");
	}


	abstract void init();

	public String beforeTreatment(String strippedHtml){
		return strippedHtml;
	}

	public abstract String treated(String tag);

	public String afterTreatment(String treatedHtml) {
		return treatedHtml;
	}

	/**
	 * Substitui texto 'str' encontrado somente dentro das tags pela regex
	 * com o valor do atributo 'replacement'
	 *
	 * @param str texto html a ser varrido
	 * @param regex padrão para substituição
	 * @param replacement novo valor
	 *
	 * @return texto substituído
	 */
	public static String replaceInTags(String str, String regex, String replacement){

		Pattern tagsPattern  = getCompiledTagsPattern();
		IterableString iter = new IterableString(tagsPattern, str);

		String end = str;
		Pattern regexPattern = Pattern.compile(regex);
		StringBuilder sb     = new StringBuilder();
		for (MatchedToken token : iter) {
			sb.append(token.getBefore());
			sb.append(regexPattern.matcher(token.getMatched()).replaceAll(replacement));
			end = token.getAfter();
		}
		sb.append(end);
		return sb.toString();
	}

	/**
	 * retira lixo do html:
	 * 1) remove comentários
	 * 2) remove tag xml gerada pelo word
	 * 3) remove html script (javascript por exemplo)
	 * 4) remove tags vazias
	 * 5  troca aspas simples por aspas duplas dentro das tags
	 * 6) remove atributos inválidos
	 */
	public static String noTrashText(String htmlContent) {
		String commentPattern   = commentPattern();
		String xmlPattern       = tagAndContentPattern("xml");
		String headPattern       = tagAndContentPattern("head");
		String scriptPattern    = tagAndContentPattern("script");


		htmlContent =htmlContent.replaceAll(commentPattern   ,  ""); //remove comentários
		htmlContent =htmlContent.replaceAll(xmlPattern       ,  ""); //remove tag xml gerada pelo word
		htmlContent =htmlContent.replaceAll(headPattern      ,  ""); //remove tag xml gerada pelo word
		htmlContent =htmlContent.replaceAll(scriptPattern    ,  ""); //remove html script (javascript por exemplo)

		//Comentado - decidiu-se não tirar tags vazias
		//String emptyTagsPattern = emptyTagsPattern();
		//htmlContent =htmlContent.replaceAll(emptyTagsPattern ,  ""); //remove tags vazias

		htmlContent = replaceInTags(htmlContent, "'" , "\"");                 //aspas simples por aspas duplas
		htmlContent = replaceInTags(htmlContent, invalidAttrPattern()  ,  "");//atributos inválidos

		return htmlContent;
	}

	/**
	 * 0) Retira-se o lixo utilizando-se do método noTrashText
	 * 1) Retira-se as tags [style] com seu conteúdo
	 * 2) Retira os atributos "style" e "class" das tags com duas ressalvas:
	 *  - [span style="font-weight:bold"] é substituído e [strong]
	 *  - [span style="text-decoration:underline"] é substituído por [u]
	 * @return texto sem estilo, exceto estilos com bold e underline
	 */
	public static String noStylesText(String htmlContent) {
		htmlContent = noTrashText(htmlContent);
		htmlContent = htmlContent.replaceAll(tagAndContentPattern("style") , "");

		htmlContent = replaceInTags(htmlContent, attrPattern("class"), "");
		htmlContent = htmlContent.replaceAll(spanWithStylePropertiePattern("font-weight","bold"),tagWithContentReplacement("strong"));    //estilizados com negrito
		htmlContent = htmlContent.replaceAll(spanWithStylePropertiePattern("text-decoration","underline"),tagWithContentReplacement("u"));//estilizados com sublinhado
		htmlContent = replaceInTags(htmlContent, attrPattern("style"), "");
		return htmlContent;
	}

	protected String treat(String[] tagParts, boolean closing, TagReplacement tagRep) {
		if (tagRep.reduce == null) {
			return closing ? tagRep.close : tagRep.open;
		} else {
			return tagRep.execute(tagParts, closing);
		}
	}

	/**
	 * Exemplo 1: entrada <a href="#"> , a saída será: {'a', 'href="#"' , '>'}
	 * Exemplo 2: entrada </a> , a saída será: {'a', '' , '>'}
	 */
	protected String[] tagParts(String tag, boolean closing) {
		int start = (closing) ? 2 : 1;
		String tagId = NON_WORD_REGEX_PATTERN.split(tag.substring(start))[0].toLowerCase();


		int end = (tag.charAt(tag.length()-2)=='/') ? 1 : 0;

		String attributes = tag.substring(start+tagId.length() ,tag.length() - end-1);
		String endFlag = (end==1)?"/>":">";

		return new String[]{tagId,attributes,endFlag};
	}

	protected boolean isClosingTag(String tag) {
		return tag.charAt(1) == '/';
	}

	protected TagReplacement getReplacement(String tagId) {
		return tagsToCare.get(tagId);
	}

	protected void register(String tagId, TagReplacement tagReplacement) {
		tagsToCare.put(tagId, tagReplacement);
	}

	// REPLACE ###################################
	protected void replace(String tagIdFrom, String tagIdTo) {
		register(tagIdFrom, new TagReplacement(tagIdTo, tagIdTo, null));
	}

	protected void replace(String tagIdFrom, String tagIdToOpen, String tagIdToClose) {
		register(tagIdFrom, new TagReplacement(tagIdToOpen, tagIdToClose, null));
	}

	// KEEP #####################################
	protected void keep(String tagId) {
		register(tagId, new TagReplacement(tagId, tagId, false));
	}

	protected void keep(String tagIdFrom, String tagIdTo) {
		register(tagIdFrom, new TagReplacement(tagIdTo, tagIdTo, false));
	}

	protected void keep(String tagIdFrom, String tagIdToOpen, String tagIdToClose) {
		register(tagIdFrom, new TagReplacement(tagIdToOpen, tagIdToClose, false));
	}

	protected void keep(String tagId, String[] tagsAttributes) {
		register(tagId, new TagReplacement(tagId, tagId, false, tagsAttributes));
	}

	protected void keep(String tagIdFrom, String tagIdTo, String[] tagsAttributes) {
		register(tagIdFrom, new TagReplacement(tagIdTo, tagIdTo, false, tagsAttributes));
	}

	protected void keep(String tagIdFrom, String tagIdToOpen, String tagIdToClose, String[] tagsAttributes) {
		register(tagIdFrom, new TagReplacement(tagIdToOpen, tagIdToClose, false, tagsAttributes));
	}

	// REDUCE #####################################
	protected void reduce(String tagId) {
		register(tagId, new TagReplacement(tagId, tagId, true));
	}

	protected void reduce(String tagIdFrom, String tagIdTo) {
		register(tagIdFrom, new TagReplacement(tagIdTo, tagIdTo, true));
	}

	protected void reduce(String tagIdFrom, String tagToOpen, String tagToClose) {
		register(tagIdFrom, new TagReplacement(tagToOpen, tagToClose, true));
	}

	protected void reduce(String tagId, String[] tagsAttributes) {
		register(tagId, new TagReplacement(tagId, tagId, true, tagsAttributes));
	}

	protected void reduce(String tagIdFrom, String tagIdTo, String[] tagsAttributes) {
		register(tagIdFrom, new TagReplacement(tagIdTo, tagIdTo, true, tagsAttributes));
	}

	protected void reduce(String tagIdFrom, String tagToOpen, String tagToClose, String[] tagsAttributes) {
		register(tagIdFrom, new TagReplacement(tagToOpen, tagToClose, true, tagsAttributes));
	}

	protected String[] attrs(String... attrIds){
		return attrIds;
	}
}