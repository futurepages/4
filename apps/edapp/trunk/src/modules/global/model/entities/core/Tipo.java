package modules.global.model.entities.core;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

/**
 * Tipo Genérico para tabelas de tipos.
 * @author leandro 
 */
@MappedSuperclass
public abstract class Tipo implements Serializable{
    
    private String descricao;

    private boolean visivel = true;
    
    private boolean apagavel;

	public abstract int getId();

	public boolean isCriavel(){
		return true;
	}
	
    @Override
    public String toString(){
        return this.descricao;
    }

    public Tipo(){}

    public String getDescriminatorValue(){
    	return descricao;
    }
    
    public String getDescriminatorFieldName(){
    	return "descricao";
    }
    
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isVisivel() {
        return visivel;
    }

    public void setVisivel(boolean visivel) {
        this.visivel = visivel;
    }
    
    public void build(String descritor){
    	this.descricao = descritor;
    }
    
	public boolean isApagavel() {
		return apagavel;
	}

	public void setApagavel(boolean apagavel) {
		this.apagavel = apagavel;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((descricao == null) ? 0 : descricao.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tipo other = (Tipo) obj;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		return true;
	}
   
}