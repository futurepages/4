package modules.global.model.entities;

import org.futurepages.util.Is;
import org.futurepages.util.The;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.io.Serializable;

/**
 *
 * @author Severiano Alves
 */
@Entity
public class ContaBancaria  implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@OneToOne
	private Banco banco;

	private String op;

	private String cc;

	private String bancoAgencia;

	private boolean ativa;
	
	public ContaBancaria() {
	}

	@Override
	public String toString() {
		if(this.getBanco()!=null){
			return The.concat("C/C: ", this.getCc(), ", Agência: ", this.getBancoAgencia(), ", ", (!Is.empty(this.getOp()) ? "OP: " + Integer.parseInt(this.getOp()) + ", " : ""), this.getBanco().toString());
		}else{
			return "";
		}
	}

	public Banco getBanco() {
		return banco;
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
	}

	public String getBancoAgencia() {
		return bancoAgencia;
	}

	public void setBancoAgencia(String bancoAgencia) {
		this.bancoAgencia = bancoAgencia;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public boolean isAtiva() {
		return ativa;
	}

	public void setAtiva(boolean ativa) {
		this.ativa = ativa;
	}

	public boolean equals(ContaBancaria other){
		if(other!=null && this.toString().equals(other.toString())){
			return true;
		}
		return false;
	}
}
