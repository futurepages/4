package org.futurepages.util.html;

import org.futurepages.util.Is;

import java.util.HashMap;
/**
 * Mapa de caracteres HTLM
 *
 */
public class HtmlMapChars {

    private static final HashMap<Character, String> plainTable    = new HashMap<Character, String>();
    private static final HashMap<String, String> simpleTable    = new HashMap<String, String>();
    private static final HashMap<String, String> charsetSafe = new HashMap<String, String>();
    private static final HashMap<String, String> completeTable  = new HashMap<String, String>();
    private static final HashMap<Character, String> textareaTable  = new HashMap<Character, String>();
    private static final HashMap<String,String> reverseCompleteTable  = new HashMap<String,String>();

    static
    {

		plainTable.put('<'  ,"&lt;");
        plainTable.put('>'  ,"&gt;");
        plainTable.put('\t' ,"&nbsp;"); //  \t
        plainTable.put('\n' ,"<br/>");  //  \n
        plainTable.put('\r' ,"");
		
        //Povoando tabela de textarea
        textareaTable.put('\"' ,"&quot;");
        textareaTable.put('<'  ,"&lt;");
        textareaTable.put('>'  ,"&gt;");
        textareaTable.put('&'  ,"&amp;");
        textareaTable.put('\t' ,"&nbsp;"); //  \t
        textareaTable.put('\n' ,"<br/>");  //  \n
        textareaTable.put('\r' ,"");       //  \r

		//Povoando tabela simples (somente html brakers)
        simpleTable.put("\"" ,"&quot;");
        simpleTable.put("<"  ,"&lt;");
        simpleTable.put(">"  ,"&gt;");
        simpleTable.put("&"  ,"&amp;");
        simpleTable.put("'"  ,"&apos;");
        simpleTable.put("`"  ,"&apos;");

        //povoando tabela com todos os especiais alfabéticos.
        charsetSafe.put("Á","&Aacute;");
        charsetSafe.put("á","&aacute;");
        charsetSafe.put("Â","&Acirc;");
        charsetSafe.put("â","&acirc;");
        charsetSafe.put("À","&Agrave;");
        charsetSafe.put("à","&agrave;");
        charsetSafe.put("Å","&Aring;");
        charsetSafe.put("å","&aring;");
        charsetSafe.put("Ã","&Atilde;");
        charsetSafe.put("ã","&atilde;");
        charsetSafe.put("Ä","&Auml;");
        charsetSafe.put("ä","&auml;");
        charsetSafe.put("Æ","&AElig;");
        charsetSafe.put("æ","&aelig;");
        charsetSafe.put("É","&Eacute;");
        charsetSafe.put("é","&eacute;");
        charsetSafe.put("Ê","&Ecirc;");
        charsetSafe.put("ê","&ecirc;");
        charsetSafe.put("È","&Egrave;");
        charsetSafe.put("è","&egrave;");
        charsetSafe.put("Ë","&Euml;");
        charsetSafe.put("ë","&euml;");
        charsetSafe.put("Ð","&ETH;");
        charsetSafe.put("ð","&eth;");
        charsetSafe.put("Í","&Iacute;");
        charsetSafe.put("í","&iacute;");
        charsetSafe.put("Î","&Icirc;");
        charsetSafe.put("î","&icirc;");
        charsetSafe.put("Ì","&Igrave;");
        charsetSafe.put("ì","&igrave;");
        charsetSafe.put("Ï","&Iuml;");
        charsetSafe.put("ï","&iuml;");
        charsetSafe.put("Ó","&Oacute;");
        charsetSafe.put("ó","&oacute;");
        charsetSafe.put("Ô","&Ocirc;");
        charsetSafe.put("ô","&ocirc;");
        charsetSafe.put("Ò","&Ograve;");
        charsetSafe.put("ò","&ograve;");
        charsetSafe.put("Ø","&Oslash;");
        charsetSafe.put("ø","&oslash;");
        charsetSafe.put("Õ","&Otilde;");
        charsetSafe.put("õ","&otilde;");
        charsetSafe.put("Ö","&Ouml;");
        charsetSafe.put("ö","&ouml;");
        charsetSafe.put("Ú","&Uacute;");
        charsetSafe.put("ú","&uacute;");
        charsetSafe.put("Û","&Ucirc;");
        charsetSafe.put("û","&ucirc;");
        charsetSafe.put("Ù","&Ugrave;");
        charsetSafe.put("ù","&ugrave;");
        charsetSafe.put("Ü","&Uuml;");
        charsetSafe.put("ü","&uuml;");
        charsetSafe.put("Ç","&Ccedil;");
        charsetSafe.put("ç","&ccedil;");
        charsetSafe.put("Ñ","&Ntilde;");
        charsetSafe.put("ñ","&ntilde;");

		charsetSafe.put("º","&ordm;");
		charsetSafe.put("°","&deg;");
		charsetSafe.put("ª","&ordf;");
		charsetSafe.put("§","&sect;");
		charsetSafe.put("©","&copy;");
		charsetSafe.put("®","&reg;");
		charsetSafe.put("™","&trade;");
		charsetSafe.put("€","&euro;");
		charsetSafe.put("£","&pound;");

		charsetSafe.put("“","&ldquo;");
		charsetSafe.put("”","&rdquo;");
		charsetSafe.put("′","&prime;");
		charsetSafe.put("‘","&lsquo;");
		charsetSafe.put("’","&rsquo;");
		charsetSafe.put("″","&Prime;");
		
        completeTable.putAll(simpleTable);
        completeTable.putAll(charsetSafe);

		reverseCompleteTable.put("&nbsp;","	");
		for(String str : completeTable.keySet()){
			reverseCompleteTable.put(completeTable.get(str), str);
		}
    }

    public static String getSimple(char ch){
        return simpleTable.get(String.valueOf(ch));
    }

    public static String getComplete(char ch){
        return completeTable.get(String.valueOf(ch));
    }

    public static String getTextArea(char ch){
        return textareaTable.get(ch);
    }

	public static String removeHtmlSpecials(String str){
		for(String simbol : reverseCompleteTable.keySet()){
			str = str.replaceAll(simbol, reverseCompleteTable.get(simbol));
		}
		return str;
    }

    /**
     * Retorna o valor correspondente de uma String em condificação html;
     * somente para os caracteres mais críticos.
     * Exemplo: entrada: "leandro"  --> saída: (&)quot;leandro(&)quot; (sem os parenteses)
     * contempla somente os HTML Brakers: aspa dupla, menor que e maior que.
     * @return a string com os caracteres especiais críticos convertidos para a codificação HTML
     */
    public static String htmlSimpleValue(String strIn, String excludedChars){
        if(strIn == null) return "";

		StringBuilder outBuffer = new StringBuilder();
        String htmlValue;
        for(int i = 0; i < strIn.length();i++){
            htmlValue = getSimple(strIn.charAt(i));
            if(htmlValue != null && (Is.empty(excludedChars) || !excludedChars.contains(""+strIn.charAt(i)))){
                outBuffer.append(htmlValue);
            }
            else{
                outBuffer.append(strIn.charAt(i));
            }
        }
        return outBuffer.toString();
    }

    public static String charsetSafe(String strIn){
        if(strIn == null) return "";

		StringBuilder outBuffer = new StringBuilder();
        for(int i = 0; i < strIn.length();i++){
            String htmlValue = charsetSafe.get(String.valueOf(strIn.charAt(i)));
            if(htmlValue != null){
                outBuffer.append(htmlValue);
            }
            else{
                outBuffer.append(strIn.charAt(i));
            }
        }
        return outBuffer.toString();
    }

    /**
     * Retorna o valor correspondente de uma String em condificação html;
     * Exemplo: entrada: "leandro"  --> saída: (&)quot;leandro(&)quot; (sem os parenteses)
     * contempla HTML Brakers e caracteres alfabéticos acentuados e especiais.
     * @return a string com os caracteres especiais convertidos para a codificação HTML
     */
    public static String htmlValue(String strIn){
        if(strIn == null) return "&nbsp;";

        StringBuilder outBuffer = new StringBuilder();
        for(int i = 0; i < strIn.length();i++){
            String htmlValue = getComplete(strIn.charAt(i));
            if(htmlValue != null){
                outBuffer.append(htmlValue);
            }
            else{
                outBuffer.append(strIn.charAt(i));
            }
        }
        return outBuffer.toString();
    }

    /**
     * Converte as quebras de texto e aspas escrito em textarea para os caracteres válidos de html.
     * @param strIn
     */
    public static String textAreaValue(String strIn){
        if(strIn == null) return "&nbsp;";

        char[] strInChars = strIn.toCharArray();

        StringBuilder outBuffer = new StringBuilder();
        for(char c : strInChars){
            if(getTextArea(c)==null)
                outBuffer.append(c);
            else
                outBuffer.append(getTextArea(c));
        }
        return outBuffer.toString();
    }
    /**
     * Converte as quebras de texto escrito em textarea para as quebras de html.
     * @param strIn
     */
    public static String plainTextValue(String strIn){
        if(strIn == null) return "&nbsp;";

        char[] strInChars = strIn.toCharArray();

        StringBuilder outBuffer = new StringBuilder();
        for(char c : strInChars){
            if(plainTable.get(c)==null)
                outBuffer.append(c);
            else
                outBuffer.append(plainTable.get(c));
        }
        return outBuffer.toString();
    }

    public static String noHtmlTags(String in){
        return in.replaceAll("<"  ,"&lt;");
    }

}