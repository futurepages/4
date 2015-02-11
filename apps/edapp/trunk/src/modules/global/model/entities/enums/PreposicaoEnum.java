package modules.global.model.entities.enums;

import java.io.Serializable;

public enum PreposicaoEnum implements Serializable{
	
	A("a", "ao", "à"),
	ANTE("ante"),
	APOS("após"),
	ATE("até"),
	COM("com"),
	CONTRA("contra"),
	DE("de", "do", "da"),
	DESDE("desde"), 
	EM("em","no", "na"), 
	ENTRE("entre"), 
	PARA("para"), 
	PERANTE("perante"), 
	POR("por"), 
	SEM("sem"), 
	SOB("sob"), 
	SOBRE("sobre"), 
	TRAS("trás");
	
	private String descricao;
	private String contracaoMasc;
	private String contracaoFemi;

	private PreposicaoEnum(String desc){
		this(desc, desc, desc);
	}

	private PreposicaoEnum(String desc, String contraMasc, String contraFemin){
		this.descricao = desc;
		this.contracaoMasc = contraMasc;
		this.contracaoFemi = contraFemin;
	}
	
	public String getDescricao(){
		return this.descricao;
	}
	
	public String getDescricao(SexoEnum genero){
		if(genero == null){
			return descricao;
		}
		if(SexoEnum.F.equals(genero)){
			return contracaoFemi;
		}
		return contracaoMasc;
	}
	
}
