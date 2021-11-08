package org.futurepages.util.brazil;

public class PisPasepNitUtil {

    public static int QUANTIDADE_DIGITOS_PISPASEPNIT = 11;


    /**
     * Valida o PIS/PASEP/NIT
	 *
     * @param str_pispasepnit somente números, sem máscara.
	 *
     * @return true se for PIS, PASEP ou NIT
     */
    public static boolean validaPisPasepNit(String str_pispasepnit) {

		if (str_pispasepnit == null
			|| str_pispasepnit.length() != QUANTIDADE_DIGITOS_PISPASEPNIT
			|| str_pispasepnit.equals("00000000000")
			|| !isNumero(str_pispasepnit)
			) {
			return false;
		}

		String ftap = "3298765432";
		Integer total = 0;
		try {

			for (int i = 1; i <= 10; i++) {
			Integer parte1 = Integer.valueOf(str_pispasepnit.substring(i-1, i));
			Integer parte2 = Integer.valueOf(ftap.substring(i-1, i));
			total += parte1 * parte2;
			}

			Integer resto = (total % 11);

			if ((int) resto != (int) 0) {
			resto = 11 - resto;
			}

			if((int)resto == (int)10 || (int)resto == (int)11){
			String restoStr = String.valueOf(resto);
				resto = Integer.valueOf(restoStr.substring(1, 2));
				}

			Integer digitoVerificador = Integer.valueOf(str_pispasepnit.substring(10, 11));
			if ((int) resto != (int) digitoVerificador) {
			return false;
			}

		} catch (Exception ex) {
			return false;
		}
		return true;

    }

	/**
	 * Valida o PIS/PASEP, se for  NIT não é válido.
	 * @param pisPasep entrada sem máscara. somente números.
	 * 
	 * @return boolean true se for um número PIS/PASEP. NIT é inválido.
	 */
	 public static boolean  validaPisPasep(String pisPasep){
		 
		boolean valido = validaPisPasepNit(pisPasep);
		return (valido && !isNIT(pisPasep));
		
	 }

	/**
	 * Remove máscara
	 * @param pisPasep número com máscara ou sem máscara.
	 */
	public static String semNumeros(String pisPasep) {
		pisPasep = pisPasep.replace(".", "").toString();
		pisPasep = pisPasep.replace("-", "").toString();
		return pisPasep.toString();
	}

	/**
	 *
	 * @param pisPasepNit número pispasep sem máscara
	 * @return pispasepNit formatado na máscara correta.
	 */
	public static String formata(String pisPasepNit) {
		StringBuilder sb = new StringBuilder(pisPasepNit);
		sb.insert(1, '.');
		sb.insert(5, '.');
		sb.insert(9, '.');
		sb.insert(13, '-');
		return sb.toString();
	}

	private static boolean isNumero(String str) {
				return NumberUtil.strToLong(str) != null;
	}

	/**
	 *
	 * @param pisPasepNit entrada somente números, sem máscara.
	 * @return true se for NIT
	 */
	private static boolean isNIT(String pisPasepNit) {
	    int prefixo = Integer.parseInt(pisPasepNit.substring(0, 3));
		return (prefixo >= 109 && prefixo <=119) || (prefixo  == 167 || prefixo == 168 ) || prefixo == 267;
	}

	/**
	 *
	 * @param pisPasepNit entrada somente números sem máscara.
	 *
	 * @return "PIS/PASEP" ou "NIT" ou null.
	 */
	public static String getTipo(String pisPasepNit){

		if (validaPisPasep(pisPasepNit)){
			return "PIS/PASEP";
		}

		if (validaPisPasepNit(pisPasepNit) && isNIT(pisPasepNit)){
			return "NIT";
		}

		return null;
	}
}
