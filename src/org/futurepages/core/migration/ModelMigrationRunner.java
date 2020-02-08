package org.futurepages.core.migration;

import org.futurepages.core.config.Apps;
import org.futurepages.core.persistence.Dao;
import org.futurepages.util.FileUtil;
import org.futurepages.util.Is;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ModelMigrationRunner {

	private static final String MIGRATION_FILE_NAME_REGEX = "V_(\\d+)_?(\\d)?_?_?[\\w-\\d]*.(sql|class)";
	;

	public static void run() throws Exception {

		String dataModelClassName = Apps.get("APP_DATA_MODEL_CLASS");
		boolean avoidExecutions = !Apps.get("SCHEMA_GENERATION_TYPE").equals("none");
		if(Is.empty(dataModelClassName)){
			System.out.println("[fpg] Você não está utilizando o Controle de Versão do Modelo. Para ligar informe o path de uma classe que herde de VersionedDataModel no parâmertro APP_DATA_MODEL_CLASS");
			return;
		}
		VersionedDataModel appModel;
		try{
			Class dataModelClass = Class.forName(dataModelClassName);
			appModel = (VersionedDataModel) dataModelClass.newInstance();
		}catch (Exception ex){
			System.out.println("[fpg] Você não definiu uma classe corretamente para APP_DATA_MODEL_CLASS="+Apps.get("APP_DATA_MODEL_CLASS"));
			throw ex;
		}
		String actualVersion = appModel.getVersion();
		System.out.println("Actual Version: "+actualVersion);

		File versionsMigrationDir = new File(Apps.get("CLASSES_REAL_PATH")+"/migration/versions");
		if(!versionsMigrationDir.exists()) {
			System.out.println("\nMODEL MIGRATION (not found: 'migration/versions' dir)...");
		}else{
			System.out.println("\nMODEL MIGRATION STARTED (we found 'migration/versions' dir)...");

			ArrayList<File> migrationFiles = new ArrayList<>();
			File[] migrationRoots = versionsMigrationDir.listFiles();
			for(File f : migrationRoots){
				if(!f.isDirectory()){
					migrationFiles.add(f);
				}
			}
			if(migrationFiles.size()>1){
				sortByVersion(migrationFiles);
			}

			File pastVersionsDir = new File(versionsMigrationDir.getAbsoluteFile()+"/past");
			if(Apps.devMode() && !Apps.get("DEPLOY_MODE").equals("production") && pastVersionsDir.exists() && pastVersionsDir.isDirectory() && appModel.getVersionNum() < getVersionNumOf(migrationFiles.get(0).getName())){
				System.out.println("Rodaremos scripts guardados no diretório 'past'...");
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
			String novaVersao = null;
			if(avoidExecutions){
				System.out.println("SCHEMA_GENERATION_TYPE="+Apps.get("SCHEMA_GENERATION_TYPE")+", migrations não serão executados enquanto este for diferente de 'none'!");
				novaVersao = getVersionStrOf(migrationFiles.get(migrationFiles.size()-1).getName());
			}else{
				for(File versionFile : migrationFiles){
					double versionNum = getVersionNumOf(versionFile.getName());
					if(versionNum > appModel.getVersionNum()){
						System.out.println(" > "+versionFile.getName());
						if (FileUtil.extensionFormat(versionFile.getName()).equals("sql")) {
							try {
								Dao.getInstance().beginTransaction();
								Dao.getInstance().executeSQLs(true, FileUtil.getStringLines(versionFile));
								Dao.getInstance().commitTransaction();
							} catch (Exception e) {
								e.printStackTrace();
								if (Dao.getInstance().isTransactionActive()) {
									Dao.getInstance().rollBackTransaction();
								}
							}
						}
						else if (FileUtil.extensionFormat(versionFile.getName()).equals("class")) {
							try {
								Dao.getInstance().beginTransaction();
								Class.forName("migration.versions." + versionFile.getName().replace(".class", "")).newInstance();
								Dao.getInstance().commitTransaction();
							} catch (Exception e) {
								e.printStackTrace();
								if (Dao.getInstance().isTransactionActive()) {
									Dao.getInstance().rollBackTransaction();
								}
							}
						}
						novaVersao = getVersionStrOf(versionFile.getName());
					}
				}
			}
			if(novaVersao!=null){
				System.out.println("Versão Atualizada para: "+novaVersao);
				appModel.setVersion(novaVersao);
			}else{
				System.out.println("Nenhuma atualização a ser realizada!");
			}
		}
		Dao.getInstance().close();
	}

	private static ArrayList sortByVersion(ArrayList lista) {
		Collections.sort(lista, (Comparator<File>) (f1, f2) -> getVersionNumOf(f1.getName()) > getVersionNumOf(f2.getName())? 1 : -1);
		return lista;
	}

	private static void addMigrationFile(ArrayList<File> migrationFiles, File f) {
		if(f.getName().matches(MIGRATION_FILE_NAME_REGEX)){
			migrationFiles.add(f);
		}else if(!f.getParentFile().isDirectory()){
			if(f.getParentFile().getName().equals("res") || f.getParentFile().getName().startsWith("res_")){
				System.out.println("Migration Res file: "+f.getAbsolutePath());
			}else{
				throw new RuntimeException("ATENÇÃO! O arquivo "+f.getName()+" não está escrito no padrão de migration. Por segurança, o migration foi interrompido.");
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