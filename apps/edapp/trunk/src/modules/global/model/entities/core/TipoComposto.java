package modules.global.model.entities.core;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class TipoComposto extends Tipo{
	
	public static final String DELIMITADOR = " > ";

	private String caminho;

	@Override
	public String getDescriminatorFieldName() {
		return "caminho";
	}
	
	public String getCaminho() {
		return caminho;
	}

	public void setCaminho(String caminho) {
		this.caminho = caminho;
	}

	@Override
	public String toString() {
		return this.getCaminho();
	}

	protected String[] getCaminhoFatiado() {
		return caminho.split(DELIMITADOR);
	}
	
}
