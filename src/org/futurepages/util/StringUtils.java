package org.futurepages.util;


import java.util.Map;

public class StringUtils {

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

	public static boolean isNotEmpty(String s) {
		return s != null && s.length() > 0;
	}

	public static boolean isEmpty(String s) {
		return s == null || s.length() == 0;
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

	//corrige um nome que possa estar em maiusculo para ficar no formato: Fulano da Silva dos Anzois
	public static String corrigeNomeCompleto(String nome) {
		nome = nome.replaceAll("'", "’"); //ao migrar para UTF8, mudar a crase por aspa própria.
		nome = nome.replaceAll("`", "’"); //ao migrar para UTF8, mudar a crase por aspa própria.
		nome = nome.replaceAll("\"", "");


		String[] nomes = nome.split("[ |\\t]+");
		StringBuilder sb = new StringBuilder();


		for (String palavra : nomes) {
			palavra = palavra.toLowerCase().trim();


			if (palavra.length() == 3 && (palavra.equals("dos") || palavra.equals("das"))) {
				sb.append(palavra);


			} else if (palavra.length() <= 2
					&& (palavra.equals("de")
					|| palavra.equals("da")
					|| palavra.equals("do")
					|| palavra.equals("e"))) {
				sb.append(palavra);


			} else if (palavra.length() == 2 && (palavra.equals("ii"))) {
				sb.append(palavra.toUpperCase());


			} else {
				if (palavra.length() == 1) {
					sb.append(palavra.toUpperCase());


				} else {
					sb.append(The.capitalizedWord(palavra));


				}
			}
			sb.append(" ");


		}
		return sb.toString().trim();

	}
}