package org.futurepages.core.control;

import org.futurepages.core.config.Apps;
import org.futurepages.core.exception.AppLogger;
import org.futurepages.core.locale.Txt;
import org.futurepages.core.mail.MailConfig;
import org.futurepages.core.persistence.HibernateManager;
import org.futurepages.core.quartz.QuartzManager;
import org.futurepages.exceptions.NotModuleException;
import org.futurepages.util.Is;
import org.futurepages.util.ModuleUtil;
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
@Deprecated
public class AppListener implements ServletContextListener {

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
//			initHibernateAndConsequences(appsAndModules); //TODO enable it.
			initTxts(appsAndModules);
			initQuartzManager(appsAndModules);

			initEmailConfigurations();

			//initAutoRedirectionEngine(context); //TODO enable it.

			configureApplicationServlets(ctx);

			log(ctx.getServletContextName() + " started.");

		} catch (Exception ex) {
			log("Error trying to start context.");
			AppLogger.getInstance().execute(ex);
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
			Map<String,String> appsMapping = new LinkedHashMap();
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
				Class servletClass = AppServlet.class;
				String servletName = appPackagePath.replaceAll("\\.","_")+"_Servlet";
				String servletMapping = appsMapping.get(appPackagePath);

	            Servlet servlet = context.createServlet(servletClass);
				ServletRegistration.Dynamic d = context.addServlet(servletName, servlet);
				d.setInitParameter("UI", appPackagePath+".AppUI");
				d.setInitParameter("widgetset","apps.Widgetsets");
				d.addMapping(servletMapping);
				//it's necessary when it's a subpath...
				if(!servletMapping.equals("/*")){
					d.addMapping("/VAADIN/*");
				}
			}
		log("vaadin-productionMode: " + context.getInitParameter("productionMode"));
	}


	private File[] loadAppsAndModules() {
		log("Loading Apps and Modules...");

		//TODO Load only requireds motdules by apps??? (need module-dependencies)
		File[] appsAndModules = ModuleUtil.listModulesAndApps();

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

	@Override
	public void contextDestroyed(ServletContextEvent evt) {
		log("Stopping: " + evt.getServletContext().getServletContextName());
		try {
			QuartzManager.shutdown();
			log("Quartz Schedulers stopped.");
		} catch (SchedulerException ex) {
			log("Quartz Schedulers ERROR: " + ex.getMessage());
			ex.printStackTrace();
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