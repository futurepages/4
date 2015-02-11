package modules.global.model.entities.enums;

/**
 *
 * @author Jainilene
 */
public enum TipoFeriadoEnum {
		NACIONAL("Nacional"),
		INTERNACIONAL("Internacional"),
		ESTADUAL("Estadual"),
		MUNICIPAL("Municipal"),
		RECESSO("Recesso")
	;

	private String descricao;

	private TipoFeriadoEnum(String descricao)
	{
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}



}
