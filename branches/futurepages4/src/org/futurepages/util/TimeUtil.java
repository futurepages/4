package org.futurepages.util;

/**
 * Rotinas úteis de manipulação de datas String
 */
public class TimeUtil {

	/**
	 * Valida o tempo em hh:mm verificando se é uma hora válida ou se é um minuto válido
	 * caso não seja retorna false
	 */
	public static boolean timeIsValid(String horario) {

		if (horario == null) {
			return false;
		} else if (horario.toString().trim().equals("") || horario.length() != 5) {
			return false;
		} else {

			String separadorDoisPontos = horario.substring(2, 3);
			if (!separadorDoisPontos.equals(":")) {
				return false;
			}
			try {
				int hora = Integer.parseInt(horario.substring(0, 2));
				int minuto = Integer.parseInt(horario.substring(3, 5));
				if (hora < 0 || hora >= 24) {
					return false;
				}
				if (minuto < 0 || minuto > 60) {
					return false;
				}

			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}

	//entrada 30.5 --> saída: "30:30"
	//entrada 1.1 --> saída: "01:06"
	//entrada -20.25 --> saída: "-20:15"
	public static String timeFrom(double dbTime){
		Double horasDouble = Math.floor(Math.abs(dbTime));
		Double minutosDouble = (Math.abs(dbTime) - horasDouble) * 60 ;
	   return The.concat(
						(dbTime<0?"-":""),
						The.intWithLeftZeros(horasDouble.intValue(),2),
						":",
						The.intWithLeftZeros(minutosDouble.intValue(),2)
			  );
	}
}