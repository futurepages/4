package modules.global.model.dao;

import modules.global.model.entities.Orgao;
import modules.global.model.entities.PessoaFisica;
import modules.global.model.entities.TipoOrgao;
import org.futurepages.core.persistence.PaginationSlice;
import org.futurepages.core.persistence.Dao;
import org.futurepages.core.persistence.HQLProvider;
import org.futurepages.util.Is;
import org.futurepages.util.The;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Jorge Rafael
 */
public class OrgaoDao extends HQLProvider {

	public static final String DEFAULT_ORDER = concatWithComma(desc("ativo"), asc("descricao"));

	public static final String ATIVO = whereAtivo(null);

	public static final String PERMITE_LOTACAO = wherePermiteLotacao(null);

	
	public static List<Orgao> listOrgaosGestao(PessoaFisica gestor) {
		return Dao.getInstance().list(hql(Orgao.class,
				ands(ATIVO,
				   ors(
				        field("gestorAtual.id").equalsTo(gestor.getId()),
				        field("gestorSubstitutoAtual.id").equalsTo(gestor.getId()),
						field("gestorExcepcionalAtual.id").equalsTo(gestor.getId())
				   )
				 
				), 
				asc("id")));
	}

	public static Orgao getByCaminho(String caminho) {
		return Dao.getInstance().uniqueResult(hql(Orgao.class, ands(ATIVO, field("caminho").equalsTo(caminho))));
	}

	public static Orgao getOrgaoById(int id) {
		return Dao.getInstance().get(Orgao.class, id);
	}

	public static List<Orgao> listByCaminho(String caminho) {
		return Dao.getInstance().list(hql(Orgao.class, ands(ATIVO, field("caminho").contains(caminho)), asc("descricao")));
	}

	public static List<Orgao> listStartCaminho(String caminho, boolean negate) {
		if (negate) {
			return Dao.getInstance().list(hql(Orgao.class, ands(ATIVO, field("caminho").notStartsWith(caminho)), asc("descricao")));
		}
		return Dao.getInstance().list(hql(Orgao.class, ands(ATIVO, field("caminho").startsWith(caminho)), asc("descricao")));
	}
	
	//Prepara um "OR" buscando na descrição, na descrição abreviada e no caminho
	public static String whereLocalByDescricaoOuCaminho(String descricaoOuCaminho) {
		descricaoOuCaminho = (!Is.empty(descricaoOuCaminho))?descricaoOuCaminho.trim():"";
		String ors = "";
		if (!Is.empty(descricaoOuCaminho)) {
			String[] palavras1 = The.explodedToArray(descricaoOuCaminho, " ");
			String[] palavras2 = The.explodedToArray(descricaoOuCaminho, " ");
			String[] palavras3 = The.explodedToArray(descricaoOuCaminho, " ");
			for (int i = 0; i < palavras1.length; i++) {
				palavras1[i] = "_" + palavras1[i];
				palavras2[i] = "_" + palavras2[i];
				palavras3[i] = "_" + palavras3[i];
			}

			palavras1[0] = palavras1[0].substring(1); // Primeira palavra sem o underline inicial
			palavras2[0] = concat(" ", palavras1[0]);
			palavras3[0] = concat("(", palavras1[0]);
			
			ors = ors(
					field("caminho").startsWith(descricaoOuCaminho),
					field("caminho").hasAllWordsInSameSequence(palavras2),
					field("caminho").hasAllWordsInSameSequence(palavras3),
					field("descricao").hasAllWordsInSameSequence(palavras1).replaceFirst("%", ""), // A primeira palavra deve estar no início
					field("descricao").hasAllWordsInSameSequence(palavras2),
					field("descricao").hasAllWordsInSameSequence(palavras3),
					field("descricaoAbreviada").hasAllWordsInSameSequence(palavras1).replaceFirst("%", ""), // A primeira palavra deve estar no início
					field("descricaoAbreviada").hasAllWordsInSameSequence(palavras2),
					field("descricaoAbreviada").hasAllWordsInSameSequence(palavras3)
				);
		} 
		return ors.replace("\\_", "_");
	}

	public static PaginationSlice<Orgao> paginationSlice(int pageNum, int pageSize, int idCidade,String campoBusca, int idTipoOrgao, boolean permiteLotacao) {
	
		String[] ors = construirAclausulaOrs(campoBusca,idTipoOrgao);
		
        String where = ors(ors);
	
		if(Is.selected(idCidade)){
			where = ands(where, field("predioSede.endereco.cidade.id").equalsTo(idCidade));
		}
		
		if (permiteLotacao) {
			where = ands(where, field("permiteLotacao").isTrue());
		}

		String order = concatWithComma(desc("permiteLotacao"), desc("ativo"), asc("descricao"));
		PaginationSlice<Orgao> orgaoPaginationSlice =  Dao.getInstance().paginationSlice(pageNum, pageSize, hql(Orgao.class, where, order));
		return orgaoPaginationSlice;
	}

	
	public static Map<Integer, String> mapDefaultByCaminhoOuDescricao(String caminhoOuDescricao) {
		Map<Integer, String> map = new LinkedHashMap<Integer, String>();
		List<Orgao> unidades = Dao.getInstance().list(hql(Orgao.class,
				ands(
						ATIVO,
						ors(field("descricao").contains(caminhoOuDescricao),
								field("descricaoAbreviada").contains(caminhoOuDescricao),
								field("caminho").contains(caminhoOuDescricao))
				),
				asc("descricao")));
		for (Orgao o : unidades) {
			map.put(o.getId(), o.getCaminho());
		}
		return map;
	}
	
	public static Orgao getOrgaoByCodigoETipo(String codigo, TipoOrgao tipoOrgao){
		return Dao.getInstance().uniqueResult(hql(Orgao.class, ands(field("codigo").equalsTo(codigo), field("tipo.caminho").equalsTo(tipoOrgao.getCaminho()))));
	}

	public static long quantidadeOrgaosPorTipoOrgao(int idTipoOrgao){
		return Dao.getInstance().numRows(hql(Orgao.class, field("tipo.id").equalsTo(idTipoOrgao)));
	}
	
	
	public static Orgao getOrgaoDiferenteByCaminho(Orgao orgao){
		String caminho = orgao.getCaminho();
		int idOrgao = orgao.getId();		
		String[] clausulas = {};
		if (caminho == null) {
			caminho = orgao.geraCaminho();
		}
		if (caminho!= null && !caminho.equals("")){
			clausulas  = new  String[2];
			clausulas[0] = field("caminho").equalsTo(caminho);
			clausulas[1] = field("id").differentFrom(idOrgao);
		}
		
		String where = ands(clausulas);
		
		List<Orgao> list = Dao.getInstance().list(hql(Orgao.class, where));
		if (list.size()> 0){
			return list.get(0);
		}else return null;
		
	}	
	
	public static List<Orgao> listAllFilhos(Orgao orgaoPai){
		return Dao.getInstance().list(hql(Orgao.class,field("unidadeSuperior.id").equalsTo(orgaoPai.getId())));
	}
	

	public static List<Orgao> listFilhos(Orgao orgaoPai){
		return listFilhos(orgaoPai, false);
	}
	public static List<Orgao> listFilhosImediatosLotaveis(Orgao orgaoPai){
		return Dao.getInstance().list(hql(Orgao.class,
						ands(
							ATIVO,
							field("unidadeSuperior.id").equalsTo(orgaoPai.getId()),
							PERMITE_LOTACAO
						)
						,
						asc("caminho")
				));
	}

	public static List<Orgao> listFilhos(Orgao orgaoPai, boolean somenteUnidadesLotacao){
		if(!somenteUnidadesLotacao){
		return Dao.getInstance().list(hql(Orgao.class,
						ands(
							ATIVO,
							field("unidadeSuperior.id").equalsTo(orgaoPai.getId())
						)
						,
						asc("caminho")
				));
		}else{
			return Dao.getInstance().list(hql(Orgao.class,ands(
						ATIVO,
						field("caminho").startsWith(orgaoPai.getCaminho()+" > "),
						PERMITE_LOTACAO
					),
					asc("caminho")));
		}
	}

	private static String[] construirAclausulaOrs(String campoBusca, int idTipoOrgao) {
		
		String caminho = field("caminho").contains(campoBusca);
		String descricao = field("descricao").contains(campoBusca);
		
		if(Is.selected(idTipoOrgao)){
			TipoOrgao tipoOrgao = TipoOrgaoDao.getTipoOrgaoById(idTipoOrgao);
			String equalsToIdTipoOrgao = field("tipo.id").equalsTo(idTipoOrgao);
			String caminhoPorTipoDeOrgao = field("tipo.caminho").contains(tipoOrgao.getCaminho());
			String descricaoPorTipoDeOrgao = field("tipo.descricao").contains(tipoOrgao.getCaminho());
			return colocarCondicoes(caminho,descricao,caminhoPorTipoDeOrgao,descricaoPorTipoDeOrgao,equalsToIdTipoOrgao);
		}else{
			return colocarCondicoes(caminho,descricao);
		}
		
	}
	
	private static String[] colocarCondicoes(String...condicoes){
		return condicoes;
	}

	public static List<Orgao> listByTipoDescricao(String tipoDescricao) {
		return listFilhosByTipoDescricao(null, tipoDescricao);
	}

	public static List<Orgao> listFilhosByTipoDescricao(Orgao orgaoPai, String tipoDescricao) {
		return Dao.getInstance().list(hql(Orgao.class, ands(  ATIVO,
											(orgaoPai!=null? field("caminho").startsWith(orgaoPai.getCaminho()+" > ") : null),
											field("tipo.descricao").equalsTo(tipoDescricao)
										)
						, asc("descricao")));

	}
	
	public static List<Orgao> listFilhosComContatos() {
		return Dao.getInstance().list(hql(Orgao.class, ands(field("unidadeSuperior.id").isNotNull(),field("contatos.id").isNotNull())));
	}

	public static String whereAtivo(String alias) {
		alias = !Is.empty(alias)? alias+".": "";
		return field(alias+"ativo").isTrue();
	}

	public static String wherePermiteLotacao(String alias) {
		alias = !Is.empty(alias)? alias+".": "";
		return ors(
					field(alias+"permiteLotacao").isTrue(),
					ands(
						field(alias+"permiteLotacao").isNull(),
						field(alias+"tipo.permiteLotacao").isTrue()
					)
		);
	}

	public static String whereFieldEhOMesmoOuSubordinado(String path, Orgao orgao) {
		return ors(
				field(path,"id").equalsTo(orgao.getId()),
				field(path,"caminho").startsWith(orgao.getCaminho() + " > ")
		);

	}

	public static String whereFieldNaoEhOMesmoNemSubordinado(String path, Orgao orgao) {
		return ands(
				field(path,"id").differentFrom(orgao.getId()),
				field(path,"caminho").notStartsWith(orgao.getCaminho() +  " > ")
		);
	}
}