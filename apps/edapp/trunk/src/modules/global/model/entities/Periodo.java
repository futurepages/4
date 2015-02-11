package modules.global.model.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Calendar;

/**
 *
 * @author Jainilene
 */
@Entity
public class Periodo implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Temporal(TemporalType.DATE)
	private Calendar momentoInicial;
	@Temporal(TemporalType.DATE)
	private Calendar momentoFinal;
    
    @Transient
    private String diaSemanaMomentoInicial;
    
    @Transient
    private String diaSemanaMomentoFinal;
    
	public Periodo() {
	}

	public Periodo(Calendar dataI, Calendar dataF)
	{
		this.momentoInicial = dataI;
		this.momentoFinal = dataF;
	}

	public Calendar getMomentoFinal() {
		return momentoFinal;
	}

	public void setMomentoFinal(Calendar momentoFinal) {
		this.momentoFinal = momentoFinal;        
	}

	public Calendar getMomentoInicial() {
		return momentoInicial;
	}

	public void setMomentoInicial(Calendar momentoInicial) {
		this.momentoInicial = momentoInicial;
	}
    
    
    public String getDiaSemanaMomentoInicial() {
		return diaSemanaMomentoInicial;
	}

	public void setDiaSemanaMomentoInicial(String diaSemanaMomentoInicial) {
		this.diaSemanaMomentoInicial = diaSemanaMomentoInicial;        
	}
    
    
    public String getDiaSemanaMomentoFinal() {
		return diaSemanaMomentoFinal;
	}

	public void setDiaSemanaMomentoFinal(String diaSemanaMomentoFinal) {
		this.diaSemanaMomentoFinal = diaSemanaMomentoFinal;
	}
    
	
	public String toString()
	{
		String texto = this.momentoInicial.get(Calendar.DAY_OF_MONTH) + "/" + this.momentoInicial.get(Calendar.MONTH) + "/" + this.momentoInicial.get(Calendar.YEAR);
		texto += " a " + this.momentoFinal.get(Calendar.DAY_OF_MONTH) + "/" + this.momentoFinal.get(Calendar.MONTH) + "/" + this.momentoFinal.get(Calendar.YEAR);
		return texto;
	}   
}
