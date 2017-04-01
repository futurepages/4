package org.futurepages.core;

import org.futurepages.core.config.Apps;
import org.futurepages.core.persistence.Dao;
import org.futurepages.util.FileUtil;

import java.io.File;
import java.io.IOException;

public class DeployMigrations {

	public static void run() {
		File versionLastRun = new File(Apps.get("CLASSES_REAL_PATH")+"/migration/last-run");
		if(versionLastRun.exists()){
			System.out.println("\nDEPLOY-MIGRATION WILL RUN SOMETHING...");
			File versionsMigrationDir = new File(Apps.get("CLASSES_REAL_PATH")+"/migration/versions");
			if(versionsMigrationDir.exists() && versionsMigrationDir.isDirectory()){
				File[] versions = versionsMigrationDir.listFiles();
				if(versions != null){
					for (File version : versions){
						if(version.isFile()){
							if(FileUtil.extensionFormat(version.getName()).equals("sql")){
								System.out.println(" > MIGRATION-SQL: "+version.getName()+"...");
								try {
									Dao.getInstance().executeSQLs(true,FileUtil.getStringLines(version));
								} catch (IOException e) {
									e.printStackTrace();
								}
							}else if(FileUtil.extensionFormat(version.getName()).equals("class")){
								System.out.println(" > MIGRATION-CLASS: "+version.getName()+"...");
								try {
									Dao.getInstance().beginTransaction();
									Class.forName("migration.versions."+version.getName().replace(".class","")).newInstance();
									Dao.getInstance().commitTransaction();
								} catch (Exception e) {
									if(Dao.getInstance().isTransactionActive()){
										Dao.getInstance().rollBackTransaction();
										e.printStackTrace();
									}
								}
							}
						}
					}
					Dao.getInstance().close();
				}
			}
			boolean deleted = versionLastRun.delete();
			System.out.println(versionLastRun.getAbsoluteFile()+" deleted?\n >> "+(deleted?"YES!":"NO!"));
			System.out.println("\n\nDEPLOY-MIGRATION DONE!");
		}

	}
}
