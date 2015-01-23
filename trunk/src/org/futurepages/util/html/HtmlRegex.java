
package org.futurepages.util.html;
import java.util.regex.Pattern;
import static org.futurepages.util.StringUtils.concat;

/**
 * Gerador de Regex Patterns para capturar padrões de tags
 * @author leandro
 */
public class HtmlRegex {

	private static Pattern COMPILED_TAGS_PATTERN;
	private static Pattern COMPILED_TAGS_WITH_CONTENT_PATTERN;
	private static Pattern COMPILED_URL_PATTERN;

	// Patterns usados para "santinizar" Strings em relação ao JavaScript
	private static Pattern NEW_LINE = Pattern.compile("\n");
	private static Pattern CARRIAGE_RETURN = Pattern.compile("\r");
	private static Pattern SINGLE_QUOTE = Pattern.compile("'");
	private static Pattern DOUBLE_QUOTE = Pattern.compile("\"");
	private static Pattern OPEN_SCRIPT_TAG = Pattern.compile("<(script)([^>]*)((.|\\s)*?)>");
	private static Pattern CLOSE_SCRIP_TAG = Pattern.compile("</(script)>");

	/**
	 * Casa padrão da tag com seu conteúdo
	 * @param tagName
	 *  xml  ==> <xml>.*</xml>
	 * @return
	 */
	public static String tagAndContentPattern(String tagName){
		return concat("(?i)(?s)(<",tagName,"((\\s+).*?>.*?</",tagName,">)|(<",tagName,">.*?</",tagName,">))");		
	}

	//(?s)(?i)<span\s*style\s*=\s*"text-decoration:\s*underline\b.*?"[^>]*>.*?</span\s*>
	public static String spanWithStylePropertiePattern(String propertie, String value){
		return "(?s)(?i)<span\\s*style\\s*=\\s*\""+propertie+":\\s*"+value+"\\b.*?\"[^>]*>(.*?)</span\\s*>";
	}

	public static String tagWithContentReplacement(String tagName){
		return "<"+tagName+">$1</"+tagName+">";
	}

	public static String emptyTagsPattern(){
		return "(?s)(?i)<([\\w]+\\b)[^>]*?></\\1>";
	}

	public static String commentPattern() {
		return "(?s)<!--.*?-->";
	}

	//atributos inválidos, ex.: class=exampleClass
	public static String invalidAttrPattern() {
		return " [\\w]+=[\\w]+\\b";
	}

	//(?i)(?s) ?\bstyle\s*=\s*"[^"]+"
	public static String attrPattern(String name) {
		return "(?i)(?s) ?\\b"+name+"\\s*=\\s*\"[^\"]+\"";
	}

	public static String attrsPattern(String... attrs) {
		String atributos="";
		if(attrs!=null && attrs.length>0){
			for(String attr:attrs){
				atributos=concat(atributos,attr,"|");
			}
			atributos=atributos.substring(0, atributos.length()-1);
		}
		else{
			atributos="\\w+?";
		}
		return "(?i)(?s) ?\\b("+atributos+")\\s*=\\s*\"([^\"]+)\"";
	}

//text-decoration:underline;padding-left : 30px;
	public static String attrValuesPattern(String... attrs) {
		String atributos="";
		if(attrs!=null && attrs.length>0){
			for(String attr:attrs){
				atributos=concat(atributos,attr,"|");
			}
			atributos=atributos.substring(0, atributos.length()-1);
		}
		else{
			atributos="[^:]*";
		}
		return "(?i)(?s) ?\\b("+atributos+")\\s*:\\s*([^;]*)";
	}

	public static String attrPatternWithGroups(String name) {
		return "(?i)(?s)((?=.*)) ?\\b"+name+"\\s*=\\s*\"[^\"]+\"( ?(?=.*))";
	}

	/**
	 * Cria padrão regex que casa com as tags passadas como parâmetro (has=true) ou
	 * todas as tags exceto as passadas por parâmetro  (has=false)
	 * @param has com ou sem as tags passadas como parâmetro
	 * @param tagNames são os nomes das tags a serem casadas
	 * @return regex pattern para as tags
	 */
	// has = true  para "em","p" =>   </?(em\b|p\b).*?>
	// has = false para "em","p" =>   <(?!em\b)(?!/em)(?!p\b)(?!/p).*?>
	public static String tagsPattern(boolean has, String... tagNames){
		if(!has && tagNames.length==0){
			return null;
		}
		return concat(has ? "(?s)(?i)</?":"<",tagNamesPattern(has,tagNames),".*?>");
	}

    private static String tagNamesPattern(boolean has, String... ids){
		if(ids.length>0){
			StringBuilder sb = new StringBuilder("(");
			sb.append(tagNamePattern(has,ids[0]));
			String con = has?"|":")(";
			for(int i = 1 ; i < ids.length; i++){
				sb.append(con).append(tagNamePattern(has,ids[i]));
			}
			sb.append(")");
			return sb.toString();

		}
		return "";
	}

	private static String tagNamePattern(boolean has, String id) {
		return has? id+"\\b" : "?!"+id+"\\b)(?!/"+id+"";
	}

	public static Pattern getCompiledTagsPattern() {
		if(COMPILED_TAGS_PATTERN == null){
			COMPILED_TAGS_PATTERN = Pattern.compile(tagsPattern(true));
		}
		return COMPILED_TAGS_PATTERN;
	}

	public static Pattern getCompiledUrlPattern() {
		if(COMPILED_URL_PATTERN == null){
			COMPILED_URL_PATTERN = Pattern.compile("(?i)\\b(?:(?:https?|ftp|file)://|www\\.|ftp\\.)[-A-Z0-9+&@#/%=~_|$?!:,\\.]*[A-Z0-9+&@#/%=~_|$]");
		}
		return COMPILED_URL_PATTERN;
	}

	public static Pattern getCompiledTagsWithContentPattern(String tagName) {
		if(COMPILED_TAGS_WITH_CONTENT_PATTERN == null){
			COMPILED_TAGS_WITH_CONTENT_PATTERN = Pattern.compile(tagAndContentPattern(tagName));
		}
		return COMPILED_TAGS_WITH_CONTENT_PATTERN;
	}

	public static String javascriptText(String value) {
		String val = value;
		val = NEW_LINE.matcher(val).replaceAll("\\\\n");
		val = CARRIAGE_RETURN.matcher(val).replaceAll("\\\\r");
		val = SINGLE_QUOTE.matcher(val).replaceAll("\\\\\'");
		val = DOUBLE_QUOTE.matcher(val).replaceAll("\\\\\"");
		//val = OPEN_SCRIPT_TAG.matcher(val).replaceAll("&lt;$1$2$3&gt;");
		val = CLOSE_SCRIP_TAG.matcher(val).replaceAll("&lt;/$1>");
		return val;
	}
}