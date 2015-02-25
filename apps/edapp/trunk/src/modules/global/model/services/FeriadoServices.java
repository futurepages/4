package modules.global.model.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import modules.global.model.entities.Feriado;
import modules.global.model.entities.Periodo;
import modules.global.model.dao.FeriadoDao;
import modules.global.model.entities.enums.TipoFeriadoEnum;
import org.futurepages.core.persistence.Dao;

/**
 *
 * @author Jainilene
 */
public class FeriadoServices {
	/*
	 * Pega os feriados ativos de data fixa do anoBase e importa para o anoImport
	 */

	public static ArrayList<Feriado> importarFeriadosDataFixa(int anoBase, int anoImport) {
		List<Feriado> feriadosFixos = FeriadoDao.getVisiveisDataFixaAno(anoBase);
		List<Feriado> feriadosAnoImport = FeriadoDao.getVisiveisDataFixaAno(anoImport);

		if (!feriadosAnoImport.isEmpty()) {
			for (Feriado feriado : feriadosAnoImport) {
				for (int i = 0; i < feriadosFixos.size(); i++) {
					Feriado ferAA = feriadosFixos.get(i);
					int mesferAA = ferAA.getPeriodo().getMomentoInicial().get(Calendar.MONTH);
					int diaferAA = ferAA.getPeriodo().getMomentoInicial().get(Calendar.DAY_OF_MONTH);
					int mesFeriado = feriado.getPeriodo().getMomentoInicial().get(Calendar.MONTH);
					int diaFeriado = feriado.getPeriodo().getMomentoInicial().get(Calendar.DAY_OF_MONTH);
					if ((mesFeriado == mesferAA && diaFeriado == diaferAA) || feriado.getDescricao().equals(ferAA.getDescricao())) {
						feriadosFixos.remove(feriadosFixos.get(i));
						i = feriadosFixos.size();
					}
				}
			}
		}

		for (int count = 0; count < feriadosFixos.size(); count++) {
			Feriado feri = feriadosFixos.get(count);
			if (!feri.isVisivel()) {
				feriadosFixos.remove(feri);
			}
		}
		importar(feriadosFixos, anoImport, true);
		return (ArrayList<Feriado>) feriadosFixos;
	}

	/*
	 * Pega os feriados ativos de data movel do anoBase e importa para o anoImpor
	 */
	public static ArrayList<Feriado> importarFeriadosDataVariavel(int anoBase, int anoImport) {
		List<Feriado> feriadosMoveis = calculaFeriadosMoveis(anoImport);
		List<Feriado> feriadosMoveisAnoImport = FeriadoDao.getVisiveisDataNaoFixaAno(anoImport);
		List<Feriado> feriadosMoveisAtivosAnoBase = FeriadoDao.getVisiveisDataNaoFixaAno(anoBase);
		List<Feriado> feriadosAImportar = new ArrayList<Feriado>();
		int i;
		if (!feriadosMoveisAnoImport.isEmpty()) {
			for (Feriado feriado : feriadosMoveisAnoImport) {
				for (i = 0; i < feriadosMoveis.size(); i++) {
					Feriado ferAA = feriadosMoveis.get(i);
					int mesferAA = ferAA.getPeriodo().getMomentoInicial().get(Calendar.MONTH);
					int diaferAA = ferAA.getPeriodo().getMomentoInicial().get(Calendar.DAY_OF_MONTH);
					int mesFeriado = feriado.getPeriodo().getMomentoInicial().get(Calendar.MONTH);
					int diaFeriado = feriado.getPeriodo().getMomentoInicial().get(Calendar.DAY_OF_MONTH);
					if ((mesFeriado == mesferAA && diaFeriado == diaferAA) || feriado.getDescricao().equals(ferAA.getDescricao())) {
						feriadosMoveis.remove(feriadosMoveis.get(i));
						i = feriadosMoveis.size();
					}
				}
			}
		}
		if (!feriadosMoveis.isEmpty()) {
			if (!feriadosMoveisAtivosAnoBase.isEmpty()) {
				for (int c = 0; c < feriadosMoveis.size(); c++) {
					Feriado feriMoveis = feriadosMoveis.get(c);
					for (Feriado feriMoveisAnoBase : feriadosMoveisAtivosAnoBase) {
						if (feriMoveis.getDescricao().equals(feriMoveisAnoBase.getDescricao())) {
							feriadosAImportar.add(feriMoveis);
						}
					}
				}
			}
		}

		importar(feriadosAImportar, anoImport, false);
		return (ArrayList<Feriado>) feriadosAImportar;
	}


	/*
	 * Retorna os Feriados de data móvel de um ano(passado por parametro)
	 * Pascoa ; Carnaval ; Cinzas ; Paixão de Cristo ; Corpus Christi ;
	 *
	 */
	public static ArrayList<Feriado> calculaFeriadosMoveis(int ano) {
		ArrayList<Integer> diaMesPascoa = calculaPascoa(ano);

		Calendar pascoa = Calendar.getInstance();
		Calendar carnaval = Calendar.getInstance();
		Calendar cinzas = Calendar.getInstance();
		Calendar paixao = Calendar.getInstance();
		Calendar corpus = Calendar.getInstance();

		pascoa.set(ano, diaMesPascoa.get(0) - 1, diaMesPascoa.get(1));
		carnaval.set(ano, diaMesPascoa.get(0) - 1, diaMesPascoa.get(1));
		cinzas.set(ano, diaMesPascoa.get(0) - 1, diaMesPascoa.get(1));
		paixao.set(ano, diaMesPascoa.get(0) - 1, diaMesPascoa.get(1));
		corpus.set(ano, diaMesPascoa.get(0) - 1, diaMesPascoa.get(1));

		carnaval.add(Calendar.DAY_OF_YEAR, - 47);
		cinzas.add(Calendar.DAY_OF_YEAR, -46);
		paixao.add(Calendar.DAY_OF_YEAR, -2);
		corpus.add(Calendar.DAY_OF_YEAR, +60);
		Periodo perPascoa = new Periodo();
		Periodo perCarnaval = new Periodo();
		Periodo perCinzas = new Periodo();
		Periodo perPaixao = new Periodo();
		Periodo perCorpus = new Periodo();
		Calendar dPas = Calendar.getInstance();
		Calendar dCar = Calendar.getInstance();
		Calendar dCin = Calendar.getInstance();
		Calendar dPai = Calendar.getInstance();
		Calendar dCor = Calendar.getInstance();
		dPas.set(pascoa.get(Calendar.YEAR), pascoa.get(Calendar.MONTH), pascoa.get(Calendar.DAY_OF_MONTH));
		dCar.set(carnaval.get(Calendar.YEAR), carnaval.get(Calendar.MONTH), carnaval.get(Calendar.DAY_OF_MONTH));
		dCin.set(cinzas.get(Calendar.YEAR), cinzas.get(Calendar.MONTH), cinzas.get(Calendar.DAY_OF_MONTH));
		dPai.set(paixao.get(Calendar.YEAR), paixao.get(Calendar.MONTH), paixao.get(Calendar.DAY_OF_MONTH));
		dCor.set(corpus.get(Calendar.YEAR), corpus.get(Calendar.MONTH), corpus.get(Calendar.DAY_OF_MONTH));

		perPascoa.setMomentoInicial(dPas);
		perPascoa.setMomentoFinal(dPas);
		perCarnaval.setMomentoInicial(dCar);
		perCarnaval.setMomentoFinal(dCar);
		perCinzas.setMomentoInicial(dCin);
		perCinzas.setMomentoFinal(dCin);
		perPaixao.setMomentoInicial(dPai);
		perPaixao.setMomentoFinal(dPai);
		perCorpus.setMomentoInicial(dCor);
		perCorpus.setMomentoFinal(dCor);

		Feriado ferPascoa = new Feriado("Pascoa", false, true, "", perPascoa);
		ferPascoa.setTipo(TipoFeriadoEnum.NACIONAL);
		Feriado ferCarnaval = new Feriado("Carnaval", false, true, "", perCarnaval);
		ferCarnaval.setTipo(TipoFeriadoEnum.NACIONAL);
		Feriado ferCinzas = new Feriado("Cinzas", false, true, "", perCinzas);
		ferCinzas.setTipo(TipoFeriadoEnum.NACIONAL);
		Feriado ferPaixao = new Feriado("Paixao de Cristo", false, true, "", perPaixao);
		ferPaixao.setTipo(TipoFeriadoEnum.NACIONAL);
		Feriado ferCorpus = new Feriado("Corpus Christi", false, true, "", perCorpus);
		ferCorpus.setTipo(TipoFeriadoEnum.NACIONAL);
		ArrayList<Feriado> array = new ArrayList<Feriado>();
		array.add(ferPascoa);
		array.add(ferCarnaval);
		array.add(ferCinzas);
		array.add(ferPaixao);
		array.add(ferCorpus);
		return array;

	}

	/*
	 * Retorna o dia e mes da pascoa de um ano (passado por parâmetro)
	 * indice 0 - mes
	 * indice 1 - dia
	 */
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

	protected static void importar(List<Feriado> feriados, int anoImport, boolean dFixa) {
		for (Feriado feri : feriados) {
			int mes = feri.getPeriodo().getMomentoInicial().get(Calendar.MONTH);
			int dia = feri.getPeriodo().getMomentoInicial().get(Calendar.DAY_OF_MONTH);
			//int ano = Calendar.getInstance().get(Calendar.YEAR);
			Calendar calI = Calendar.getInstance();
			calI.set(anoImport, mes, dia);
			Calendar calF = Calendar.getInstance();
			calF.set(anoImport, mes, dia);
			Periodo periodo = new Periodo(calI, calF);
			Dao.getInstance().save(periodo);
			Feriado feriado = new Feriado(feri.getDescricao(), dFixa, true, "", periodo);
			feriado.setTipo(feri.getTipo());
			Dao.getInstance().save(feriado);
		}
	}
}