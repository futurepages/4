package modules.global.model.entities.enums;

/**
 *
 * @author nayane
 */
public enum NivelEscolaridadeEnum {

	SEM_ESCOLARIDADE	(0, "Sem Escolaridade"),
	FUNDAMENTAL			(1, "Nível Fundamental"),
	MEDIO				(2, "Nível Médio"),
	SUPERIOR			(3, "Nível Superior"),
	POS_GRADUACAO		(4, "Pós-Graduação");

	private int nivel;
	private String rotulo;
	private NivelEscolaridadeEnum(int nivel, String rotulo) {
		this.nivel = nivel;
		this.rotulo = rotulo;
	}

	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	public String getRotulo() {
		return rotulo;
	}

	public void setRotulo(String rotulo) {
		this.rotulo = rotulo;
	}

	public String getId() {
		return this.name();
	}
	public String getDescricao() {
		return this.rotulo;
	}

}
