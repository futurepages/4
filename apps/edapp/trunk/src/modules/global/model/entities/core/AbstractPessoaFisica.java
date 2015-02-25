package modules.global.model.entities.core;

import modules.global.model.entities.ContaBancaria;
import modules.global.model.entities.Contato;
import modules.global.model.entities.Endereco;
import modules.global.model.entities.brasil.Cidade;
import modules.global.model.entities.brasil.Estado;
import modules.global.model.entities.enums.EstadoCivilEnum;
import modules.global.model.entities.enums.NivelEscolaridadeEnum;
import modules.global.model.entities.enums.SexoEnum;
import modules.global.model.entities.enums.TipoContatoEnum;
import org.futurepages.util.Is;

import javax.persistence.CascadeType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Severiano Alves
 */
@MappedSuperclass
public abstract class AbstractPessoaFisica implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String nomeCompleto;
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	private Calendar momentoCadastro;
	@Temporal(javax.persistence.TemporalType.DATE)
	private Calendar nascimentoData;
	@ManyToOne
	private Cidade nascimentoCidade;
	@Enumerated(EnumType.STRING)
	private SexoEnum sexo;
	@Enumerated(EnumType.STRING)
	private EstadoCivilEnum estadoCivil;
	@Enumerated(EnumType.STRING)
	private NivelEscolaridadeEnum escolaridade;
	private String filiacaoPai;
	private String filiacaoMae;
	private String filiacaoPaiCPF;
	private String filiacaoMaeCPF;
	private String filiacaoAvoMMaterno;
	private String filiacaoAvoFMaterno;
	private String filiacaoAvoMPaterno;
	private String filiacaoAvoFPaterno;
	@OneToOne(cascade = CascadeType.ALL)
	private Endereco endereco;
	private String rgNumero;
	private String rgOrgaoExpedidor;
	@ManyToOne
	private Estado rgOrgaoExpedidorUF;
	@Temporal(javax.persistence.TemporalType.DATE)
	private Calendar rgDataEmissao;
	private String ctpsNumero;
	private String ctpsSerie;
	private String nitPisPasepNum;
	private String tituloEleitorNumero;
	private String tituloEleitorZona;
	private String tituloEleitorSecao;
	@ManyToOne
	private Cidade tituloEleitorCidade;
	private String docMilitarNumero;
	private String docMilitarSerie;
	private String docMilitarCat;
	private String docMilitarUnidade;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNomeCompleto() {
		return nomeCompleto;
	}

	public void setNomeCompleto(String nomeCompleto) {
		this.nomeCompleto = nomeCorrigido(nomeCompleto);
	}

	public String getRgNumero() {
		return rgNumero;
	}

	public void setRgNumero(String rgNumero) {
		this.rgNumero = rgNumero;
	}

	public String getRgOrgaoExpedidor() {
		return rgOrgaoExpedidor;
	}

	public void setRgOrgaoExpedidor(String rgOrgaoExpedidor) {
		this.rgOrgaoExpedidor = rgOrgaoExpedidor;
	}

	public Estado getRgOrgaoExpedidorUF() {
		return rgOrgaoExpedidorUF;
	}

	public void setRgOrgaoExpedidorUF(Estado rgOrgaoExpedidorUF) {
		this.rgOrgaoExpedidorUF = rgOrgaoExpedidorUF;
	}

	public Calendar getRgDataEmissao() {
		return rgDataEmissao;
	}

	public void setRgDataEmissao(Calendar rgDataEmissao) {
		this.rgDataEmissao = rgDataEmissao;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public SexoEnum getSexo() {
		return sexo;
	}

	public void setSexo(SexoEnum sexo) {
		this.sexo = sexo;
	}

	public String getCtpsNumero() {
		return ctpsNumero;
	}

	public void setCtpsNumero(String ctpsNumero) {
		this.ctpsNumero = ctpsNumero;
	}

	public String getCtpsSerie() {
		return ctpsSerie;
	}

	public void setCtpsSerie(String ctpsSerie) {
		this.ctpsSerie = ctpsSerie;
	}

	public String getDocMilitarCat() {
		return docMilitarCat;
	}

	public void setDocMilitarCat(String docMilitarCat) {
		this.docMilitarCat = docMilitarCat;
	}

	public String getDocMilitarNumero() {
		return docMilitarNumero;
	}

	public void setDocMilitarNumero(String docMilitarNumero) {
		this.docMilitarNumero = docMilitarNumero;
	}

	public String getDocMilitarSerie() {
		return docMilitarSerie;
	}

	public void setDocMilitarSerie(String docMilitarSerie) {
		this.docMilitarSerie = docMilitarSerie;
	}

	public String getDocMilitarUnidade() {
		return docMilitarUnidade;
	}

	public void setDocMilitarUnidade(String docMilitarUnidade) {
		this.docMilitarUnidade = docMilitarUnidade;
	}

	public EstadoCivilEnum getEstadoCivil() {
		return estadoCivil;
	}

	public void setEstadoCivil(EstadoCivilEnum estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	public String getEstadoCivilRotulo() {
		if (this.getSexo() == SexoEnum.M) {
			return this.getEstadoCivil().getRotuloM();
		} else {
			return this.getEstadoCivil().getRotuloF();
		}
	}

	public NivelEscolaridadeEnum getEscolaridade() {
		return escolaridade;
	}

	public void setEscolaridade(NivelEscolaridadeEnum escolaridade) {
		this.escolaridade = escolaridade;
	}

	public String getFiliacaoMae() {
		return filiacaoMae;
	}

	public void setFiliacaoMae(String filiacaoMae) {
		this.filiacaoMae = nomeCorrigido(filiacaoMae);
	}

	public String getFiliacaoPai() {
		return filiacaoPai;
	}

	public void setFiliacaoPai(String filiacaoPai) {
		this.filiacaoPai = nomeCorrigido(filiacaoPai);
	}

	public Calendar getMomentoCadastro() {
		return momentoCadastro;
	}

	public void setMomentoCadastro(Calendar momentoCadastro) {
		this.momentoCadastro = momentoCadastro;
	}

	public Cidade getNascimentoCidade() {
		return nascimentoCidade;
	}

	public void setNascimentoCidade(Cidade nascimentoCidade) {
		this.nascimentoCidade = nascimentoCidade;
	}

	public Calendar getNascimentoData() {
		return nascimentoData;
	}

	public void setNascimentoData(Calendar nascimentoData) {
		this.nascimentoData = nascimentoData;
	}

	public String getNitPisPasepNum() {
		return nitPisPasepNum;
	}

	public void setNitPisPasepNum(String nitPisPasepNum) {
		this.nitPisPasepNum = nitPisPasepNum;
	}
	

	public Cidade getTituloEleitorCidade() {
		return tituloEleitorCidade;
	}

	public void setTituloEleitorCidade(Cidade tituloEleitorCidade) {
		this.tituloEleitorCidade = tituloEleitorCidade;
	}

	public String getTituloEleitorNumero() {
		return tituloEleitorNumero;
	}

	public void setTituloEleitorNumero(String tituloEleitorNumero) {
		this.tituloEleitorNumero = tituloEleitorNumero;
	}

	public String getTituloEleitorSecao() {
		return tituloEleitorSecao;
	}

	public void setTituloEleitorSecao(String tituloEleitorSecao) {
		this.tituloEleitorSecao = tituloEleitorSecao;
	}

	public String getTituloEleitorZona() {
		return tituloEleitorZona;
	}

	public void setTituloEleitorZona(String tituloEleitorZona) {
		this.tituloEleitorZona = tituloEleitorZona;
	}

	public abstract List<Contato> getContatos();

	public abstract List<ContaBancaria> getContasBancarias();

	public abstract String getCpf();

	public String getFiliacaoAvoFMaterno() {
		return filiacaoAvoFMaterno;
	}

	public void setFiliacaoAvoFMaterno(String filiacaoAvoFMaterno) {
		this.filiacaoAvoFMaterno = nomeCorrigido(filiacaoAvoFMaterno);
	}

	public String getFiliacaoAvoFPaterno() {
		return filiacaoAvoFPaterno;
	}

	public void setFiliacaoAvoFPaterno(String filiacaoAvoFPaterno) {
		this.filiacaoAvoFPaterno = nomeCorrigido(filiacaoAvoFPaterno);
	}

	public String getFiliacaoAvoMMaterno() {
		return filiacaoAvoMMaterno;
	}

	public void setFiliacaoAvoMMaterno(String filiacaoAvoMMaterno) {
		this.filiacaoAvoMMaterno = nomeCorrigido(filiacaoAvoMMaterno);
	}

	public String getFiliacaoAvoMPaterno() {
		return filiacaoAvoMPaterno;
	}

	public void setFiliacaoAvoMPaterno(String filiacaoAvoMPaterno) {
		this.filiacaoAvoMPaterno = nomeCorrigido(filiacaoAvoMPaterno);
	}

	public String getFiliacaoMaeCPF() {
		return filiacaoMaeCPF;
	}

	public void setFiliacaoMaeCPF(String filiacaoMaeCPF) {
		this.filiacaoMaeCPF = nomeCorrigido(filiacaoMaeCPF);
	}

	public String getFiliacaoPaiCPF() {
		return filiacaoPaiCPF;
	}

	public void setFiliacaoPaiCPF(String filiacaoPaiCPF) {
		this.filiacaoPaiCPF = nomeCorrigido(filiacaoPaiCPF);
	}

	private String nomeCorrigido(String nome) {
		if (nome != null) {
			nome = nome.trim();
			nome = nome.replaceAll("'", "`"); //ao migrar para UTF8, mudar a crase por aspa própria.
			nome = nome.replaceAll("`", "`"); //ao migrar para UTF8, mudar a crase por aspa própria.
			nome = nome.replaceAll("\"","`");

			//vários espaços se tornam um só.
			nome = nome.replaceAll("\\s"," ");
			nome = nome.replaceAll("  +"," ");

//          @TODO descomentar se descobrir por que estava sendo enviado ? pro banco de dados.
//			nome = nome.replaceAll("'", "’");
//			nome = nome.replaceAll("`", "’");
//			nome = nome.replaceAll("\"", "");
		}
		return nome;
	}

	public List<String> getEmails(){
		return contatosDoTipo(TipoContatoEnum.EMAIL);
	}

	public List<String> contatosDoTipo(TipoContatoEnum tipoContatoEnum) {
		List<Contato> contatos = getContatos();
		List<String> listaContatos = new ArrayList<String>();
		for (Iterator<Contato> it = contatos.iterator(); it.hasNext();) {
			Contato contato = it.next();
			if (contato.getTipo().equals(tipoContatoEnum)) {
				if(!Is.empty(contato.getValor())){
					listaContatos.add(contato.getValor());
				}
			}

		}
		return listaContatos;
	}

	public HashMap<String, String> getMapContatos() {
		HashMap<String, String> mapContatos = new HashMap<String, String>();
		for (Contato contato : this.getContatos()) {
			if (contato != null) {
				if (contato.isPessoal()) {
					mapContatos.put(contato.getTipo().getId().toLowerCase() + "_pessoal", contato.getValor());
					if (!Is.empty(contato.getObs())) {
						mapContatos.put(contato.getTipo().getId().toLowerCase() + "_pessoal_obs", contato.getValor());
					}
				} else {
					mapContatos.put(contato.getTipo().getId().toLowerCase() + "_institucional", contato.getValor());
					if (!Is.empty(contato.getObs())) {
						mapContatos.put(contato.getTipo().getId().toLowerCase() + "_institucional_obs", contato.getValor());
					}
				}
			}

		}
		return mapContatos;

	}

	public boolean isExisteContato(TipoContatoEnum tipo, String contato) {
		for (Contato c : this.getContatos()) {
			if (c.getTipo().equals(tipo) && contato.equals(c.getValor())) {
				return true;
			}
		}
		return false;
	}
}
