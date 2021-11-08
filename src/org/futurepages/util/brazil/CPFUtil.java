package org.futurepages.util.brazil;

import org.futurepages.util.Is;
import org.futurepages.util.The;

public abstract class CPFUtil {

	public static int QUANTIDADE_DIGITOS_CPF = 11;

	/**
	 * Converte 00351063366 -> 003.510.633-66
	 * @param cpfCnpj entrada bruta
	 */
	public static String formata(String cpfCnpj) {
		StringBuilder sb = new StringBuilder(cpfCnpj);
		sb.insert(3, '.');
		sb.insert(7, '.');
		sb.insert(11, '-');
		return sb.toString();
	}

	/**
	 * Remove máscara
	 * @param cpf
	 */
	public static String somenteNumeros(String cpf) {
		if(cpf!=null){
			cpf = cpf.replace(".", "");
			cpf = cpf.replace("-", "");
			return cpf;
		}else{
			return null;
		}
	}

	/**
	 * Calcula o dígito verificador a partir do número de entrada
	 */
	public static String calcDigVerif(String num) {
		int primDig, segDig;
		int soma = 0, peso = 10;
		for (int i = 0; i < num.length(); i++) {
			soma += Integer.parseInt(num.substring(i, i + 1)) * peso--;
		}

		if (soma % 11 == 0 | soma % 11 == 1) {
			primDig = 0;
		} else {
			primDig = 11 - (soma % 11);
		}

		soma = 0;
		peso = 11;
		for (int i = 0; i < num.length(); i++) {
			soma += Integer.parseInt(num.substring(i, i + 1)) * peso--;
		}

		soma += primDig * 2;
		if (soma % 11 == 0 | soma % 11 == 1) {
			segDig = 0;
		} else {
			segDig = 11 - (soma % 11);
		}

		return The.concat(primDig, segDig);
	}

	/**
	 * Gera um CPF aleatório
	 */
	public static String geraCPF() {
		StringBuilder iniciais = new StringBuilder();
		int numero;
		String cpf;
		do {
			for (int i = 0; i < 9; i++) {
				numero = (int) (Math.random() * 10);
				iniciais.append(numero);
			}
			cpf = iniciais + calcDigVerif(iniciais.toString());
		} while (!validaCPF(cpf));
		return cpf;
	}

	/**
	 * Gera um CPF a partir de um inteiro
	 * ex.: 1 , retornaria 000000001DF onde DF é o dígito verificador gerado.
	 */
	public static String geraCPF(long value) {
		StringBuilder iniciais = new StringBuilder();
		String cpf;
		do {
			iniciais.append(The.strWithLeftZeros(value + "", 9));
			cpf = iniciais + calcDigVerif(iniciais.toString());
		} while (!validaCPF(cpf));
		return cpf;
	}

	/**
	 *
	 * @param cpf entrada com os dígitos do CPF, somente caracteres numéricos
	 * @return verdadeiro se o CPF é válido
	 */
	public static boolean validaCPF(String cpf) {
		if (cpf == null) {
			return false;
		}
		if (cpf.length() != QUANTIDADE_DIGITOS_CPF) {
			return false;
		}
		if (Is.everyCharsLike(cpf, cpf.substring(0, 1))) {
			return false;
		}
		String numDig = cpf.substring(0, 9);

		try {
			Integer.parseInt(numDig);
		} catch (Exception ex) {
			return false;
		}
		return calcDigVerif(numDig).equals(cpf.substring(9, 11));
	}
}
