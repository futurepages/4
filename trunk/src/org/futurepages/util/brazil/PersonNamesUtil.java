package org.futurepages.util.brazil;

import org.futurepages.util.The;

public class PersonNamesUtil {

		public static String correctFullName(String nome) {
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
