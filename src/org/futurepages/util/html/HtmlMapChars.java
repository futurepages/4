package org.futurepages.util.html;

import java.util.HashMap;
/**
 * Mapa de caracteres HTLM
 *
 */
public class HtmlMapChars {

    private static final HashMap<Character, String> plainTable    = new HashMap<Character, String>();
    private static final HashMap<String, String> simpleTable    = new HashMap<String, String>();
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

        //povoando tabela com todos os especiais alfabéticos.
        completeTable.putAll(simpleTable);
        completeTable.put("Á","&Aacute;");
        completeTable.put("á","&aacute;");
        completeTable.put("Â","&Acirc;");
        completeTable.put("â","&acirc;");
        completeTable.put("À","&Agrave;");
        completeTable.put("à","&agrave;");
        completeTable.put("Å","&Aring;");
        completeTable.put("å","&aring;");
        completeTable.put("Ã","&Atilde;");
        completeTable.put("ã","&atilde;");
        completeTable.put("Ä","&Auml;");
        completeTable.put("ä","&auml;");
        completeTable.put("Æ","&AElig;");
        completeTable.put("æ","&aelig;");
        completeTable.put("É","&Eacute;");
        completeTable.put("é","&eacute;");
        completeTable.put("Ê","&Ecirc;");
        completeTable.put("ê","&ecirc;");
        completeTable.put("È","&Egrave;");
        completeTable.put("è","&egrave;");
        completeTable.put("Ë","&Euml;");
        completeTable.put("ë","&euml;");
        completeTable.put("Ð","&ETH;");
        completeTable.put("ð","&eth;");
        completeTable.put("Í","&Iacute;");
        completeTable.put("í","&iacute;");
        completeTable.put("Î","&Icirc;");
        completeTable.put("î","&icirc;");
        completeTable.put("Ì","&Igrave;");
        completeTable.put("ì","&igrave;");
        completeTable.put("Ï","&Iuml;");
        completeTable.put("ï","&iuml;");
        completeTable.put("Ó","&Oacute;");
        completeTable.put("ó","&oacute;");
        completeTable.put("Ô","&Ocirc;");
        completeTable.put("ô","&ocirc;");
        completeTable.put("Ò","&Ograve;");
        completeTable.put("ò","&ograve;");
        completeTable.put("Ø","&Oslash;");
        completeTable.put("ø","&oslash;");
        completeTable.put("Õ","&Otilde;");
        completeTable.put("õ","&otilde;");
        completeTable.put("Ö","&Ouml;");
        completeTable.put("ö","&ouml;");
        completeTable.put("Ú","&Uacute;");
        completeTable.put("ú","&uacute;");
        completeTable.put("Û","&Ucirc;");
        completeTable.put("û","&ucirc;");
        completeTable.put("Ù","&Ugrave;");
        completeTable.put("ù","&ugrave;");
        completeTable.put("Ü","&Uuml;");
        completeTable.put("ü","&uuml;");
        completeTable.put("Ç","&Ccedil;");
        completeTable.put("ç","&ccedil;");
        completeTable.put("Ñ","&Ntilde;");
        completeTable.put("ñ","&ntilde;");

		completeTable.put("º","&ordm;");
		completeTable.put("°","&deg;");
		completeTable.put("ª","&ordf;");
		completeTable.put("§","&sect;");
		completeTable.put("©","&copy;");
		completeTable.put("®","&reg;");
		completeTable.put("™","&trade;");
		completeTable.put("€","&euro;");
		completeTable.put("£","&pound;");

		completeTable.put("“","&ldquo;");
		completeTable.put("”","&rdquo;");
		completeTable.put("′","&prime;");
		completeTable.put("‘","&lsquo;");
		completeTable.put("’","&rsquo;");
		completeTable.put("″","&Prime;");

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
    public static String htmlSimpleValue(String strIn){
        if(strIn == null) return "&nbsp;";

		StringBuilder outBuffer = new StringBuilder();
        for(int i = 0; i < strIn.length();i++){
            String htmlValue = getSimple(strIn.charAt(i));
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