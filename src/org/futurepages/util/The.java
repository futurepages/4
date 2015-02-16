package org.futurepages.util;

import org.apache.commons.lang.math.RandomUtils;
import org.futurepages.core.exception.DefaultExceptionLogger;
import org.futurepages.util.html.HtmlMapChars;
import org.futurepages.util.html.HtmlRegex;
import org.futurepages.util.iterator.string.IterableString;
import org.futurepages.util.iterator.string.MatchedToken;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

/**
 * Esta é "A" Classe
 * @author leandro
 */
public class The {
	
	public static boolean bool(Boolean x) {
		if (x != null) {
			return x.booleanValue();
		}
		return false;
	}

	public static <T> T cloneOf(T fromObj){
		return ReflectionUtil.clone(fromObj);
	}


	public static String implodedArray(String[] array, String delim, String quote) {
		if (quote == null) {
			quote = "";
		}
		StringBuilder out = new StringBuilder("");
		for (int i = 0; i < array.length; i++) {
			if (i != 0) {
				out.append(delim);
			}
			out.append(quote).append(array[i]).append(quote);
		}
		return out.toString();
	}
	
	public static String implodedArray(Object[] array, String attributeName, String delim, String quote) {
		if (quote == null) {
			quote = "";
		}
		StringBuilder out = new StringBuilder("");
		for (int i = 0; i < array.length; i++) {
			if (i != 0) {
				out.append(delim);
			}
			out.append(quote).append(ReflectionUtil.getField(array[i], attributeName)).append(quote);
		}
		return out.toString();
	}

	public static String implodedArray(Object[] array, String delim, String quote) {
		if (quote == null) {
			quote = "";
		}
		StringBuilder out = new StringBuilder("");
		for (int i = 0; i < array.length; i++) {
			if (i != 0) {
				out.append(delim);
			}
			out.append(quote).append(array[i].toString()).append(quote);
		}
		return out.toString();
	}

	public static String implodedArray(long[] array, String delim, String quote) {
		if (quote == null) {
			quote = "";
		}
		StringBuilder out = new StringBuilder("");
		for (int i = 0; i < array.length; i++) {
			if (i != 0) {
				out.append(delim);
			}
			out.append(quote).append(array[i]).append(quote);
		}
		return out.toString();
	}

	/**
	 * Retorna o primeiro token da String
	 */
	public static String firstTokenOf(String str, String separator) {
		if (str != null) {
			return tokenAt(0, str, separator);
		} else {
			return null;
		}
	}

	/**
	 * @return the first token of the string str after splitting by 'separator'.
	 */
	public static String lastTokenOf(String str, String separator) {
		if (str != null) {
			String[] tokens = str.split(separator);
			return tokens[tokens.length - 1];
		} else {
			return null;
		}
	}

	/**
	 * Retorna o token localizado numa determinada posição dentro de uma string com separadoes
	 * caso não encontre, o retorno é null
	 * @param posi é a posição que se deseja... a primeira posiçao é 0
	 * @param str a string que deseja-se pesquisar o token
	 * @param separator o separador dos tokens
	 * @return o token da posiçao 'posi'. nulo caso não exista a posição.
	 */
	public static String tokenAt(int posi, String str, String separator) {
		StringTokenizer st = new StringTokenizer(str, separator);
		int i = 0;
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (i == posi) {
				return token;
			}
			i++;
		}
		return null;
	}

	public static String strWithRightSpaces(String s, int len) {
		// converts integer to left-zero padded string, len  chars long.
		if (s.length() > len) {
			return s.substring(0, len);
		} else if (s.length() < len) // pad on left with zeros
		{
			return s + "                                                        ".substring(0, len - s.length());
		} else {
			return s.substring(0, len);
		}
	}

	public static String strWithLeftSpaces(String s, int len) {
		// converts integer to left-zero padded string, len  chars long.
		if (s.length() > len) {
			return s.substring(0, len);
		} else if (s.length() < len) // pad on left with zeros
		{
			return "                                                                             ".substring(0, len - s.length()) + s;
		} else {
			return s.substring(0, len);
		}
	}

	/**
	 * Retorna o array com os tokens separados pelo 'separator'
	 */
	public static String[] explodedToArray(String str, String separator) {
		StringTokenizer st = new StringTokenizer(str, separator);
		String[] explodedTokens = new String[st.countTokens()];
		int i = 0;
		while (st.hasMoreTokens()) {
			explodedTokens[i] = st.nextToken();
			i++;
		}
		return explodedTokens;
	}

	/**
	 * Retorna a lista com os tokens separados pelo 'separator'
	 */
	public static List<String> explodedToList(String str, String separator) {
		StringTokenizer st = new StringTokenizer(str, separator);
		List<String> explodedTokens = new ArrayList<String>();
		while (st.hasMoreTokens()) {
			explodedTokens.add(st.nextToken());
		}
		return explodedTokens;
	}

	/**
	 * Retorna o número de tokens separados por espaço
	 */
	public static int countTokensIn(String str) {
		StringTokenizer st = new StringTokenizer(str, " ");
		return st.countTokens();
	}

	/**
	 *  Retorna o inteiro 'i' com o número de zeros 'len'
	 */
	public static String intWithLeftZeros(long i, int len) {
		// converts integer to left-zero padded string, len  chars long.
		String s = Long.toString(i);
		if (s.length() > len) {
			return s.substring(0, len);
		} else if (s.length() < len) // pad on left with zeros
		{
			return "000000000000000000000000000000000000000000000000000".substring(0, len - s.length()) + s;
		} else {
			return s;
		}
	}

	public static String strWithLeftZeros(String s, int len) {
		// converts integer to left-zero padded string, len  chars long.
		if (s.length() > len) {
			return s.substring(0, len);
		} else if (s.length() < len) // pad on left with zeros
		{
			return "000000000000000000000000000000000000000000000000000".substring(0, len - s.length()) + s;
		} else {
			return s;
		}
	}

	public static String integerWithoutLeftZeros(String numberWithLeftZeros){
		return ((Integer)Integer.parseInt(numberWithLeftZeros)).toString();
	}

	public static <X> boolean arrayContains(X[] xArray, X obj) {
		for(X x : xArray){
			if(x==obj){
				return true;
			}
		}
		return false;
	}

	/**
	 * Capitaliza a primeira letra da string passada como entrada.
	 * Exemplo: "palavraComposta" --> "PalavraComposta"
	 */
	public static String capitalizedWord(String in) {
		if (in.length() > 1) {
			return (in.substring(0, 1).toUpperCase().concat(in.substring(1)));
		} else {
			return in;
		}
	}

	/**
	 * "des"capitaliza a primeira letra da string passada como entrada.
	 * Exemplo: "PalavraComposta" --> "palavraComposta"
	 */
	public static String uncapitalizedWord(String in) {
		if (in.length() > 1) {
			return (in.substring(0, 1).toLowerCase().concat(in.substring(1)));
		} else {
			return in;
		}
	}

	/**
	 * Retorna a string como o HTMLParser - ou seja: texto com tokens somente entre espaço,
	 * retira '\t', '\n', '\r', '\h' e ' ' sequenciais trocando por um único ' '
	 */
	public static String stringWithNoSpecials(String str) {
		StringTokenizer st = new StringTokenizer(str);
		StringBuilder newStr = new StringBuilder(st.nextToken());
		while (st.hasMoreTokens()) {
			newStr.append(" ").append(st.nextToken());
		}
		return newStr.toString();
	}

	/**
	 * Retorna o valor correspondente de uma String em condificação html;
	 * Exemplo: entrada: "leandro"  --> saída: &quot;leandro&quot;
	 * @return a string com os caracteres especiais convertidos para a codificação HTML
	 */
	public static String htmlSimpleValue(String strIn) {
		return HtmlMapChars.htmlSimpleValue(strIn);
	}

	/**
	 * Mediante um objeto como entrada, retorno o nome do pacote mais interno a que ele
	 * pertence.
	 *
	 * @param obj objeto de entrada
	 * @return o nome do pacote mais interno
	 */
	public static String lastPackageName(Object obj) {
		return lastPackageName(obj.getClass());
	}

	public static String lastPackageName(Class clss) {
		String name = clss.getName();
		String[] explodedName = name.split("\\.");
		return explodedName[explodedName.length - 2];
	}

	public static String firstTokenAfter(String str, String discarding, String separator) {
		String str2 = str.substring(str.indexOf(discarding) + discarding.length());
		return firstTokenOf(str2, separator);
	}

	/**
	 * Retorna o nome da classe mediante o nome do arquivo como entrada.
	 * Ex.: fileName = "ClasseName.class" retorna "ClasseName"
	 * @param fileName nome do arquivo
	 * @return no da classe sem a extensão
	 */
	public static String className(String fileName) {
		StringTokenizer st = new StringTokenizer(fileName, ".");
		return st.nextToken();
	}

	/**
	 * Retornar str sem caracteres especiais.
	 */
	public static String wordIn(String str) {


		str = SEOUtil.replaceSpecialAlphas(str);  //CONVERTE ACENTUADOS E Ç

		String regexPatternChars = "[\\d|A-Z|a-z]";

		return wordInRegex(str, regexPatternChars); //retira caracteres especiais diversos.
	}

	public static String wordWithSpecialsIn(String wIn) {
		String[] specials = new String[]{":", ";", ",", ".", "!", "?", "(", ")", "\\", "/",
			"\"", "'", "%", "#", "{", "}", "[", "]", "º", "ª",
			"<", ">", "´", "`"};

		String strOut = wordWithoutSpecials(specials, wIn, " ").trim();
		if (strOut.startsWith("-")) {
			strOut = strOut.substring(1);
		}
		if (strOut.endsWith("-")) {
			strOut = strOut.substring(0, strOut.length() - 1);
		}
		return strOut;
	}

	/**
	 * Remove caracteres especiais.
	 * @param str somente alfanumericos puros sem acentuaçao, ponto, underline e hifen
	 * @return
	 */
	public static String stringKeyIn(String str) {

		str = SEOUtil.replaceSpecialAlphas(str);  //CONVERTE ACENTUADOS E Ç

		String regexPatternChars = "[\\d|A-Z|a-z|\\.|\\-|_]";

		return wordInRegex(str, regexPatternChars);

	}

	/**
	 *
	 * @param in objeto que deseja-se alterar
	 * @return o mesmo objeto com os campos String em caixa alta
	 */
	public static Object upperCasedBean(Object in) {
		Field[] fields = in.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			if (fields[i].getType() == String.class) {
				String field = (String) ReflectionUtil.getField(in, fields[i].getName());
				if (field != null) {
					ReflectionUtil.setField(in, fields[i].getName(), field.toUpperCase());
				}
			}
		}
		return in;
	}

	public static String wordWithoutSpecials(String[] specials, String wIn, String newChar) {
		for (String special : specials) {
			if (wIn.contains(special)) {
				wIn = wIn.replace(special, newChar);
			}
		}
		return wIn;
	}

	public static String javascriptText(String value) {		
		return HtmlRegex.javascriptText(value);
	}

	static String stringWithoutInitialNumbers(String str) {
		char[] c = str.toCharArray();
		while (c != null && Character.isDigit(c[0])) {
			str = str.substring(1, str.length());
			c = (str.length() > 0 ? str.toCharArray() : null);
		}
		return str;
	}

	public static String sequence(char ch, int len) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++) {
			sb.append(ch);
		}
		return sb.toString();
	}

	public static String strWithoutExtraBlanks(String inputText) {
		inputText = inputText.trim();

		String[] lista = inputText.split(" ");
		inputText = "";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < lista.length; i++) {
			if (lista[i].length() > 0) {
				sb.append(lista[i]);
				if (i < lista.length - 1) {
					sb.append(" ");
				}
			}
		}
		return sb.toString();
	}

	//ajuda: regex que traz letras (com e sem acentos) e números [A-Za-zá-üÁ-Ü0-9]
	public static String wordInRegex(String str, String regexPatternChars) {
		Pattern tagsPattern = Pattern.compile(regexPatternChars);
		IterableString iter = new IterableString(tagsPattern, str);
		StringBuilder sb = new StringBuilder();
		for (MatchedToken token : iter) {
			sb.append(token.getMatched());
		}
		return sb.toString();
	}

	/**
	 * Devolve um plain-text ou um html-text embaralhado com textos randômicos, preservando
	 * existência de URLs (no entanto substituídas) e de tags HTML.
	 * @param str
	 * @return
	 */
	public static String undercoverMsg(String str) {

		str = str.replaceAll("http://", "<HTTP/>");
		str = str.replaceAll("https://", "<HTTP/>");
		str = str.replaceAll("ftp://", "<HTTP/>");
		str = str.replaceAll("www.", "<WWW_DOT/>");

		IterableString iter = new IterableString(HtmlRegex.getCompiledTagsPattern(), str);

		StringBuilder sb = new StringBuilder();
		String end = randomTextOf(str);
		for (MatchedToken token : iter) {
			sb.append(randomTextOf(token.getBefore()));
			sb.append(token.getMatched());
			end = randomTextOf(token.getAfter());
		}
		sb.append(end);

		return sb.toString().replaceAll("<HTTP/>", "http://").replaceAll("<WWW_DOT/>", "www.");
	}


	//A-Z : 65 a 90
	//a-z : 97 a 122
	public static String randomTextOf(String str) {
		StringBuilder sb = new StringBuilder();
		sb.append("");

		int len = str.length();
		for (int i = 0; i < len; i++) {
			char ch = str.charAt(i);
			int posi = str.charAt(i);
			int cx;
			if (posi >= 65 && posi <= 90) {
				cx = (RandomUtils.nextInt(26) + 65);
				sb.append((char) cx);
			} else if (posi >= 97 && posi <= 122) {
				cx = (RandomUtils.nextInt(26) + 97);
				sb.append((char) cx);
			} else if (posi >= 210) {
				cx = (RandomUtils.nextInt(26) + 97);
				sb.append((char) cx);
			} else {
				sb.append(ch);
			}
		}
		return sb.toString();
	}

	public static String strWithoutCharsetBreakers(String str) {
			str = SEOUtil.replaceSpecialAlphas(str);  //substitui acentuados e troca ç por c e retorna todo em in.
			String regexPatternChars = "[\\d|A-Z|a-z|\\s|\\-|\\.]"; //regex para manter somente estes caracteres.
			return The.wordInRegex(str, regexPatternChars); //retira caracteres especiais
	}

	/**
	 * Serialize object...
	 * put on byte[] array
	 *
	 * @return null quando obj é null
	 *
	 */
	public static byte[] bytesFrom(Object obj) {
		byte[] objBytes = null;
		if(obj!=null){
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutput out = null;
			try {
				out = new ObjectOutputStream(bos);
				out.writeObject(obj);
				objBytes = bos.toByteArray();
			}catch(IOException ex){
				DefaultExceptionLogger.getInstance().execute(ex);
			} finally {
				try {
					if (out != null) {
						out.close();
					}
				} catch (IOException ex) {
					DefaultExceptionLogger.getInstance().execute(ex);
				}
				try {
					bos.close();
				} catch (IOException ex) {
					DefaultExceptionLogger.getInstance().execute(ex);
				}
			}
		}
		return objBytes;
	}

	/**
	 * Serialize object...
	 * put on byte[] array
	 *
	 * @return null quando obj é null
	 *
	 */
	public static Object objectFrom(byte[] bytes){
		Object obj = null;
		if(bytes!=null){
			ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			ObjectInput in = null;
			try {
				in = new ObjectInputStream(bis);
				obj = in.readObject();
			} catch (IOException e)
			{
				DefaultExceptionLogger.getInstance().execute(e);
			}
			catch (ClassNotFoundException e)
			{
				DefaultExceptionLogger.getInstance().execute(e);
			}
			finally {
				try {
					bis.close();
					if (in != null) {
						in.close();
					}
				} catch (IOException e) {
					DefaultExceptionLogger.getInstance().execute(e);
				}
			}
		}
		return obj;
	}

	public static String replace(String text, String textToBeReplaced, String replacement) {
		return org.apache.commons.lang.StringUtils.replace(text, textToBeReplaced, replacement);
	}

	public static String concat(String... args) {
		StringBuilder sb = new StringBuilder();
		for (String string : args) {
			sb.append(string);
		}
		return sb.toString();
	}

	public static String concat(Object... args) {
		StringBuilder sb = new StringBuilder();
		for (Object string : args) {
			sb.append(string);
		}
		return sb.toString();
	}

	public static String concatWith(String inserted, String... array) {
		if (inserted == null) {
			return concat(array);
		} else {
			StringBuffer out = new StringBuffer();
			for (int i = 0; i < array.length; i++) {
				if (i != 0) {
					out.append(inserted);
				}
				out.append(array[i]);
			}
			return out.toString();
		}
	}

	public static String truncated(String in, int size) {
		if (in == null || in.length() <= size) {
			return in;
		} else {
			return in.substring(0, size);
		}
	}

	public static String replace(String strIn, Map<Character, String> dirt) {
		if (Is.empty(strIn)) return "";

		StringBuilder outBuffer = new StringBuilder();
		String clean;
		for (Character charDirty : strIn.toCharArray()) {

			if (dirt.containsKey(charDirty)) {
				clean = dirt.get(charDirty);
			} else {
				clean = charDirty.toString();
			}
			outBuffer.append(clean);
		}
		return outBuffer.toString();
	}

	public static String replaceIntoByMap(String strIn, Map<String, String> map) {
		if (Is.empty(strIn)) {
			return "";
		}

		for (String key : map.keySet()) {
			strIn = org.apache.commons.lang.StringUtils.replace(strIn, key, map.get(key));
		}

		return strIn;
	}

	public static String leftPad(String value, int qt, String token) {
		return org.apache.commons.lang.StringUtils.leftPad(value, qt, token);
	}

	public static String rightPad(String value, int qt, String token) {
		return org.apache.commons.lang.StringUtils.rightPad(value, qt, token);
	}
}