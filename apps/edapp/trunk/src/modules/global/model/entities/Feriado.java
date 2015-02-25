package modules.global.model.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import modules.global.model.entities.brasil.Cidade;
import modules.global.model.entities.enums.TipoFeriadoEnum;

/**
 *
 * @author Jainilene
 */
@Entity
public class Feriado implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String descricao;

	private boolean dataFixa;

	private boolean visivel;        

	private String obs;
    
    @ManyToOne
    private Cidade cidade;

	@OneToOne
	private Periodo periodo;

	@Enumerated (EnumType.STRING)
	private TipoFeriadoEnum tipo;

	public String getObservacao() {
		return obs;
	}

	public void setObservacao(String observacao) {
		this.obs = observacao;
	}

	public boolean isVisivel() {
		return visivel;
	}

	public void setVisivel(boolean visivel) {
		this.visivel = visivel;
	}

	public Feriado(){
		
	}


//	public Feriado(String descricao, Periodo periodo, boolean dataFixa)
//	{
//		this.periodo = periodo;
//		this.descricao = descricao;
//		this.dataFixa = dataFixa;
//	}
    
    public Feriado(String descricao, boolean dataFixa, boolean visivel, String observacao, Periodo periodo, TipoFeriadoEnum tipo, Cidade cidade) {
		this.descricao = descricao;
		this.dataFixa = dataFixa;
		this.visivel = true;
		this.obs = observacao;
		this.periodo = periodo;
        this.cidade = cidade;
		this.tipo = tipo;
	}

	public Feriado(String descricao, boolean dataFixa, boolean visivel, String observacao, Periodo periodo, TipoFeriadoEnum tipo) {
		this.descricao = descricao;
		this.dataFixa = dataFixa;
		this.visivel = true;
		this.obs = observacao;
		this.periodo = periodo;        
		this.tipo = tipo;
	}

	public Feriado(String descricao, boolean dataFixa, boolean visivel, String observacao, Periodo periodo) {
		this.descricao = descricao;
		this.dataFixa = dataFixa;
		this.visivel = true;
		this.obs = observacao;
		this.periodo = periodo;

	}


	public boolean isDataFixa() {
		return dataFixa;
	}

	public void setDataFixa(boolean dataFixa) {
		this.dataFixa = dataFixa;
	}

	public String getDescricao() {
		return descricao;
	}

	public static List<TipoFeriadoEnum> listTipoFeriado()
	{
		List tipO = new ArrayList();
		tipO.addAll(Arrays.asList(TipoFeriadoEnum.values()));
		return tipO;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public TipoFeriadoEnum getTipo() {
		return tipo;
	}

	public void setTipo(TipoFeriadoEnum tipo) {
		this.tipo = tipo;
	}

	public Periodo getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}
    
    public Cidade getCidade() {
		return cidade;
	}
    
    public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}
}
