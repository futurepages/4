package org.futurepages.core.migration;

import org.futurepages.core.config.Apps;
import org.futurepages.core.persistence.Dao;
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

			File pastVersionsDir = new File(versionsMigrationDir.getAbsoluteFile()+"/past");
			if(Apps.devMode() && !Apps.get("DEPLOY_MODE").equals("production") && pastVersionsDir.exists() && pastVersionsDir.isDirectory() && appModel.getVersionNum() < getVersionNumOf(migrationFiles.get(0).getName())){
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
				String infoLog = "SCHEMA_GENERATION_TYPE="+Apps.get("SCHEMA_GENERATION_TYPE")+", migrations will not run unless you put 'none'!";
				System.out.println("[fpg] DMMC: "+infoLog);
				newVersion = getVersionStrOf(migrationFiles.get(migrationFiles.size()-1).getName());
				try{
					appModel.addVersion(newVersion, oldVersion, infoLog, 0,0);
				}catch (Exception ex){
					if(!appModel.installed()){
						System.out.println("[fpg] DMMC: Your metadata for your Migration Data Model Control isn't built. Try to set SCHEMA_GENERATION_TYPE to 'none'");
					}else{
						throw ex;
					}
				}
			}else{
				double appVersionNum = appModel.getVersionNum();
				int success = 0;
				int fail = 0;
				// O MIGRATION STARTS HERE.........................................................................................
				if(migrationFiles.size()>0){
					System.out.println("[fpg] DMMC: Scanned "+migrationFiles.size()+" element(s) at /migration/versions/*");
					if(getVersionNumOf(migrationFiles.get(migrationFiles.size()-1).getName()) > appVersionNum ){
						System.out.println("[fpg] DMMC: Starting to apply changes");
						for(File versionFile : migrationFiles){
							double fileVersionNum = getVersionNumOf(versionFile.getName());
							if(fileVersionNum > appVersionNum){
								logTxt.append(">> ").append(versionFile.getName());
								try {
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
								} catch (Exception e) {
									logTxt.append(" [FAIL]\n");
									fail++;
									StringWriter errors = new StringWriter();
									e.printStackTrace(new PrintWriter(errors));
									logTxt.append(errors);
									if (Dao.getInstance().isTransactionActive()) {
										Dao.getInstance().rollBackTransaction();
									}
								}
							}
						}
					}

				}
				if(newVersion!=null){
					System.out.println("[fpg] DMMC: New version: "+newVersion+". Success: "+success+", Fail: "+fail);
					appModel.addVersion(newVersion, oldVersion, logTxt.toString(), success,fail);
					System.out.println(logTxt.toString());
				}else{
					System.out.println("[fpg] DMMC: No changes found! Still on version: "+appModel.getVersion());
					if(Apps.get("DEPLOY_MODE").equals("production")){
						appModel.registerNoChanges(oldVersion);
					}
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