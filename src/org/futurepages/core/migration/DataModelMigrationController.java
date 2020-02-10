package org.futurepages.core.migration;

import org.futurepages.core.config.Apps;
import org.futurepages.core.exception.AppLogger;
import org.futurepages.core.persistence.Dao;
import org.futurepages.menta.core.control.Controller;
import org.futurepages.util.FileUtil;
import org.futurepages.util.Is;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class DataModelMigrationController {

	private static final String MIGRATION_FILE_NAME_REGEX = "V_(\\d+)_?(\\d)?_?_?[\\w-\\d]*.(sql|class)";

	public static void execute() throws Exception {

		String dataModelClassName = Apps.get("APP_DATA_MODEL_CLASS");
		boolean avoidExecutions = !Apps.get("SCHEMA_GENERATION_TYPE").equals("none");
		if(Is.empty(dataModelClassName)){
			System.out.println("[fpg] Data Model Migration Control is OFF. To turn on, tell us the class path of yout VersionedDataModel instance on the parameter APP_DATA_MODEL_CLASS on app-params.xml");
			return;
		}
		VersionedDataModel appModel;
		try{
			Class dataModelClass = Class.forName(dataModelClassName);
			appModel = (VersionedDataModel) dataModelClass.newInstance();
			if(!appModel.installed()){
				System.out.println("[fpg] DMMC: Your Migration Data Model Control Structure isn't built.");
			}
		}catch (Exception ex){
			System.out.println("[fpg] You didn't define a "+VersionedDataModel.class.getName()+" correctly. APP_DATA_MODEL_CLASS="+Apps.get("APP_DATA_MODEL_CLASS"));
			throw ex;
		}
		String oldVersion = appModel.getVersion();
		System.out.println("[fpg] DMMC: Data Model Migration Control started! Actual (or maybe old) App's Version: "+oldVersion);

		File versionsMigrationDir = new File(Apps.get("CLASSES_REAL_PATH")+"/migration/versions");
		if(!versionsMigrationDir.exists()) {
			System.out.println("[fpg] DMMC didn't find: 'migration/versions' on SRC). Nothing to do.");
		}else{
			ArrayList<File> migrationFiles = new ArrayList<>();
			File[] migrationRoots = versionsMigrationDir.listFiles();
			if(migrationRoots!=null){
				for(File f : migrationRoots){
					if(!f.isDirectory()){
						migrationFiles.add(f);
					}
				}
				if(migrationFiles.size()>1){
					sortByVersion(migrationFiles);
				}
			}

			// SE ESTIVERMOS EM MODO DE DESENVOLVIMENTO, A SUBPASTA "past" É CONSIDERADA.
			File pastVersionsDir = new File(versionsMigrationDir.getAbsoluteFile()+"/past");
			if(Apps.devMode() && pastVersionsDir.exists() && pastVersionsDir.isDirectory() && appModel.getVersionNum() < getVersionNumOf(migrationFiles.get(0).getName())){
				System.out.println("[fpg] DMMC: Loading migrations on 'past' dir ...");
				ArrayList pastFiles = new ArrayList();
				Files.walk(Paths.get(versionsMigrationDir.getAbsolutePath()+"/past")).filter(Files::isRegularFile).forEach(pastFiles::add);
				Object[] olderVersions = pastFiles.toArray();
				File f;
				for(Object oldV : olderVersions){
					f = new File(oldV.toString());
					if(!f.isDirectory()){
						addMigrationFile(migrationFiles, f);
					}
				}
				sortByVersion(migrationFiles);
			}



			StringBuilder logTxt = new StringBuilder();
			String newVersion = null;

			if(avoidExecutions){
				String infoLog = "SCHEMA_GENERATION_TYPE="+Apps.get("SCHEMA_GENERATION_TYPE")+", migrations skipped. To run you need to set to 'none'!";
				System.out.println("[fpg] DMMC: "+infoLog);
				logTxt.append(infoLog).append("\n");
			}

			double appVersionNum = appModel.getVersionNum();
			int success = 0;
			int skipped = 0;
			// O MIGRATION STARTS HERE.........................................................................................
			Exception exCaused = null;
			if(migrationFiles.size()>0){
				System.out.println("[fpg] DMMC: Scanned "+migrationFiles.size()+" element(s) at /migration/versions/*");
				if(getVersionNumOf(migrationFiles.get(migrationFiles.size()-1).getName()) > appVersionNum ){
					System.out.println("[fpg] DMMC: Starting to apply changes");
					for(File versionFile : migrationFiles){
						double fileVersionNum = getVersionNumOf(versionFile.getName());
						if(fileVersionNum > appVersionNum){
							logTxt.append(">> ").append(versionFile.getName());
							try {
								if(avoidExecutions){
									//SKIPPED
									logTxt.append(" [IGNORED]\n");
									skipped++;
								}
								else if(exCaused==null){
									Dao.getInstance().beginTransaction();
									if (versionFile.getName().endsWith(".sql")) {
										//System.out.println("[fpg] DMMC: running " + versionFile.getName());
										Dao.getInstance().executeSQLs(true, FileUtil.getStringLines(versionFile));
									}else {
										//System.out.println("[fpg] DMMC: running migration.versions." + versionFile.getName().replace(".class", ""));
										Class.forName("migration.versions." + versionFile.getName().replace(".class", "")).newInstance();
									}
									Dao.getInstance().commitTransaction();
									newVersion = getVersionStrOf(versionFile.getName());
									logTxt.append(" [OK]\n");
									success++;
								}else{
									//SKIPPED
									logTxt.append(" [HALTED]\n");
									skipped++;
								}
							} catch (Exception e) {
								logTxt.append(" [FAIL]\n");
								skipped++;
								StringWriter errors = new StringWriter();
								e.printStackTrace(new PrintWriter(errors));
								logTxt.append(errors);
								if (Dao.getInstance().isTransactionActive()) {
									Dao.getInstance().rollBackTransaction();
								}
								exCaused = e;
							}
						}
					}
					if(exCaused!=null){
						AppLogger.getInstance().execute(exCaused);
						Controller.makeUnavailable();
						System.out.println("### APPLICATION IS UNAVAILABLE FOR USERS (code 503) ###");
					}
				}
			}
			if(newVersion!=null){
				System.out.println("[fpg] DMMC: New version: "+newVersion+". Executed with Success: "+success+", Skipped because of Fail: "+skipped);
				appModel.addVersion(newVersion, oldVersion, logTxt.toString(), success, skipped);
				System.out.println(logTxt.toString());
			}else{
				if(avoidExecutions){
					newVersion = getVersionStrOf(migrationFiles.get(migrationFiles.size()-1).getName());
					appModel.addVersion(newVersion, oldVersion, logTxt.toString(), success, skipped);
				}else{
					System.out.println("[fpg] DMMC: No changes found! Still on version: "+appModel.getVersion());

					//só registra na produção se: 1) não for 'production', OU 2) sendo deploy, tem build_id ou mandou-se explicitamente logar.
					if(     !Apps.get("DEPLOY_MODE").equals("production")
						 || (!Is.empty(Apps.get("APP_BUILD_ID")) || Apps.get("LOG_RESTART").equals("true"))
					){
						appModel.registerNoChanges(oldVersion,logTxt.toString(), skipped);
					}
				}

				// clean build info.
				if(!Is.empty(Apps.get("APP_BUILD_ID"))){
					HashMap<String,String> contentMap = new HashMap<>();
					contentMap.put("<param.*\\bname=\"APP_BUILD_ID\".*?>","");
					FileUtil.replaceAll(contentMap, Apps.getInstance().getPropertiesFilePath(), true);
				}
			}
		}
		Dao.getInstance().close();
	}

	private static ArrayList sortByVersion(ArrayList list) {
		Collections.sort(list, (Comparator<File>) (f1, f2) -> getVersionNumOf(f1.getName()) > getVersionNumOf(f2.getName())? 1 : -1);
		return list;
	}

	private static void addMigrationFile(ArrayList<File> migrationFiles, File f) {
		if(f.getName().matches(MIGRATION_FILE_NAME_REGEX)){
			migrationFiles.add(f);
		}else if(!f.getParentFile().isDirectory()){
			if(f.getParentFile().getName().equals("res") || f.getParentFile().getName().startsWith("res_")){
				System.out.println("[fpg] DMMC: Migration Res file: "+f.getAbsolutePath());
			}else{
				throw new RuntimeException("[fpg] DMMC: The file '"+f.getName()+"' isn't in the migration pattern we accept. Migration STOPPED!");
			}
		}
	}

	private static Double getVersionNumOf(String name) {
		String valStr = name.replaceAll(MIGRATION_FILE_NAME_REGEX,"$1.$2");
		if(valStr.endsWith(".")){
			valStr = valStr.substring(0,valStr.length()-1);
		}
		return Double.valueOf(valStr);
	}

	private static String getVersionStrOf(String name) {
		String valStr = name.replaceAll(MIGRATION_FILE_NAME_REGEX,"$1_$2");
		if(valStr.endsWith("_")){
			valStr = valStr.substring(0,valStr.length()-1);
		}
		return valStr;
	}
}