package org.futurepages.core;

import org.futurepages.core.config.Apps;
import org.futurepages.core.control.vaadin.DefaultAppServlet;
import org.futurepages.core.exception.DefaultExceptionLogger;
import org.futurepages.core.install.InstallersManager;
import org.futurepages.core.locale.Txt;
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
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.Map;

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
			initTxts(appsAndModules);
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

	private void initTxts(File[] appsAndModules) throws IOException {
		log("Loading Locale txts...");
		Txt.initialize(appsAndModules);
		log("Locale txts loaded.");
	}

	private void configureApplicationServlets(ServletContext context) throws ClassNotFoundException, ServletException {
			String appsPaths = Apps.get("APPS");

			String[] apps = appsPaths.split(",");
			Map<String,String> appsMapping = new LinkedHashMap<>();
			for(String app : apps){
				app = app.trim();
				String appPackage, appPath;
				if(!app.contains(":")){
					appPackage = app;
					appPath = "/*";
				}else{
					String[] appPackageAndPath = app.split("\\:");
					appPackage = appPackageAndPath[0];
					appPath = (!appPackageAndPath[1].equals("/")?appPackageAndPath[1]:"")+(appPackageAndPath[1].contains("*")?"":"/*");
				}
				if(appsMapping.get(appPackage)!=null){
					throw new ServletException("Unable to load servlets. Define only one application package per path. ");
				}
				if(appsMapping.values().contains(appPath)){
					throw new ServletException("Unable to load servlets. Duplicatated path: \""+appPath+"\"");
				}
				appsMapping.put(appPackage, appPath);
			}

			context.setInitParameter("productionMode", String.valueOf(!Apps.devMode())); // for VAADIN Production Mode
			for(String appPackagePath : appsMapping.keySet()){
				Class servletClass = DefaultAppServlet.class;
				String servletName = appPackagePath.replaceAll("\\.","_")+"_Servlet";
				String servletMapping = appsMapping.get(appPackagePath);

	            Servlet servlet = context.createServlet(servletClass);
				ServletRegistration.Dynamic d = context.addServlet(servletName, servlet);
				d.setInitParameter("UI", appPackagePath+".AppUI");
				d.setInitParameter("widgetset",appPackagePath+".app_conf.widgetset");
				d.addMapping(servletMapping);
				//it's necessary when it's a subpath...
				if(!servletMapping.equals("/*")){
					d.addMapping("/VAADIN/*");
				}
			}
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
		//TODO Load only requireds motdules by apps???
		log("Loading Apps and Modules...");

		File[] appsAndModules = Apps.getInstance().listModulesAndApps();

		log("Apps and Modules OK");
		return 	appsAndModules;
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