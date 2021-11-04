package org.futurepages.core.persistence;

import java.io.IOException;
import org.futurepages.core.exception.AppLogger;
import org.futurepages.util.FileUtil;

/**
 * Gerador de Schema. Herde esta classe e coloque dentro do pacote schema
 * do seu módulo caso queira executar antes da instalação alguma rotina sql.
 * Executado após a geração do schema a partir das Entitys que são Table
 * 
 * @author leandro
 */
public abstract class SchemaGenerator {

	public SchemaGenerator() {
		try {
			this.execute();
		} catch (Exception ex) {
			System.out.println("[::schema-generator::] Error exporting... " + this.getClass().getName());
			AppLogger.getInstance().execute(ex);
		}
	}

	protected void executeSQL(String sql) {
			System.out.println("[::schema-generator::] " + sql);
			Dao.getInstance().executeSQL(sql);
	}

	protected void executeSQLFromFile(String path) throws IOException {
		String[] sqls = FileUtil.getStringLines(this.getClass(), path);
		Dao.getInstance().executeSQLs(sqls);
	}

	public abstract void execute() throws Exception;
}
