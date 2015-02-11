package modules.global.model.entities.enums;

import modules.global.model.entities.Contato;

/**
 *
 * @author Severiano Alves
 */
public enum TipoContatoEnum {

	TELEFONEFIXO      ("Telefone Fixo","Ramal","(99)9999-9999"),
	TELEFONEMOVEL     ("Telefone Celular", null,"(99)9999-9999"),
	EMAIL			  ("E-mail", null, null),
	WEBPAGE			  ("Web Page", null, null)

	;

	private String descricao;
	
	private String descricaoValorComplemento;

	private String mascara;

	private TipoContatoEnum(String descricao){
		this.descricao = descricao;
	}

	private TipoContatoEnum(String descricao, String descricaoValorComplemento, String mascara) {
		this.descricao = descricao;
		this.descricaoValorComplemento = descricaoValorComplemento;
		this.mascara = mascara;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getId() {
		return this.name();
	}

	public String getDescricaoCompleta(Contato contato){
		String pessoal = "";
		if(contato.isPessoal()){
			pessoal="Pessoal";
		}
		else{
			pessoal="Profissional";
		}
		return contato.getTipo().getDescricao()+" ("+pessoal+") : "+contato.getValor();

	}

	public String getDescricaoValorComplemento() {
		return descricaoValorComplemento;
	}

	public void setDescricaoValorComplemento(String descricaoValorComplemento) {
		this.descricaoValorComplemento = descricaoValorComplemento;
	}

	public String getMascara() {
		return mascara;
	}

}
