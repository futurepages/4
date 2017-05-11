package org.futurepages.menta.core;

import org.futurepages.core.DeployMigrations;
import org.futurepages.core.config.Apps;
import org.futurepages.core.exception.AppLogger;
import org.futurepages.core.install.InstallersManager;
import org.futurepages.core.mail.MailConfig;
import org.futurepages.core.path.Paths;
import org.futurepages.core.persistence.HibernateManager;
import org.futurepages.core.persistence.SchemaGeneration;
import org.futurepages.core.quartz.QuartzManager;
import org.futurepages.menta.core.resource.ResourceMinifier;
import org.futurepages.menta.core.tags.build.TagLibBuilder;
import org.futurepages.util.Is;
import org.quartz.SchedulerException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;

/**
 * É nesta classe onde o futurepages age sobre a aplicação,
 * gerando o necessário antes do deploy da aplicação
 * e desalocando o que for necessário no undeploy da mesma.
 *
 * @author leandro
 */
public class ApplicationListener implements ServletContextListener {

	private static final String ON = "on";
	private String contextName;

	@Override
	public void contextInitialized(ServletContextEvent evt) {
		try {
			ServletContext servletContext = evt.getServletContext();
			String context = evt.getServletContext().getContextPath();
			contextName = (!Is.empty(context) ? context : "ROOT");

			log("Inicializando " + servletContext.getServletContextName() + "...");

			log("Inicializando Parâmetros...");
			Apps.init(context);
			log("Parâmetros OK");

			System.setProperty("file.encoding", Apps.get("PAGE_ENCODING"));
			System.setProperty("sun.jnu.encoding", Apps.get("PAGE_ENCODING"));
			log("File Encoding: "+ System.getProperty("file.encoding"));

			log("Carregando módulos...");
			File[] modules = (new File(Apps.get("MODULES_CLASSES_REAL_PATH"))).listFiles();
			log("Módulos OK");

			if (HibernateManager.isRunning()) {
				log("Hibernate OK");

				// Atualiza/gera esquema do banco como solicitado no arquivo de configuração.
				// somente se NÃO estiver em DEPLOY_MODE=production (tentará gerar e instalar banco de dados.
				log("Deploy Mode: "+Apps.get("DEPLOY_MODE"));
				if (!Apps.get("DEPLOY_MODE").equals("production")) {
					if (Apps.get("SCHEMA_GENERATION_TYPE").startsWith("update")) {
						if (Apps.get("SCHEMA_GENERATION_TYPE").equals("update")) {
							log("SCHEMA UPDATE - Begin");
							SchemaGeneration.update(false);
							log("SCHEMA UPDATE - End");
						} else if (Apps.get("SCHEMA_GENERATION_TYPE").equals("update_beans")) {
							log("SCHEMA UPDATE JUST BEANS - Begin");
							SchemaGeneration.update(true);
							log("SCHEMA UPDATE JUST BEANS - End");
						}
					} else if (Apps.get("SCHEMA_GENERATION_TYPE").equals("export")) {
						log("SCHEMA EXPORT - Begin");
						SchemaGeneration.export();
						log("SCHEMA EXPORT - End");
					}

					//Se o modo de instalação estiver ligado, serão feitas as instalações de cada módulo.
					String installMode = Apps.get("INSTALL_MODE");
					if (!installMode.equals("off") && !installMode.equals("none") && !Is.empty(installMode)) {
						log("Install Mode: " + installMode);
						InstallersManager.initialize(modules, installMode);
						log("Install - End");
					}
				}else{
					//Micro-migração. Instancia a classe - onde espera-se que estejam as execuções de migração.
					if(!Is.empty(Apps.get("MIGRATION_CLASSPATH"))){
						try{
							log("Inicializando Migração em: "+Apps.get("MIGRATION_CLASSPATH"));
							Class.forName(Apps.get("MIGRATION_CLASSPATH")).newInstance();
						}catch(Exception ex){
							log("Erro de Migração... "+ex.getMessage());
						}
					}
					if (Apps.get("DEPLOY_MODE").equals("production")
					 || Apps.get("DEPLOY_MODE").equals("pre-production")) {
						DeployMigrations.run();
					}
				}
			} else {
				log("WARNING: HIBERNATE is not running!");
			}

			//Inicializa o gerenciador do Quartz (Agendador de Tarefas) caso solicitado.
			if (Apps.get("QUARTZ_MODE").equals(ON)) {
				log("Iniciando Quartz...");
				QuartzManager.initialize(modules);
				log("Quartz Inicializado.");
			}

			//Inicializa os parâmetros de configuração de Email se solicitado.
			if (Apps.get("EMAIL_ACTIVE").equals("true")) {
				log("Configurando Email...: ");
				MailConfig.initialize();
				log("Config Email OK");
			}

			//Por padrão gera o arquivo taglib.tld com as tags dos módulos da aplicação
			if (Apps.get("GENERATE_TAGLIB").equals("true")) {
				log("Iniciando criação da Taglib.");
				(new TagLibBuilder(modules)).build();
				log("Taglib criada com sucesso.");
			}

			if(!Is.empty(Apps.get("AUTO_REDIRECT_DOMAIN")) && Apps.get("AUTO_REDIRECT_DOMAIN").contains("://") ){
				log("Auto Redirect Domain ON. Inicializando 'Static Paths'...");
				Paths.initialize(context); //somente se o AUTO_REDIRECT_DOMAIN for completo com protocolo.
			}else{
				if(Apps.get("DEPLOY_MODE").equals("production")){
					if(Is.empty(Apps.get("AUTO_REDIRECT_DOMAIN"))){
						log("Auto Redirect Domain OFF. Prefira usar o parâmetro AUTO_REDIRECT_DOMAIN para melhorar a performance da aplicação.");
					}
				}
				Paths.initialize();
			}

			//Compacta recursos web
			String minifyMode = Apps.get("MINIFY_RESOURCE_MODE");
			if (!minifyMode.equals("none")) {  //none, js, css, both
				log("MINIFY RESOURCE MODE = " + minifyMode);
				(new ResourceMinifier()).execute(minifyMode);
				log("MINIFY RESOURCE DONE.");
			}
			if (Apps.get("DEPLOY_MODE").equals("production")
			 || Apps.get("DEPLOY_MODE").equals("pre-production")) {
				Apps.getInstance().removeFileAutomations();
			}

			log(servletContext.getServletContextName() + " inicializado.");
		} catch (Exception ex) {
			log("Erro ao inicializar contexto.");
			AppLogger.getInstance().execute(ex);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent evt) {
		log("Parando: " + evt.getServletContext().getServletContextName());
		if (Apps.get("QUARTZ_MODE").equals(ON)) {
			try {
				QuartzManager.shutdown();
				log("Schedulers do Quartz parado.");
			} catch (SchedulerException ex) {
				log("Erro ao tentar parar Schedulers do Quartz: " + ex.getMessage());
				AppLogger.getInstance().execute(ex);
			}
		}
		if (HibernateManager.isRunning()) {
			HibernateManager.shutdown();
		}
		log("Aplicação parada: " + evt.getServletContext().getServletContextName());
	}

	/**
	 * Mensagem de log padrão do listener.
	 * @param logText the Text!
	 */
	private void log(String logText) {
		System.out.println("[::" + contextName + "::] " + logText);
	}
}
