package org.futurepages.util.brazil;

public class CNPJUtil {
	public static int QUANTIDADE_DIGITOS_CNPJ = 14;

	/**
	 * 78425986003615 --> 78.425.986/0036-15
	 * @param cpfCnpj
	 * @return
	 */
	public static String formata(String cpfCnpj) {
		StringBuffer sb = new StringBuffer(cpfCnpj);
		sb.insert(2, '.');
		sb.insert(6, '.');
		sb.insert(10,'/');
		sb.insert(15,'-');
		return sb.toString();
	}
	/**
	 * Remove máscara
	 * @param cnpj
	 */
	public static String somenteNumeros(String cnpj) {
		cnpj = cnpj.replace(".", "").toString();
		cnpj = cnpj.replace("-", "").toString();
		cnpj = cnpj.replace("/", "").toString();
		return cnpj.toString();
	}
    /**
     * Valida o CNPJ - Cadastro Nacional de Pessoa Jurídica
     * @param str_cnpj
     * @return
     */
    public static boolean validaCNPJ(String str_cnpj) {
        if (str_cnpj.length() != QUANTIDADE_DIGITOS_CNPJ) {
            return false;
        }
        if(str_cnpj.equals("00000000000000")||(str_cnpj.equals(""))){
            return false;
        }
        int soma = 0, aux, dig;
        String cnpj_calc = str_cnpj.substring(0, 12);

        char[] chr_cnpj = str_cnpj.toCharArray();

        /* Primeira parte */
        for (int i = 0; i < 4; i++) {
            if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9) {
                soma += (chr_cnpj[i] - 48) * (6 - (i + 1));
            }
        }
        for (int i = 0; i < 8; i++) {
            if (chr_cnpj[i + 4] - 48 >= 0 && chr_cnpj[i + 4] - 48 <= 9) {
                soma += (chr_cnpj[i + 4] - 48) * (10 - (i + 1));
            }
        }
        dig = 11 - (soma % 11);

        cnpj_calc += (dig == 10 || dig == 11) ? "0" : Integer.toString(dig);

        /* Segunda parte */
        soma = 0;
        for (int i = 0; i < 5; i++) {
            if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9) {
                soma += (chr_cnpj[i] - 48) * (7 - (i + 1));
            }
        }
        for (int i = 0; i < 8; i++) {
            if (chr_cnpj[i + 5] - 48 >= 0 && chr_cnpj[i + 5] - 48 <= 9) {
                soma += (chr_cnpj[i + 5] - 48) * (10 - (i + 1));
            }
        }
        dig = 11 - (soma % 11);
        cnpj_calc += (dig == 10 || dig == 11) ? "0" : Integer.toString(dig);

        return str_cnpj.equals(cnpj_calc);
    }
}
