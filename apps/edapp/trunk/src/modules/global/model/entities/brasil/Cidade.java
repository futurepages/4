package modules.global.model.entities.brasil;

import modules.global.model.entities.Pais;
import org.futurepages.core.admin.DefaultUser;
import org.futurepages.core.validation.Validator;
import org.jboss.logging.Field;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * Representa uma cidade brasileira (quando estado é especificado) ou estrangeira (estado não espeficado
 * e país diferente de BRA).
 */
@Entity
//@BeanView(label="Cidade*|$Txt:global.cidade",menu=NONE|PUBLIC|PROTECTED*|ALL_MODULES_FREE, create="...",read="PUBLIC|BY_ROLE|NON_TRIVIAL|*|a,b,c,d,e,f", update="", delete="")
public class Cidade implements Serializable
//		, NonTrivialBeanView
{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

//	@FieldNow(create=true,update=true,delete=true) //default true,true,true
	// private Calendar momentoUltimaAtualizacao;

	@Column(length = 120, nullable = false)
//	@FieldView(label="$Txt:",create=NON_TRIVIAL, update=BY_ROLE, formBreakLine=true,exploreShow=true,exploreSort=true,exploreMainSearch=true,exploreExtraFilters=true)
	private String nome;

	@ManyToOne(optional = true)
//	@RefFieldView(explore=@ReferencedFieldView[]{}, cardinality="LOW|MEDIUM|LARGE" ,show=true, label="$Txt:id",sort=false,update=NEVER|SPECIFIC_ROLE|)
//    @FieldView(label="$Txt:",sort=false,crud...) //nesse tipo de campo, pode ser o de cima ou este aqui que retorna o toString
	private Estado estado;

	@ManyToOne(optional = false)
	private Pais pais;

	/**
	 * Se a cidade for do Brasil, o formato será nome - estado.sigla
	 * Senão, o formato será nome (pais.nome)
	 */
    private String nomeBusca;
	
	@Column(columnDefinition = "boolean default true")
//	@FieldForPseudoDelete
	private boolean ativo;
	
	@Transient
	private String caminhoBandeira;

	public Cidade() {
		
	}

	public Cidade(long id) {
		this.id = id;
		this.ativo = true;
	}

	public Cidade(String nome) {
		this.nome = nome;
		this.pais = new Pais("BRA");
		this.ativo = true;
	}

	public Cidade(String nome, String paisSigla) {
		this.nome = nome;
		this.pais = new Pais(paisSigla);
	}
    public String getNomeBusca() {
		nomeBusca = toString();
        return nomeBusca;
    }

    public void setNomeBusca(String nomeBusca) {
        this.nomeBusca = nomeBusca;
    }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Pais getPais() {
		return pais;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	@Override
	public String toString() {
		if (estado != null) {
			return this.nome + " - " + estado.getSigla();
		} else {
			return this.nome + " (" + this.getPais() + ")";
		}
	}

	@Transient
	public String getNomeExtenso(){
		if (estado != null) {
			return this.nome + ", " + estado.getNome();
		} else {
			return this.nome + " (" + this.getPais() + ")";
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Cidade other = (Cidade) obj;
		if ((this.nome == null) ? (other.nome != null) : !this.nome.equals(other.nome)) {
			return false;
		}
		if (this.estado != other.estado && (this.estado == null || !this.estado.equals(other.estado))) {
			return false;
		}
		if (this.pais != other.pais && (this.pais == null || !this.pais.equals(other.pais))) {
			return false;
		}
		return true;
	}

	
}

interface NonTrivialBeanView {

	// a ideia é CidadeBeanView extends Cidade implements NonTrivialBeanView
	// setWithSuperClass uma nova instancia de Cidade

	//(empty(target)? trying the instance : trying a @FieldView or @ActionView
	//if target is a field, crudType must be NotNull, else must be Null
	// if returns null, call only default getAccess.
	public Boolean getAccess(DefaultUser user, String target, String crudType);

	public NonTrivialBeanView create(Object... args);
	public NonTrivialBeanView read(Object... args);
	public NonTrivialBeanView update(Object... args);
	public NonTrivialBeanView delete(Object... args);

	public NonTrivialBeanView dependencies(String fieldName); //to use when a field depends o

// TODO temos mais a pensar a respeito aqui...
//	public String getIconPath();
//	public boolean validateWithInputs(String Object... arguments); //(empty(target)? trying the instance : trying a @FieldView or @ActionView
//	public Validator getSpecificValidator();
//	public void logIt(NonTrivialBeanView bean);
}