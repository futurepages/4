package org.futurepages.util.brazil;

import java.util.ArrayList;
import java.util.HashMap;

public class NumberUtil {

	private static final HashMap<String, String> unidades = new HashMap<>();
	private static final HashMap<String, String> centenas = new HashMap<>();
	private static final HashMap<String, String> dezenas = new HashMap<>();
	private static final HashMap<String, String> milhar = new HashMap<>();
	private static final HashMap<String, String> nomeUnidade = new HashMap<>();


	static {
		unidades.put("0", "zero");
		unidades.put("1", "um");
		unidades.put("2", "dois");
		unidades.put("3", "três");
		unidades.put("4", "quatro");
		unidades.put("5", "cinco");
		unidades.put("6", "seis");
		unidades.put("7", "sete");
		unidades.put("8", "oito");
		unidades.put("9", "nove");
		unidades.put("10", "dez");
		unidades.put("11", "onze");
		unidades.put("12", "doze");
		unidades.put("13", "treze");
		unidades.put("14", "quatorze");
		unidades.put("15", "quinze");
		unidades.put("16", "dezesseis");
		unidades.put("17", "dezessete");
		unidades.put("18", "dezoito");
		unidades.put("19", "dezenove");

		dezenas.put("2", "vinte");
		dezenas.put("3", "trinta");
		dezenas.put("4", "quarenta");
		dezenas.put("5", "cinquenta");
		dezenas.put("6", "sessenta");
		dezenas.put("7", "setenta");
		dezenas.put("8", "oitenta");
		dezenas.put("9", "noventa");

		centenas.put("1", "cento");
		centenas.put("2", "duzentos");
		centenas.put("3", "trezentos");
		centenas.put("4", "quatrocentos");
		centenas.put("5", "quinhentos");
		centenas.put("6", "seiscentos");
		centenas.put("7", "setecentos");
		centenas.put("8", "oitocentos");
		centenas.put("9", "novecentos");
		centenas.put("100", "cem");

		milhar.put("18", "mil oitocentos");
		milhar.put("19", "mil novecentos");
		milhar.put("20", "dois mil");

		milhar.put("1800", "mil e oitocentos");
		milhar.put("1900", "mil e novecentos");
		milhar.put("2000", "dois mil");

		nomeUnidade.put("1", "mil");
		nomeUnidade.put("2", "milh");
		nomeUnidade.put("3", "bilh");
		nomeUnidade.put("4", "trilh");
		nomeUnidade.put("5", "quadrilh");
		nomeUnidade.put("6", "quintilh");
		nomeUnidade.put("7", "sextilh");
		nomeUnidade.put("8", "setilh");
		nomeUnidade.put("9", "octilh");
		nomeUnidade.put("10", "nonilh");
		nomeUnidade.put("11", "decilh");
	}

	/**
	 * Converte milhares com 4 caracteres para o formato por extenso.
	 * Obs.: converte somente de 1500 a 2099
	 *
	 * @param number - o número a ser convertido
	 * @return - o número por extenso
	 * @throws Exception
	 */
	public static String milharPorExtenso(String number) throws Exception {
		if (number.length() != 4) {
			throw new Exception(number + " não é um milhar válido.");
		}
		String fstPart = number.substring(0, 2);
		String sndPart = number.substring(2, 4);

		if (sndPart.equals("00")) {
			return milhar.get(number);
		}
		return milhar.get(fstPart) + " e " + dezenaPorExtenso(sndPart);
	}

	/**
	 * Converte um número com dois caracteres no seu formato por extenso
	 *
	 * @param number
	 * @return
	 * @throws Exception
	 */
	public static String dezenaPorExtenso(String number) throws Exception {
		if (number.length() != 2) {
			throw new Exception(number + " não é uma dezena válida.");
		}
		String fstPart = number.substring(0, 1);
		String sndPart = number.substring(1, 2);

		//zero a nove
		if (fstPart.equals("0")) {
			return unidades.get(sndPart);
		} //dez a dezenove
		else if (fstPart.equals("1")) {
			return unidades.get(number);
		}
		//vinte a noventa e nove
		if (sndPart.equals("0")) {
			return dezenas.get(fstPart);
		}
		return dezenas.get(fstPart) + " e " + unidades.get(sndPart);
	}

	public static String partePorExtenso(String number) throws Exception {
		if (number.length() != 3) {
			throw new Exception(number + " não é uma centena válida.");
		}
		String fstPart = number.substring(0, 1);
		String sndPart = number.substring(1, 3);

		//so dezena
		if (fstPart.equals("0")) {
			return dezenaPorExtenso(sndPart);
		} else if (fstPart.equals("1") && dezenaPorExtenso(sndPart).equals("zero")) {
			return centenas.get(number);
		}
		return centenas.get(fstPart) +
				(dezenaPorExtenso(sndPart).equals("zero") ? "" : " e " + dezenaPorExtenso(sndPart));
	}

	public static String nomeUnidades(String number, String grupo, boolean more) {
		if (more && number.equals("zero")) {
			return "";
		}
		if (number.equals("um")) {
			if (grupo.equals("1")) {
				return "mil";
			} else if (!grupo.equals("0")) {
				return number + " " + nomeUnidade.get(grupo) + "ão";
			}
		} else if (!grupo.equals("0")) {
			if (grupo.equals("1")) {
				return number + " mil";
			} else {
				return number + " " + nomeUnidade.get(grupo) + "ões";
			}
		}
		return number;
	}

	public static String numeroPorExteso(int number) {
		try {
			return numeroPorExteso(String.valueOf(number));
		}catch (Exception e){
			throw new RuntimeException(e);
		}
	}
	public static String numeroPorExteso(String number) throws Exception {
		StringBuilder retorno = new StringBuilder();
		String ultimo = "";
		ArrayList<String> ret = new ArrayList<String>();
		StringBuilder numberBuilder = new StringBuilder(number);
		while (numberBuilder.length() % 3 != 0) {
			numberBuilder.insert(0, "0");
		}
		number = numberBuilder.toString();
		for (int i = 0; i < number.length() / 3; i++) {
			String parte = number.substring(number.length() - ((i + 1) * 3), number.length() - ((i + 1) * 3 - 3));
			if (i == 0) {
				ultimo = parte;
			}
			String parc = nomeUnidades(partePorExtenso(parte), String.valueOf(i), number.length() > 3);
			if (!parc.equals("")) {
				ret.add(parc);
			}
		}
		for (int i = 0; i < ret.size(); i++) {
			if (i == 0 && i == ret.size() - 1) {
				retorno = new StringBuilder(ret.get(i));
			} else if (i == 0) {
				if (ret.get(i + 1).endsWith("mil")) {
					int n = Integer.parseInt(ultimo);
					if (n <= 99 || n % 100 == 0) {
						retorno.insert(0, " e " + ret.get(i));
					} else {
						retorno.insert(0, " " + ret.get(i));
					}
				} else {
					retorno.insert(0, " e " + ret.get(i));
				}
			} else if (i == ret.size() - 1) {
				retorno.insert(0, " " + ret.get(i));
			} else {
				retorno.insert(0, ", " + ret.get(i));
			}
		}
		return retorno.toString().trim();
	}


	public static Long strToLong(String number){
		try {
			return Long.parseLong(number);
		}catch(NumberFormatException ex){
			return null;
		}
	}

	public static int onlyPositivesOrZero(int number){
		return Math.max(number, 0);
	}
}