package modules.global.model.install;

import java.util.ArrayList;
import java.util.Calendar;
import modules.global.model.entities.Feriado;
import modules.global.model.entities.Periodo;
import modules.global.model.entities.enums.TipoFeriadoEnum;
import org.futurepages.core.install.Installer;
import org.futurepages.core.persistence.Dao;

/**
 *
 * @author Jainilene
 */
public class Feriados extends Installer {

	@Override
	public void execute() throws Exception {
		System.out.println("-> Instalando Feriados");
		instalaFeriadosDataFixa();
		instalaFeriadosDataVariavel();
	}

	public Feriados() {
	}

	// feriado com data inicial igual a data final ;
	public static Feriado criarFeriado(int anoI, int mesI, int diaI, String descricao, boolean dtFixa, TipoFeriadoEnum tipo, boolean visivel, String obs) {
		Calendar calI = Calendar.getInstance();
		calI.set(anoI, mesI - 1, diaI);
		Calendar calF = (Calendar.getInstance());
		calF.set(anoI, mesI - 1, diaI);
		Periodo periodo = new Periodo(calI, calF);
		Dao.getInstance().save(periodo);
		Feriado feriado = new Feriado(descricao, dtFixa, visivel, obs, periodo, tipo);
		feriado.setTipo(tipo);
		return feriado;
	}

	// feriado que não termina no final do dia (ex. cinzas até 12:00)
//	public static Feriado criarFeriado(int anoI, int mesI, int diaI, int horaI, int minI, int horaF, int minF, String descricao, boolean dtFixa, TipoFeriadoEnum tipo, boolean visivel, String obs)
//	{
//		Calendar calI = Calendar.getInstance();
//		calI.set(anoI, mesI-1, diaI, horaI, minI);
//		Calendar calF = (Calendar.getInstance());
//		calF.set(anoI, mesI-1, diaI, horaF, horaF);
//		Periodo periodo = new Periodo(calI, calF);
//		Dao.getInstance().save(periodo);
//		Feriado feriado = new Feriado(descricao, dtFixa, visivel,obs, periodo, tipo);
//		feriado.setTipo(tipo);
//		return feriado;
//	}
	public static Feriado criarFeriado(int anoI, int mesI, int diaI, int horaI, int minI, int anoF, int mesF, int diaF, int horaF, int minF, String descricao, boolean dtFixa, TipoFeriadoEnum tipo, String obs, boolean visivel) {
		Calendar calI = Calendar.getInstance();
		calI.set(anoI, mesI - 1, diaI, horaI, minI);
		Calendar calF = (Calendar.getInstance());
		calF.set(anoF, mesF - 1, diaF, horaF, minF);
		Periodo periodo = new Periodo(calI, calF);
		Dao.getInstance().save(periodo);
		Feriado feriado = new Feriado(descricao, dtFixa, visivel, obs, periodo, tipo);
		feriado.setTipo(tipo);
		return feriado;
	}

	// data final diferente da data inicial  -
	public static Feriado criarFeriado(int anoI, int mesI, int diaI, int anoF, int mesF, int diaF, String descricao, boolean dtFixa, TipoFeriadoEnum tipo, String obs, boolean visivel) {
		Calendar calI = Calendar.getInstance();
		calI.set(anoI, mesI - 1, diaI, 00, 00);
		Calendar calF = (Calendar.getInstance());
		calF.set(anoF, mesF - 1, diaF, 23, 00);
		Periodo periodo = new Periodo(calI, calF);
		Dao.getInstance().save(periodo);
		Feriado feriado = new Feriado(descricao, dtFixa, visivel, obs, periodo, tipo);
		feriado.setTipo(tipo);
		return feriado;
	}

	public static void instalaFeriadosDataFixa() {
		Dao.getInstance().save(criarFeriado(2013,  1, 01, "Confraternização Universal", true, TipoFeriadoEnum.INTERNACIONAL, true, null));
		Dao.getInstance().save(criarFeriado(2013,  4, 21, "Tiradentes", true, TipoFeriadoEnum.NACIONAL, true, null));
		Dao.getInstance().save(criarFeriado(2013,  5, 01, "Dia do Trabalho", true, TipoFeriadoEnum.NACIONAL, true, null));
		Dao.getInstance().save(criarFeriado(2013,  8, 16, "Aniversário de Teresina", true, TipoFeriadoEnum.MUNICIPAL, true, null));
		Dao.getInstance().save(criarFeriado(2013,  9, 07, "Independência do Brasil", true, TipoFeriadoEnum.NACIONAL, true, null));
		Dao.getInstance().save(criarFeriado(2013, 11, 02, "Finados", true, TipoFeriadoEnum.NACIONAL, true, null));
		Dao.getInstance().save(criarFeriado(2013, 11, 15, "Proclamação da República", true, TipoFeriadoEnum.NACIONAL, true, null));
		Dao.getInstance().save(criarFeriado(2013, 10, 19, "Dia do Piauí", true, TipoFeriadoEnum.ESTADUAL, true, null));
		Dao.getInstance().save(criarFeriado(2013, 10, 12, "Nossa Sra Aparecida", true, TipoFeriadoEnum.NACIONAL, true, null));
		Dao.getInstance().save(criarFeriado(2013, 12,  8, "Imaculada Conceição", true, TipoFeriadoEnum.NACIONAL, true, null));
		Dao.getInstance().save(criarFeriado(2013, 12, 25, "Natal", true, TipoFeriadoEnum.NACIONAL, true, null));
	}

	public static void instalaFeriadosDataVariavel() {
//		ArrayList<Feriado> feriados = FeriadoServices.calculaFeriadosMoveis(2013);
//		if (feriados != null) {
//			for (Feriado feriado : feriados) {
//				Dao.getInstance().save(feriado.getPeriodo());
//				Dao.getInstance().save(feriado);
//			}
//		}
	}

	public static ArrayList<Integer> calculaPascoa(int ano) {
		ArrayList<Integer> array = new ArrayList<Integer>();
		int a = ano;
		int c = a / 100;
		int n = a - (19 * (a / 19));
		int k = (c - 17) / 25;
		int i = c - c / 4 - (c - k) / 3 + (19 * n) + 15;
		i = i - 30 * (i / 30);
		i = i - ((i / 28) * (1 - (i / 28)) * (29 / (i + 1)) * ((21 - n) / 11));
		int j = a + a / 4 + i + 2 - c + c / 4;
		j = j - (7 * (j / 7));
		int l = i - j;
		int m = 3 + ((l + 40) / 44);
		int d = l + 28 - (31 * (m / 4));
		array.add(m);
		array.add(d);
		return array;
	}
}
