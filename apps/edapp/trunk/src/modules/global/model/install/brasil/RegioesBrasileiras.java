package modules.global.model.install.brasil;

import org.futurepages.core.persistence.Dao;
import modules.global.model.entities.brasil.Regiao;

/**
 *
 * @author zezim
 */
public class RegioesBrasileiras {
    
    public static void instalaTodas(){
        Dao.getInstance().save(new Regiao("N" , "Norte"));
        Dao.getInstance().save(new Regiao("NE", "Nordeste"));
        Dao.getInstance().save(new Regiao("S" , "Sul"));
        Dao.getInstance().save(new Regiao("SE", "Sudeste"));
        Dao.getInstance().save(new Regiao("CO", "Centro-Oeste"));
    }
    
}