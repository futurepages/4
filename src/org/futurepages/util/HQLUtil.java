package org.futurepages.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * HQL - Hibernate Query Language:
 * Utilidades para manipulação de queries HQL
 * 
 * @author leandro
 */
public class HQLUtil {

	/**
	 * retorno = "field like '%word1%word2%'
	 */
	public static String fieldHasAllWordsInSameSequence(String field, String... words) {
		StringBuffer sb = new StringBuffer();
		if (words != null && words.length != 0) {
			sb.append(field + " LIKE '");
			for (int i = 0; i < words.length; i++) {
				sb.append("%" + escLike(words[i]));
			}
			sb.append("%'");
		}
		return sb.toString();
	}

	/**
	 * Exemplo, sendo field = 'name' e logicalConect = 'AND':
	 * retorno = "field like '%word1%' AND field like '%word2%'
	 */
	public static String fieldHasWords(String field, String[] words, String logicalConect) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < words.length; i++) {
			sb.append(The.concat(field, " LIKE '%", escLike(words[i]), "%'", needed(i, words, " " + logicalConect + " ")));
		}
		return sb.toString();
	}

	/**
	 * Quebra 'value', exemplo: "Nome Sobrenome" e retorna:
	 * "field like '%Nome%Sobrenome%'"
	 */
	public static String fieldHasAllWordsInSameSequence(String field, String value) {
		StringTokenizer in = new StringTokenizer(value, " ");
		StringBuffer out = new StringBuffer();
		out.append(field + " LIKE '");
		while (in.hasMoreTokens()) {
			out.append("%" + escLike(in.nextToken()));
		}
		out.append("%'");

		return out.toString();
	}

	public static String imploded(Collection elements) {
		StringBuilder out = new StringBuilder("");
		String virgula = "";
		for (Object element : elements) {
			out.append(virgula);
			if(element instanceof Integer || element instanceof Long){
				out.append(escQuotesAndSlashes(element.toString()));
			}else{
				out.append("'").append(escQuotesAndSlashes(element.toString())).append("'");
			}
			if (virgula.equals("")) {
				virgula = ",";
			}
		}
		return out.toString();
	}

	public static String imploded(Enum[] elements) {
		StringBuilder out = new StringBuilder("");
		String virgula = "";
		for (Enum element : elements) {
			out.append(virgula);
			out.append("'").append(escQuotesAndSlashes(element.name())).append("'");
			if (virgula.equals("")) {
				virgula = ",";
			}
		}
		return out.toString();
	}

	public static String imploded(String tokensStr) {
		return imploded(The.explodedToArray(tokensStr, " "));
	}

	private static String needed(int position, String[] list, String string) {
		if (position < list.length - 1) {
			return string;
		} else {
			return "";
		}
	}

	/**
	 * Escape de HQL/SQL para evitar Injections em campos LIKE
	 *
	 * @param original HQL de entrada
	 * @return
	 */
	public static String escLike(String original) {
		original = escQuotesAndSlashes(original);
		original = original.replace("%", "\\%").replace("_", "\\_");
		return original;
	}

	/**
	 * Escape simple cote '
	 * Escape de HQL/SQL para evitar Injections
	 * @param original HQL de entrada
	 */
	public static String escQuotesAndSlashes(String original) {
		original = original.replace("'", "''").replace("\\", "\\\\");
		return original;
	}

	public static String imploded(String[] array) {
		StringBuffer out = new StringBuffer("");
		for (int i = 0; i < array.length; i++) {
			if (i != 0) {
				out.append(",");
			}
			out.append("'" + escQuotesAndSlashes(array[i]) + "'");
		}
		return out.toString();
	}

	public static String imploded(int[] array) {
		StringBuffer out = new StringBuffer("");
		for (int i = 0; i < array.length; i++) {
			if (i != 0) {
				out.append(",");
			}
			out.append(array[i]);
		}
		return out.toString();
	}

	public static String imploded(long[] array) {
		StringBuffer out = new StringBuffer("");
		for (int i = 0; i < array.length; i++) {
			if (i != 0) {
				out.append(",");
			}
			out.append(array[i]);
		}
		return out.toString();
	}


	public static String matches(String field, String value) {
		return matches(field, value, true, false);
	}

	/**
	 * Implementa uma busca inteligente parecida com o padrão google.
	 * 
	 *<ui>
	 * <li>- Busca sentenças que estiverem entre aspas.
	 * <li>- Desconsidera palavras precedidas do sinal MENOS (-)
	 * <li>- Desconsidera palavras com até dois caracteres.
	 *</ui>
	 *
	 * Exemplo com field = "campo" e o seguinte value:
	 * maria da silva "pereira" a "costinha da silva" -caramba pereira oi pi
	 * Temos o seguinte resultado:
	 *   campo LIKE '%pereira%'
	 *   AND campo LIKE '%silva%'
	 *   AND campo LIKE '%maria%'
	 *   AND campo LIKE '%costinha da silva%'
	 *   AND campo NOT LIKE '%caramba%'
	 *   AND (campo like '% oi %' OR campo LIKE '% oi>%' OR ...) -- ISTO SOMENTE SE findSmaller == true
	 *
	 *  SE bringAll = false --> se a busca ficar vazia, ele não retorna nada.
	 */
	public static String matches(String field, String value, boolean bringAll, boolean findSmaller) {
		value = escLike(value);
		value = value.replaceAll("\\*[\\*]*", "*");
		final int MIN_TOKEN_SIZE = 2;
		Pattern aspasPattern = Pattern.compile("-?\".*?\"");
		Matcher aspasMatcher = aspasPattern.matcher(value);
		String[] conteudoSemAspas = aspasPattern.split(value);

		HashSet<String> tokensLike = new LinkedHashSet<String>();
		HashSet<String> tokensNotLike = new LinkedHashSet<String>();
		HashSet<String> tokensRegexP = new LinkedHashSet<String>();

		//## PARTE 1 - Lista os caras ...

		while (aspasMatcher.find()) { //palavras entre aspas
			int flagStart = (value.charAt(aspasMatcher.start())=='-') ? 2 : 1;
			String foundOne = value.substring(aspasMatcher.start() + flagStart, aspasMatcher.end() - 1);
			if (!foundOne.trim().equals("") && foundOne.trim().length() > MIN_TOKEN_SIZE) {
				if (foundOne.contains("*")) {
					foundOne = foundOne.replaceAll("[\\s]?\\*[\\s]?", "%");
				}
				if(flagStart==1){
					tokensLike.add(foundOne);
				}else{
					tokensNotLike.add(foundOne);
				}
			}
		}

		for (String token : conteudoSemAspas) {
			StringTokenizer st = new StringTokenizer(token);
			while (st.hasMoreTokens()) {
				String palavra = st.nextToken().trim();
				if (palavra.charAt(0) == '-') {
					palavra = palavra.substring(1);
					if (palavra.length() > MIN_TOKEN_SIZE) {
						tokensNotLike.add(palavra);
					}
				} else {
					if (!palavra.equals("") && (palavra.length() > MIN_TOKEN_SIZE)) {
						tokensLike.add(palavra);
					} else {
						if (findSmaller && palavra.length() == MIN_TOKEN_SIZE
							&& !palavra.equalsIgnoreCase("de")
							&& !palavra.equalsIgnoreCase("do")
							&& !palavra.equalsIgnoreCase("da")
							&& !palavra.equalsIgnoreCase("ao")
						) {
							tokensRegexP.add(palavra);
						}
					}
				}
			}
		}

		//## PARTE 2 - Monta SQL com os caras listados
		StringBuilder hqlQueryBuffer = new StringBuilder();

		boolean primeiro = true;
		for (String token : tokensLike) {
			hqlQueryBuffer.append((!primeiro ? " AND " : ""))
						  .append(field)
						  .append(" LIKE '%")
						  .append(token)
						  .append("%'");
			primeiro = false;
		}

		for (String token : tokensNotLike) {
			hqlQueryBuffer.append((!primeiro ? " AND " : ""))
						  .append(field)
						  .append(" NOT LIKE '%")
						  .append(token)
						  .append("%'");
			primeiro = false;
		}

    	if (tokensRegexP.size() > 0) {
			boolean sohEle = primeiro;
			if(!sohEle){
				hqlQueryBuffer.append("AND (");
			}
			boolean primeiroAqui = true;
			for (String token : tokensRegexP) {
				String[] strs = new String[]{" "+token+" " , " "+token+"-" , "-"+token+" " , "-"+token+"-", 
											 " "+token+"?" , " "+token+"!" , " "+token+"." , " "+token+"," ,
											 " "+token+"\\n"  , " "+token+"\\r" , " "+token+"\\t",">"+token+"<" ,
											 ">"+token+" " , " "+token+"<" , " "+token+")" , "("+token+" " ,
											 "\""+token+"\"" , "("+token+")","''"+token+"''"," "+token+"''", "''"+token+" "};

				for(String str : strs){
					hqlQueryBuffer.append((!primeiroAqui ? " OR " : ""))
								  .append(field)
								  .append(" LIKE '%")
								  .append(str)
								  .append("%'");

					primeiroAqui = false;
				}
			}
			if(!sohEle){
				hqlQueryBuffer.append(")");
			}
		}
		if(tokensLike.size() == 0
		&& tokensNotLike.size() == 0
		&& tokensRegexP.size() == 0 && !bringAll){
			return "true = false"; //return nothing
		}
		return hqlQueryBuffer.toString();
	}
}
