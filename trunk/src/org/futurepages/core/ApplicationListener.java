package org.futurepages.core;

import org.futurepages.core.config.Params;
import org.futurepages.core.exception.DefaultExceptionLogger;
import org.futurepages.core.install.InstallersManager;
import org.futurepages.core.mail.MailConfig;
import org.futurepages.core.path.Paths;
import org.futurepages.core.persistence.HibernateManager;
import org.futurepages.core.persistence.SchemaGeneration;
import org.futurepages.core.quartz.QuartzManager;
import org.futurepages.core.resource.ResourceMinifier;
import org.futurepages.util.Is;
import org.quartz.SchedulerException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;

/**
 * Starting up the application resources:
 *  - param configurations
 *  - module configurations
 *  - etc.
 * @author leandro
 */
public class ApplicationListener implements ServletContextListener {

	private String contextName;

	@Override
	public void contextInitialized(ServletContextEvent evt) {
		try {
			ServletContext servletContext = evt.getServletContext();
			String context = servletContext.getContextPath();
			contextName = (!Is.empty(context) ? context : "ROOT");

			log("starting up" + servletContext.getServletContextName() + "...");

			log("Starting params...");
			Params.initialize(context);
			log("Params OK");

			System.setProperty("file.encoding"   ,   Params.get("PAGE_ENCODING"));
			System.setProperty("sun.jnu.encoding",   Params.get("PAGE_ENCODING"));
			log("File Encoding: "+System.getProperty("file.encoding"));

			log("Loading Modules...");
			File[] modules = (new File(Params.get("MODULES_CLASSES_REAL_PATH"))).listFiles();
			log("Modules OK");

			if (HibernateManager.isRunning()) {
				log("Hibernate OK");

				// Update/generate database scheme as params configuration file.
				// only if it's not DEPLOY_MODE=production, it will try to generate and install database.
				log("Deploy Mode: "+Params.get("DEPLOY_MODE"));
				if (!Params.get("DEPLOY_MODE").equals("production")) {
					if (Params.get("SCHEMA_GENERATION_TYPE").startsWith("update")) {
						if (Params.get("SCHEMA_GENERATION_TYPE").equals("update")) {
							log("SCHEMA UPDATE - Begin");
							SchemaGeneration.update(false);
							log("SCHEMA UPDATE - End");
						} else if (Params.get("SCHEMA_GENERATION_TYPE").equals("update_beans")) {
							log("SCHEMA UPDATE JUST BEANS - Begin");
							SchemaGeneration.update(true);
							log("SCHEMA UPDATE JUST BEANS - End");
						}
					} else if (Params.get("SCHEMA_GENERATION_TYPE").equals("export")) {
						log("SCHEMA EXPORT - Begin");
						SchemaGeneration.export();
						log("SCHEMA EXPORT - End");
					}

					//If INSTALL_MODE=on, install-modules will start.
					String installMode = Params.get("INSTALL_MODE");
					if (!installMode.equals("off") && !installMode.equals("none") && !Is.empty(installMode)) {
						log("Install Mode: " + installMode);
						InstallersManager.initialize(modules, installMode);
						log("Install - End");
					}
				}else{
					//Micro-migration. Instantiate the class where we put migration executions.
					if(!Is.empty(Params.get("MIGRATION_CLASSPATH"))){
						try{
							log("Migration Class: "+Params.get("MIGRATION_CLASSPATH"));
							Class.forName(Params.get("MIGRATION_CLASSPATH")).newInstance();
						}catch(Exception ex){
							log("Migration ERROR... "+ex.getMessage());
						}
					}
				}
			} else {
				log("WARNING: HIBERNATE is not running!");
			}

			//Start Quartz Manager (Task Scheduler) if QUARTZ_MODE = on
			if (Params.get("QUARTZ_MODE").equals("on")) {
				log("Quartz ...");
				QuartzManager.initialize(modules);
				log("Quartz OK");
			}

			//Start the params of email configuration
			if (Params.get("EMAIL_ACTIVE").equals("true")) {
				log("Email Config ...");
				MailConfig.initialize();
				log("Email Config OK");
			}

			if(!Is.empty(Params.get("AUTO_REDIRECT_DOMAIN")) && Params.get("AUTO_REDIRECT_DOMAIN").contains("://") ){
				log("Auto Redirect Domain ON. Starting 'Static Paths'...");
				Paths.initialize(context); //only if the AUTO_REDIRECT_DOMAIN is complete with protocol.
			}else{
				if(Params.get("DEPLOY_MODE").equals("production")){
					if(Is.empty(Params.get("AUTO_REDIRECT_DOMAIN"))){
						log("Auto Redirect Domain OFF. Consider the usage of the param AUTO_REDIRECT_DOMAIN in production environments.");
					}
				}
				Paths.initialize();
			}

			//Compact Web Resource
			String minifyMode = Params.get("MINIFY_RESOURCE_MODE");
			if (!minifyMode.equals("none")) {  //none, js, css, both
				log("MINIFY RESOURCE MODE = " + minifyMode);
				(new ResourceMinifier()).execute(minifyMode);
				log("MINIFY RESOURCE DONE.");
			}
			if (Params.get("DEPLOY_MODE").equals("production")
			 || Params.get("DEPLOY_MODE").equals("pre-production")) {
				Params.removeFileAutomations();
			}

			log(servletContext.getServletContextName() + " inicializado.");
		} catch (Exception ex) {
			log("Erro ao inicializar contexto.");
			DefaultExceptionLogger.getInstance().execute(ex);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent evt) {
		log("Stopping: " + evt.getServletContext().getServletContextName());
		if (Params.get("QUARTZ_MODE").equals("on")) {
			try {
				QuartzManager.shutdown();
				log("Quartz Schedulers stopped.");
			} catch (SchedulerException ex) {
				log("Quartz Schedulers ERROR: " + ex.getMessage());
				DefaultExceptionLogger.getInstance().execute(ex);
			}
		}
		if (HibernateManager.isRunning()) {
			HibernateManager.shutdown();
		}
		log("Application STOPPED: " + evt.getServletContext().getServletContextName());
	}

	/**
	 * @param logText the log text message.
	 */
	private void log(String logText) {
		System.out.println("[::" + contextName + "::] " + logText);
	}
}
