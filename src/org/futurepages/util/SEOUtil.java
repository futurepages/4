package org.futurepages.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Utilidades para Search Engine Optimization
 * @author leandro
 * @changes Danilo
 */
public class SEOUtil {

    private static final Map<Character, String> urlDirt    = new HashMap<Character, String>();
	private static final Map<Character, String> alphabetics    = new HashMap<Character, String>();
	private static final Map<Character, String> specials    = new HashMap<Character, String>();

    static{
    	String empty =  "";
        specials.put('#',"_");
        specials.put('-',"_");
        specials.put(' ',"-");
        specials.put('@',"_");
        specials.put('º', empty);
        specials.put('!', empty);
        specials.put('?',  empty);
        specials.put('.', empty);
        specials.put(',', empty);
        specials.put(':', empty);
        specials.put(';', empty);
        specials.put('(', empty);
        specials.put(')', empty);
        specials.put('\'', empty);
        specials.put('"', empty);
        specials.put('\\', empty);
        specials.put('/', "-");
        specials.put('<', empty);
        specials.put('>', empty);
        specials.put('\t',empty);
        specials.put('\n',empty);
        specials.put('\r',empty);
        specials.put('%',empty);
        specials.put('Ø',empty);
        specials.put('ø',empty);
        specials.put('Ð',empty);
        specials.put('ð',empty);
        specials.put('Æ',empty);
        specials.put('æ',empty);
        
        alphabetics.put('á',"a");
        alphabetics.put('â',"a");
        alphabetics.put('à',"a");
        alphabetics.put('å',"a");
        alphabetics.put('ã',"a");
        alphabetics.put('ä',"a");
        alphabetics.put('é',"e");
        alphabetics.put('ê',"e");
        alphabetics.put('è',"e");
        alphabetics.put('&',"e");
        alphabetics.put('ë',"e");
        alphabetics.put('í',"i");
        alphabetics.put('î',"i");
        alphabetics.put('ì',"i");
        alphabetics.put('ï',"i");
        alphabetics.put('ó',"o");
        alphabetics.put('ô',"o");
        alphabetics.put('ò',"o");
        alphabetics.put('õ',"o");
        alphabetics.put('ö',"o");
        alphabetics.put('ú',"u");
        alphabetics.put('û',"u");
        alphabetics.put('ù',"u");
        alphabetics.put('ü',"u");
        alphabetics.put('Ç',"c");
        alphabetics.put('ç',"c");
        alphabetics.put('ñ',"n");
		
		urlDirt.putAll(specials);
		urlDirt.putAll(alphabetics);
		
    }
    
    public static String get(Character ch){
        return  urlDirt.get(ch).toString();
    }

    /**
     * Devolve a string informada sem acentuacao e pontuacao
     */
	public static String replaceSpecialAlphas(String strIn){
        strIn = strIn.toLowerCase().trim();
        return The.replace(strIn, alphabetics);
    }

	
	/**
	 * Devolve a string informada sem acentuacao, pontuacao e sem caracteres especiais
	 */
	public static String urlFormat(String strIn){
		strIn = strIn.toLowerCase().trim();
		return The.replace(strIn, urlDirt);
	}

	public static String noAccent(String in){
		StringBuffer out = new StringBuffer();
		char[] chars = in.toCharArray();
		for(char ch : chars){
			String newChar = alphabetics.get(ch);
			if(newChar!=null){
				out.append(newChar);
			}else {
				out.append(ch);
			}
		}
		return out.toString();
	}
}