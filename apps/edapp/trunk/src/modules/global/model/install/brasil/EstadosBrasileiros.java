package modules.global.model.install.brasil;

import java.util.List;
import modules.global.model.entities.brasil.Cidade;
import modules.global.model.entities.brasil.Estado;
import org.futurepages.core.persistence.Dao;

/**
 *
 * @author leandro
 */
public class EstadosBrasileiros {
	
	CidadesBrasileiras cidadesBrasileiras;

	public EstadosBrasileiros(CidadesBrasileiras cidadesBrasileiras) {
		this.cidadesBrasileiras = cidadesBrasileiras;
	}

    
    public void instalaTodos(){
        persistEstado("AC", "Acre",               "Rio Branco",    "N" , new String[]{"AM", "RO"});
        persistEstado("AL", "Alagoas",            "Maceió",        "NE", new String[]{"PE", "BA", "SE"});
        persistEstado("AP", "Amapá",              "Macapá",        "N" , new String[]{"PA"});
        persistEstado("AM", "Amazonas",           "Manaus",        "N",  new String[]{"AC", "RR", "RO", "MT", "PA"});
        persistEstado("BA", "Bahia",              "Salvador",      "NE", new String[]{"SE", "AL", "PE", "PI", "TO", "GO", "MG", "ES"});
        persistEstado("CE", "Ceará",              "Fortaleza",     "NE", new String[]{"RN", "PB", "PE", "PI"});
        persistEstado("DF", "Distrito Federal",   "Brasília",      "CO", new String[]{"GO"});
        persistEstado("ES", "Espírito Santo",     "Vitória",       "SE", new String[]{"BA", "MG", "RJ"});
        persistEstado("GO", "Goiás",              "Goiânia",       "CO", new String[]{"MS", "MT", "TO", "BA", "MG", "DF"});
        persistEstado("MA", "Maranhão",           "São Luís",      "NE", new String[]{"PA", "TO", "PI"});
        persistEstado("MT", "Mato Grosso",        "Cuiabá",        "CO", new String[]{"RO", "AM", "PA", "TO", "GO", "MS"});
        persistEstado("MS", "Mato Grosso do Sul", "Campo Grande",  "CO", new String[]{"MT", "GO", "MG", "SP", "PR"});
        persistEstado("MG", "Minas Gerais",       "Belo Horizonte","SE", new String[]{"GO", "BA", "ES", "RJ", "SP", "MS"});
        persistEstado("PA", "Pará",               "Belém",         "N",  new String[]{"RR", "AM", "MT", "TO", "MA", "AP"});
        persistEstado("PB", "Paraíba",            "João Pessoa",   "NE", new String[]{"RN", "PE", "CE"});
        persistEstado("PR", "Paraná",             "Curitiba",      "S",  new String[]{"SP", "MS", "SC"});
        persistEstado("PE", "Pernambuco",         "Recife",        "NE", new String[]{"PB", "CE", "PI", "AL", "BA"});
        persistEstado("PI", "Piauí",              "Teresina",      "NE", new String[]{"MA", "TO", "BA", "CE", "PE"});
        persistEstado("RR", "Roraima",            "Boa Vista",     "N",  new String[]{"AM", "PA"});
        persistEstado("RO", "Rondônia",           "Porto Velho",   "N",  new String[]{"AC", "AM", "MT"});
        persistEstado("RJ", "Rio de Janeiro",     "Rio de Janeiro","SE", new String[]{"ES", "MG", "SP"});
        persistEstado("RN", "Rio Grande do Norte","Natal",         "NE", new String[]{"CE", "PB"});
        persistEstado("RS", "Rio Grande do Sul",  "Porto Alegre",  "S",  new String[]{"SC"});
        persistEstado("SC", "Santa Catarina",     "Florianópolis", "S",  new String[]{"PR", "RS"});
        persistEstado("SP", "São Paulo",          "São Paulo",     "SE", new String[]{"MG", "RJ", "MS", "PR"});
        persistEstado("SE", "Sergipe",            "Aracaju",       "NE", new String[]{"AL", "BA"});
        persistEstado("TO", "Tocantins",          "Palmas",        "N",  new String[]{"PA", "MT", "GO", "BA", "PI", "MA"});
    }

	private void persistEstado(String sigla, String nome, String nomeCapital, String regiaoSigla, String[] vizinhos){

		Estado estado = new Estado(sigla , nome , regiaoSigla, vizinhos);
		Dao.getInstance().save(estado);
		List<Cidade> cidades = cidadesBrasileiras.list(sigla);
		for(Cidade cidade : cidades){
				cidade.setEstado(estado);
				if(cidade.getNome().equals(nomeCapital)){
					estado.setCapital(cidade);
				}
				Dao.getInstance().save(cidade);
                cidade.setNomeBusca(cidade.toString());
		}
		Dao.getInstance().update(estado);



	}
}