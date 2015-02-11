package modules.global.model.entities.enums;

import java.io.Serializable;

public enum SexoEnum implements Serializable {

	M("masculino", "o", "um" , "do", "meu"  , "seu", "dele", "lo"),
	F("feminino" , "a", "uma", "da", "minha", "sua", "dela", "la");

	private String rotulo;
	private String artigoDefinido;
	private String artigoIndefinido;
	private String preposicao;
	private String pronomePossessivo1;
	private String pronomePossessivo2;
	private String pronomePossessivo3;
	private String pronomeObliquo;

	private SexoEnum(String rotulo, String artigoDefinido,
					 String artigoIndefinido, String preposicao,
					 String pronomePossessivo1, String pronomePossessivo2, String pronomePossessivo3,
					 String pronomeObliquo){
		this.rotulo = rotulo;
		this.artigoDefinido = artigoDefinido;
		this.artigoIndefinido = artigoIndefinido;
		this.preposicao = preposicao;
		this.pronomePossessivo1 = pronomePossessivo1;
		this.pronomePossessivo2 = pronomePossessivo2;
		this.pronomePossessivo3 = pronomePossessivo3;
		this.pronomeObliquo = pronomeObliquo;
	}

	public String getArtigoDefinido() {
		return artigoDefinido;
	}

	public String getArtigoIndefinido() {
		return artigoIndefinido;
	}

	public String getPreposicao() {
		return preposicao;
	}

	public String getPronomeObliquo() {
		return pronomeObliquo;
	}

	public String getPronomePossessivo1() {
		return pronomePossessivo1;
	}

	public String getPronomePossessivo2() {
		return pronomePossessivo2;
	}

	public String getPronomePossessivo3() {
		return pronomePossessivo3;
	}

	public String getRotulo() {
		return rotulo;
	}

	public String getName() {
		return this.name();
	}
}