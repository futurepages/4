package modules.global.model.entities.core;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Tipo Genérico para tabelas de tipos.
 * Acrescenta um identificador 'auto incremento' a {@link Tipo}
 * @author leandro 
 * 
 */
@MappedSuperclass
public abstract class TipoPadrao extends Tipo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}