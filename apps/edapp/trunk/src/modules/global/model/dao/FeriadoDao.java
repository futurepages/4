package modules.global.model.dao;

import java.util.Calendar;
import java.util.List;
import modules.global.model.entities.Feriado;
import modules.global.model.entities.enums.TipoFeriadoEnum;
import org.futurepages.core.persistence.PaginationSlice;
import org.futurepages.core.persistence.Dao;

/**
 *
 * @author Jainilene
 */
public class FeriadoDao extends Dao {

	public static final String DEFAULT_ORDER = concatWithComma(desc("visivel"), asc("descricao"));

	public static PaginationSlice<Feriado> paginateList(int pageNum, int pageSize, String descricao, String tipo, int ano, Calendar inicio, Calendar fim, boolean globalAdmin) {
		String t = "";
		String s = "", r = "", n = "", m = "";
		String[] filtro;
		String where = "";
		String pesqAno = year("periodo.momentoInicial").equalsTo(ano);
		String desc = field("descricao").hasAnyOfWords(descricao);
		String isAdmin = "";
		String intervalo = "";

		if (inicio != null && fim != null) {
			//intervalo = ors(field("periodo.momentoInicial").between(inicio,fim),field("periodo.momentoFinal").between(inicio,fim));
			intervalo = ors(ands(field("periodo.momentoInicial").greaterEqualsThen(inicio), field("periodo.momentoInicial").lowerEqualsThen(fim)), ands(field("periodo.momentoFinal").greaterEqualsThen(inicio), field("periodo.momentoFinal").lowerEqualsThen(fim)));
		}
		if (inicio == null && fim != null) {
			intervalo = (ands(field("periodo.momentoFinal").greaterEqualsThen(inicio), field("periodo.momentoFinal").lowerEqualsThen(fim)));
			//intervalo = ors(field("periodo.momentoInicial").lowerEqualsThen(fim),field("periodo.momentoFinal").lowerEqualsThen(fim));
		}
		if (inicio != null && fim == null) {
			intervalo = ands(field("periodo.momentoInicial").greaterEqualsThen(inicio), field("periodo.momentoInicial").lowerEqualsThen(fim));
			//intervalo = ors(field("periodo.momentoInicial").greaterEqualsThen(inicio), field("periodo.momentoFinal").greaterEqualsThen(inicio));
		}
		if (globalAdmin == false) {
			isAdmin = field("visivel").isTrue();
		}
		if (tipo == null || tipo.isEmpty() || tipo.equals("-Tipos-") || tipo.equals("0")) {
			t = field("tipo").equalsTo(TipoFeriadoEnum.ESTADUAL.getDescricao());
			s = field("tipo").equalsTo(TipoFeriadoEnum.INTERNACIONAL.getDescricao());
			r = field("tipo").equalsTo(TipoFeriadoEnum.RECESSO.getDescricao());
			n = field("tipo").equalsTo(TipoFeriadoEnum.NACIONAL.getDescricao());
			m = field("tipo").equalsTo(TipoFeriadoEnum.MUNICIPAL.getDescricao());

			filtro = new String[]{s, r, n, t, m, intervalo};
			where = ors(filtro);
			if (ano == 0) {
				where = ands(where, desc, isAdmin);
			} else {
				where = ands(where, desc, isAdmin, pesqAno);
			}

		} else {
			if (tipo.equals("ESTADUAL")) {
				t = field("tipo").equalsTo(TipoFeriadoEnum.ESTADUAL.getDescricao());
			} else if (tipo.equals("MUNICIPAL")) {
				t = field("tipo").equalsTo(TipoFeriadoEnum.MUNICIPAL.getDescricao());
			} else if (tipo.equals("NACIONAL")) {
				t = field("tipo").equalsTo(TipoFeriadoEnum.NACIONAL.getDescricao());
			} else if (tipo.equals("INTERNACIONAL")) {
				t = field("tipo").equalsTo(TipoFeriadoEnum.INTERNACIONAL.getDescricao());
			} else if (tipo.equals("RECESSO")) {
				t = field("tipo").equalsTo(TipoFeriadoEnum.RECESSO.getDescricao());
			}
			if (ano == 0) {
				filtro = new String[]{t, desc, isAdmin, intervalo};
				where = ands(filtro);
			} else {
				filtro = new String[]{t, pesqAno, desc, isAdmin, intervalo};
				where = ands(filtro);
			}

		}
		return Dao.getInstance().paginationSlice(pageNum, pageSize, hql(Feriado.class, where, DEFAULT_ORDER));
	}

	public static PaginationSlice<Feriado> paginateListImport(int pageNum, int pageSize, String anoBase, String anoImport) {
		String where = "";
		String pesqAnoBase = year("periodo.momentoInicial").equalsTo(anoBase);
		String pesqAnoImport = year("periodo.momentoInicial").equalsTo(anoImport);
		where = ors(pesqAnoBase, pesqAnoImport);
		return Dao.getInstance().paginationSlice(pageNum, pageSize, hql(Feriado.class, where, DEFAULT_ORDER));
	}

	public static List<Feriado> getByDescricao(String descricao) {
		String descr = field("descricao").equalsTo(descricao);
		return Dao.getInstance().list(hql(Feriado.class, descr));
	}

	public static Feriado getById(int id) {
		return Dao.getInstance().get(Feriado.class, id);
	}

	public static List<Feriado> listAno() {
		String where = field("periodo.momentoInicial").isNotNull();
		return Dao.getInstance().list(hql(Feriado.class, where));
	}

	public static List<Feriado> getFeriadosRelacionados(Feriado feriado) {
		String descricao = field("descricao").equalsTo(feriado.getDescricao());
		String anos = field("periodo.momentoInicial").greaterThen(feriado.getPeriodo().getMomentoInicial());
		String visiveis = field("visivel").isTrue();
		String where = ands(descricao, anos, visiveis);
		return Dao.getInstance().list(hql(Feriado.class, where));
	}

	public static List<Feriado> getVisiveisDataFixaAno(int ano) {
		String where = ands(field("visivel").isTrue(), field("dataFixa").isTrue(), field("periodo.momentoInicial").between(ano + "/1/1", +ano + "12/31"));
		return Dao.getInstance().list(hql(Feriado.class, where));
	}

	public static List<Feriado> getVisiveisDataNaoFixaAno(int ano) {
		String c = ands(field("visivel").isTrue(), field("dataFixa").isFalse(), field("periodo.momentoInicial").between(ano + "/1/1", +ano + "12/31"));
		return Dao.getInstance().list(hql(Feriado.class, c));
	}
}