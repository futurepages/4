package org.futurepages.core;

import org.futurepages.core.config.Apps;
import org.futurepages.core.exception.DefaultExceptionLogger;
import org.futurepages.core.install.InstallersManager;
import org.futurepages.core.mail.MailConfig;
import org.futurepages.core.path.Paths;
import org.futurepages.core.persistence.HibernateManager;
import org.futurepages.core.persistence.SchemaGeneration;
import org.futurepages.core.quartz.QuartzManager;
import org.futurepages.core.resource.ResourceMinifier;
import org.futurepages.exceptions.NotModuleException;
import org.futurepages.util.Is;
import org.quartz.SchedulerException;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;

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
			ServletContext ctx = evt.getServletContext();
			String context = ctx.getContextPath();
			contextName = (!Is.empty(context) ? context : "ROOT");

			log("Starting " + ctx.getServletContextName() + "...");

			initAppsProperties(context);
			configEncoding();

			File[] appsAndModules = loadAppsAndModules();

			initHibernateAndConsequences(appsAndModules);
			initQuartzManager(appsAndModules);
			initEmailConfigurations();

			//initAutoRedirectionEngine(context);

			//compressWebTextRes();

			configureApplicationServlets(ctx);

			log(ctx.getServletContextName() + " started.");

		} catch (Exception ex) {
			log("Error trying to start context.");
			DefaultExceptionLogger.getInstance().execute(ex);
		}
	}

	private void configureApplicationServlets(ServletContext context) throws ClassNotFoundException, ServletException {
			Class klass = Class.forName("com.empresadedicada.EDServlet");
            Class oneClass = (Class) klass;
            Servlet s = context.createServlet(oneClass);

            ServletRegistration.Dynamic d = context.addServlet("EDServlet", s);
			context.getAttributeNames();
			context.setInitParameter("productionMode",String.valueOf(!Apps.devMode())); // for VAADIN Production Mode
			d.setInitParameter("UI", "com.empresadedicada.EDUI");
            d.addMapping("/*");

			log("vaadin-productionMode: " + context.getInitParameter("productionMode"));
	}

	// TODO REVER SE AINDA SERÁ NECESSÁRIO...
	//Compact Web Resource
	private void compressWebTextRes() throws Exception {
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
	}

	private File[] loadAppsAndModules() {
		//TODO Loading Apps
		//TODO Load only requireds motdules by apps...
		log("Loading Modules...");
		File[] modules = (new File(Apps.get("MODULES_CLASSES_REAL_PATH"))).listFiles();
		log("Modules OK");
		return 	modules;

	}

	private void configEncoding() {
		System.setProperty("file.encoding", Apps.get("ENCODING"));
		System.setProperty("sun.jnu.encoding", Apps.get("ENCODING"));
		log("File Encoding: " + System.getProperty("file.encoding"));
	}

	private void initAppsProperties(String context) throws UnsupportedEncodingException {
		log("Starting apps properties...");
		Apps.init(context);
		log("Properties OK");
	}

	private void initAutoRedirectionEngine(String context) {
			if(!Is.empty(Apps.get("AUTO_REDIRECT_DOMAIN")) && Apps.get("AUTO_REDIRECT_DOMAIN").contains("://") ){
				log("Auto Redirect Domain ON. Starting 'Static Paths'...");
				Paths.initialize(context); //only if the AUTO_REDIRECT_DOMAIN is complete with protocol.
			}else{
				if(Apps.get("DEPLOY_MODE").equals("production")){
					if(Is.empty(Apps.get("AUTO_REDIRECT_DOMAIN"))){
						log("Auto Redirect Domain OFF. Consider the usage of the param AUTO_REDIRECT_DOMAIN in production environments.");
					}
				}
				Paths.initialize();
			}
	}

	//Start Quartz Manager (Task Scheduler) if QUARTZ_MODE = on
	private void initQuartzManager(File[] modules) throws ClassNotFoundException, NotModuleException, ParseException, SchedulerException {
		if (Apps.get("QUARTZ_MODE").equals("on")) {
			log("Quartz ...");
			QuartzManager.initialize(modules);
			log("Quartz OK");
		}
	}

	//Start the params of email configuration
	private void initEmailConfigurations() throws Exception {
			if (Apps.get("EMAIL_ACTIVE").equals("true")) {
				log("Email Config ...");
				MailConfig.initialize();
				log("Email Config OK");
			}
	}

	private void initHibernateAndConsequences(File[] modules) throws Exception {
			if (HibernateManager.isRunning()) {
				log("Hibernate OK");

				// Update/generate database scheme as params configuration file.
				// only if it's not DEPLOY_MODE=production, it will try to generate and install database.
				log("Deploy Mode: "+ Apps.get("DEPLOY_MODE"));
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

					//If INSTALL_MODE=on, install-modules will start.
					String installMode = Apps.get("INSTALL_MODE");
					if (!installMode.equals("off") && !installMode.equals("none") && !Is.empty(installMode)) {
						log("Install Mode: " + installMode);
						InstallersManager.initialize(modules, installMode);
						log("Install - End");
					}
				}else{
					//Micro-migration. Instantiate the class where we put migration executions.
					if(!Is.empty(Apps.get("MIGRATION_CLASSPATH"))){
						try{
							log("Migration Class: "+ Apps.get("MIGRATION_CLASSPATH"));
							Class.forName(Apps.get("MIGRATION_CLASSPATH")).newInstance();
						}catch(Exception ex){
							log("Migration ERROR... "+ex.getMessage());
						}
					}
				}
			} else {
				log("WARNING: HIBERNATE is not running!");
			}
	}

	@Override
	public void contextDestroyed(ServletContextEvent evt) {
		log("Stopping: " + evt.getServletContext().getServletContextName());
		if (Apps.get("QUARTZ_MODE").equals("on")) {
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