package org.futurepages.util.brazil;

/**
 *
 * @author celso/fred
 */
public class TituloEleitorUtil {

	public static boolean validarNumero(String numero) {
		int ind1, ind2, limite, soma, digito;
		StringBuilder strDVc = new StringBuilder();
		numero = numero.trim();
		try {
			Long.parseLong(numero);
		} catch (NumberFormatException nfe) {
			return false;
		}
		StringBuilder numeroBuilder = new StringBuilder(numero);
		while (numeroBuilder.length() < 13) {
			numeroBuilder.insert(0, '0');
		}
		numero = numeroBuilder.toString();
		String strSequencial = numero.substring(0, 9);
		String strUF = numero.substring(9, 11);
		String strDV1 = numero.substring(11, 12);
		String strDV2 = numero.substring(12, 13);
		// Verifca se a UF estiver entre os código possíveis, de 1(SP) até 28(ZZ-Exterior)
		if (Integer.parseInt(strUF) > 0 && Integer.parseInt(strUF) < 29) {
			limite = 9;
			// Loop para calcular os 2 dígitos verificadores
			for (ind1 = 1; ind1 <= 2; ind1++) {
				soma = 0;
				// Calcula a soma para submeter ao módulo 11
				for (ind2 = 1; ind2 <= limite; ind2++) {
					soma = soma + Integer.parseInt(strSequencial.substring((ind2 - 1), (ind2 - 1) + 1)) * (limite + 2 - (ind2));
				}
				// Pega o resto da dívisão, o módulo, por 11
				digito = soma % 11;
				// Se a UF for SP ou MG
				if (Integer.parseInt(strUF) == 1 || Integer.parseInt(strUF) == 2) {
					if (digito == 1) {
						digito = 0;
					} else if (digito == 0) {
						digito = 1;
					} else {
						digito = 11 - digito;
					}
				} else {
					// Outros UF e Exterior
					if ((digito == 1) || (digito == 0)) {
						digito = 0;
					} else {
						digito = 11 - digito;
					}
				}
				// Atribui à variável strDVc o dígito calculado
				strDVc.append(digito);
				// Muda o valor de intLimite para o cáculo do segundo dígito
				limite = 3;
				// O cálculo do segundo dígito será sobre o código da UF + primeiro dígito verificador
				strSequencial = strUF + String.valueOf(digito);
			}
		}
		return ((strDV1 + strDV2).equals(strDVc.toString()));
	}
}