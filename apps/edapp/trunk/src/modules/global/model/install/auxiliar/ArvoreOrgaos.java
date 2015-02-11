package modules.global.model.install.auxiliar;

import modules.global.model.entities.Orgao;
import modules.global.model.entities.TipoOrgao;
import org.futurepages.core.persistence.Dao;

/**
 *
 * @author Jorge Rafael
 */
public class ArvoreOrgaos {
	private NoOrgao raiz = null;

	public ArvoreOrgaos() {
		
	}

	public ArvoreOrgaos(NoOrgao raiz) {
		this.raiz = raiz;
	}

	public NoOrgao getRaiz() {
		return raiz;
	}

	public void setRaiz(NoOrgao raiz) {
		this.raiz = raiz;
	}

	public void imprime(boolean mostraCodigo){
		String str = new String("");
		if(raiz != null){
			setNiveis(raiz, 0);
			imprime(raiz, str, mostraCodigo);
		}
	}

	private void imprime(NoOrgao no, String str, boolean mostraCodigo){
		StringBuilder sb = new StringBuilder(str);
		if (sb.length() > 0){
			if (sb.charAt(sb.length()-1) != '|') {
				sb.replace(sb.length()-1, sb.length(), "|");
			}
		}
		System.out.println(sb+"__ "+((mostraCodigo)? (no.getOrgao().getId() + " - ") : "")+no);
		if(no.getFilhos() != null){
			for (int i = 0; i < no.getFilhos().size(); i++){
				NoOrgao n = no.getFilhos().get(i);
				String ch = "";
				if(no.getFilhos().size() > 1) {
					if(i+1 == no.getFilhos().size()){
						ch = " ";
					} else {
						ch = "|";
					}
				}else  {
					ch = " ";
				}
				imprime(n, str +"   "+ch, mostraCodigo);
			}
		}
	}

	private void setNiveis(NoOrgao no, int nivel){
		no.setNivel(nivel);
		if(no.getFilhos() != null){
			for (NoOrgao n:no.getFilhos()) {
				setNiveis(n, nivel+1);
			}
		}
	}

	public ArvoreOrgaos persist(){
		for(NoOrgao no: raiz.getFilhos()){
			no.setPai(null);
			persist(no);
		}
		return this;
	}

	private void persist(NoOrgao no){
		Orgao orgao = no.getOrgao();
		orgao.setUnidadeSuperior((no.getPai()!= null) ? no.getPai().getOrgao() : null);
		String caminho = orgao.geraCaminho();
//		System.out.println(caminho	+ "           ######## ("+orgao.getDescricao()+") - A: "+orgao.isPossuiAutonomia()+" - " + " GC: "+orgao.getTipo().isGrupoConceitual());
		Dao.getInstance().save(orgao);
		if(no.getFilhos() != null){
			for (NoOrgao n : no.getFilhos()) {
				persist(n);
			}
		}
	}

	@Override
	public String toString() {
		return raiz.toString();
	}

	public static NoOrgao noComAutonomia(String descricao, String sigla, TipoOrgao to, Boolean permiteLotacao){
		return new NoOrgao(descricao, sigla, to, true, permiteLotacao);
	}

	public static NoOrgao no(String descricao, String sigla, TipoOrgao to, boolean autonomia, Boolean permiteLotacao){
		return new NoOrgao(descricao, sigla, to, autonomia, permiteLotacao);
	}

	public static ArvoreOrgaos arvore(String str){
		return new ArvoreOrgaos(new NoOrgao(str));
	}

	public static ArvoreOrgaos arvore(NoOrgao no){
		return new ArvoreOrgaos(no);
	}
}