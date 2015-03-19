package modules.global.model.install.brasil;

import modules.global.model.entities.brasil.Cidade;
//import modules.global.model.dao.PaisDao;
import org.futurepages.core.persistence.Dao;

/**
 *
 * @author leandro
 */
public class CidadesEstrangeiras {
    
    public static void instalaAlgumas(){
		//Argentina
        persistCidadeEstrangeira("Buenos Aires", "ARG");

		//Japão
        persistCidadeEstrangeira("Tóquio",   "JPN");

		persistCidadeEstrangeira("Barcelona","ESP");

		//Portugal
		persistCidadeEstrangeira("Lisboa",   "PRT");
		persistCidadeEstrangeira("Porto",    "PRT");

		//Itália
        persistCidadeEstrangeira("Roma",     "ITA");
		persistCidadeEstrangeira("Milão",    "ITA");

		//Estados Unidos
        persistCidadeEstrangeira("Nova Iorque",  "USA");
        persistCidadeEstrangeira("Los Angeles",  "USA");
        persistCidadeEstrangeira("Chicago",      "USA");
        persistCidadeEstrangeira("Miame",        "USA");
	}

	private static void persistCidadeEstrangeira(String nome, String siglaPais) {
		Cidade cidade = new Cidade(nome, siglaPais);
		Dao.getInstance().save(cidade);
//        cidade.setPais(PaisDao.getBySigla(cidade.getPais().getSigla()));
        cidade.setNomeBusca(cidade.toString());
	}
}