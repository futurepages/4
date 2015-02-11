package modules.global.view;

import modules.global.model.entities.brasil.Cidade;

public class CidadeExplore extends Cidade {

	@Override
	public String getNome(){
		return super.getNome();
	}

	public String getEstadoNome(){
		if(super.getEstado()!=null){
			return super.getEstado().getNome();
		}
		return "---";
	}

	public String getPaisNome(){
		if(super.getPais()!=null){
			return super.getPais().getNome();
		}
		return "---";
	}
}
