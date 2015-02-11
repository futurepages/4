package modules.global.model.entities.enums;

/**
 *
 * @author Severiano Alves
 */
public enum EstadoCivilEnum {

	SOLTEIRO     ("Solteiro(a)"     , "Solteiro"    , "Solteira"  ),
	UNIAO_ESTAVEL("União Estável"   , ""            , ""          ),
	CASADO		 ("Casado(a)"       , "Casado"      , "Casada"    ),
	SEPARADO	 ("Separado(a)"     , "Separado"    , "Separada"  ),
	DIVORCIADO	 ("Divorciado(a)"   , "Divorciado"  , "Divorciada"),
	VIUVO		 ("Viúvo(a)"        , "Viúvo"       , "Viúva"     )
	;

	private String descricao;
	private String rotuloM;
	private String rotuloF;

	private EstadoCivilEnum(String descricao, String rotuloM, String rotuloF) {
		this.descricao = descricao;
		this.rotuloM = rotuloM;
		this.rotuloF = rotuloF;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getId() {
		return this.name();
	}

	public String getRotuloF() {
		return rotuloF;
	}

	public String getRotuloM() {
		return rotuloM;
	}

}
