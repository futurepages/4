package org.futurepages.menta.core.control;

import org.futurepages.core.config.Apps;
import org.futurepages.core.path.Paths;
import org.futurepages.core.path.StaticPaths;
import org.futurepages.menta.core.ApplicationManager;
import org.futurepages.menta.core.action.Action;
import org.futurepages.menta.core.callback.ConsequenceCallback;
import org.futurepages.menta.core.consequence.Consequence;
import org.futurepages.menta.core.consequence.ConsequenceProvider;
import org.futurepages.menta.core.consequence.DefaultConsequenceProvider;
import org.futurepages.menta.core.context.ApplicationContext;
import org.futurepages.menta.core.context.Context;
import org.futurepages.menta.core.context.CookieContext;
import org.futurepages.menta.core.context.MapContext;
import org.futurepages.menta.core.context.SessionContext;
import org.futurepages.menta.core.filter.AfterConsequenceFilter;
import org.futurepages.menta.core.filter.Filter;
import org.futurepages.menta.core.filter.GlobalFilterFree;
import org.futurepages.menta.core.formatter.FormatterManager;
import org.futurepages.menta.core.i18n.LocaleManager;
import org.futurepages.menta.core.input.PrettyGlobalURLRequestInput;
import org.futurepages.menta.core.input.PrettyURLRequestInput;
import org.futurepages.menta.core.output.ResponseOutput;
import org.futurepages.menta.exceptions.PageNotFoundException;
import org.futurepages.menta.filters.ConsequenceCallbackFilter;
import org.futurepages.menta.filters.ExceptionFilter;
import org.futurepages.menta.filters.GlobalFilterFreeFilter;
import org.futurepages.util.Is;
import org.futurepages.util.The;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * The central controller. The actions are intercepted and
 * executed by this controller. The controller is also responsable for creating
 * and starting the ApplicationManager.
 *
 * @author Sergio Oliveira
 * @author Rubem Azenha (rubem.azenha@gmail.com)
 * @author Leandro Santana Pereira
 * @author Danilo Batista
 */
public class Controller extends HttpServlet {

	private char innerActionSeparator = '.';
	private Set<String> moduleIDs;
	private String startPage = null;
	private ApplicationManager appManager = null;
	private static String appMgrClassname = null;
	private static ServletContext application = null;
	protected static Context appContext = null;
	private static ConsequenceProvider defaultConsequenceProvider = new DefaultConsequenceProvider();
	public static Controller INSTANCE;
	private static ThreadLocal<InvocationChain> chainTL = new ThreadLocal<>();
	private static ThreadLocal<Paths> pathsTL = new ThreadLocal<>();
	private static ServletConfig conf;
	private static ClassGetActionUrlParts objectGetActionUrlParts;
	private static boolean initialized = false;
	private static boolean up = true;

	public static void makeUnavailable() {
		up = false;
	}

	public static void makeAvailable() {
		up = true;
	}

	static  {
		boolean isDebugging = false;
		Pattern debugPattern = Pattern.compile("-Xdebubg|jdwp");

		for (String arg : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
			if (debugPattern.matcher(arg).find()) {
				isDebugging =true;
				break;
			}
		}
		if (isDebugging && Apps.get("DEBUG_MODE")!=null && Apps.get("DEBUG_MODE").equalsIgnoreCase("ON")) {
			objectGetActionUrlParts = new ClassGetActionUrlPartsDebugMode();
		}
		else {
			objectGetActionUrlParts = new ClassGetActionUrlPartsExecutionMode();
		}
	}

	public static Controller getInstance() {
		return INSTANCE;
	}

	/**
	 * Initialize the Controller, creating and starting the ApplicationManager.
	 */
	@Override
	public void init(ServletConfig conf) throws ServletException {
		super.init(conf);
		Controller.conf = conf;
		try {
			initialized = false;
			startPage = Apps.get("START_PAGE_NAME");

			innerActionSeparator = '-';
			INSTANCE = this;

			this.configureServlet(conf);
			initApplicationManager();
			initialized = true;
		} catch (Exception ex) {
			ExceptionFilter.Logger.getInstance().execute(ex, null, null, true);
		}

	}

	public static boolean isInitialized(){
		return initialized;
	}


	/**
	 * Initialize the Controller, creating and starting the ApplicationManager.
	 */
	public void offLineInit() throws ServletException {
		try {
			startPage = Apps.get("START_PAGE_NAME");

			innerActionSeparator = '-';
			INSTANCE = this;
			configureServletOffLine();
			initApplicationManager();
		} catch (Exception ex) {
			ExceptionFilter.Logger.getInstance().execute(ex, null, null, true);
		}

	}

	public static void fixEncoding(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		String charset = Apps.get("PAGE_ENCODING");
		if (charset.equals("UTF-8")) {
			request.setCharacterEncoding(charset);
		}
		response.setCharacterEncoding(charset);
		// se algum dia precisar, para aplicativos mobile, descomentar... (by Dimmy)
		response.setHeader("Access-Control-Allow-Origin","*");
	}

	/**
	 * Creates the AplicationManager and starts it.
	 *
	 * @throws javax.servlet.ServletException
	 */
	private void initApplicationManager() throws ServletException {

		Class<? extends Object> klass = null;

		try {
			klass = Class.forName(appMgrClassname);
		} catch (ClassNotFoundException e) {
			throw new ServletException("Could not find application manager: " + appMgrClassname, e);
		}

		try {
			appManager = (ApplicationManager) klass.newInstance();

			AbstractApplicationManager.setApplication(appContext);

			moduleIDs = appManager.moduleIds();

			appManager.init(appContext);
			appManager.loadLocales();
			appManager.loadActions();
			appManager.registerChains();

			// Load some pre-defined formatters here.
			FormatterManager.init();

			appManager.loadFormatters();

		} catch (Exception e) {
			throw new ServletException(
					"Exception while loading application manager " + appMgrClassname + ": " + e.getMessage(), e);
		}
	}

	/**
	 * Destroy all filters defined in the ApplicationManager, call the destroy()
	 * method of ApplicationManager, then call super.destroy() to destroy this
	 * servlet (the Controller).
	 */
	@Override
	public void destroy() {
		if (appManager != null) {
			Set<Filter> filters = appManager.getAllFilters();
			for (Filter f : filters) {
				f.destroy();
			}

			// call destroy from appmanager...
			appManager.destroy(appContext);
		}
		chainTL.remove();
		pathsTL.remove();

		super.destroy();
		//comentado - não se sabe para que servia no menta.
		//LocaleManager.stopLocaleScan();
	}

	/**
	 * Returns the ServletContext of your web application.
	 *
	 * @return The ServletContext of your web application.
	 */
	public ServletContext getApplication() {
		return application;
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		try {
			if(up){
				pathsTL.set(getPathsFor(req));
				doService(req, res);
			}else{
				res.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
			}
		} catch (Exception ex) {
			ExceptionFilter.Logger.getInstance().execute(ex, getChain(), req, true);
			if((ex instanceof PageNotFoundException) || (ex.getCause()!=null && ex.getCause() instanceof PageNotFoundException)){
				res.sendError(HttpServletResponse.SC_NOT_FOUND);
			}
		} finally {
			if (up) {
				chainTL.remove();
				pathsTL.remove();
			}
		}
	}

	private Paths getPathsFor(HttpServletRequest req) {
		if(!Is.empty(Apps.get("AUTO_REDIRECT_DOMAIN"))) {
			return new StaticPaths(req);
		}else{
			return new Paths(req);
		}
	}

	protected void doService(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		if (appManager == null) {
			throw new ServletException("The Application manager is not loaded");
		}
		fixEncoding(req, res);

		appManager.service(appContext, req, res);

		String actionName = null;
		String innerAction = null;
		String[] prettyUrlParts = objectGetActionUrlParts.getActionUrlParts(req, this);
		actionName = prettyUrlParts[0];
		innerAction = prettyUrlParts[1];

		// Para exibir a url requisitada que chegou ao Controller.
		if(Apps.devLocalMode()){
			System.out.println("\n"+req.getRequestURL().append((req.getQueryString()!=null?"?"+req.getQueryString():""))); //for DEBUG-MODE
		}

		ActionConfig ac = null;

		if (innerAction != null) {
			ac = appManager.getActionConfig(actionName, innerAction);
		}

		if (ac == null) {
			ac = appManager.getActionConfig(actionName);
		}

		if (ac == null) {
			if (AbstractApplicationManager.getDefaultAction() != null) {
				ac = AbstractApplicationManager.getDefaultAction();
			} else {
				throw new PageNotFoundException("Could not find the action for actionName: " + actionName + (innerAction != null ? "." + innerAction : ""));
			}
		}

		Action action = ac.getAction(); // create an action instance here...

		if (action == null) {
			throw new ServletException("Could not get an action instance: " + ac);
		}

		prepareAction(action, ac.isGlobal(), req, res);

		List<Object> filters = new LinkedList<Object>();
		Consequence c = null;
		boolean conseqExecuted = false;
		boolean actionExecuted = false;
		StringBuilder returnedResult = new StringBuilder(32);

		try {
			c = invokeAction(ac, action, innerAction, filters, returnedResult);
			actionExecuted = true;
			c.execute(action, req, res);
			conseqExecuted = true;
		} catch (Exception e) {
			Throwable cause = getRootCause(e);
			res.sendError(500);
			throw new ServletException("Exception while invoking action " + actionName + ": " + e.getMessage() + " / " + e.getClass().getName() + " / " + cause.getMessage() + " / " + cause.getClass().getName(), cause);
		} finally {
			/*
			 * Here we check all filters that were executed together with the
			 * action. If they are AfterConsequenceFilters, we need to call the
			 * afterConsequence method.
			 */
			Iterator<Object> iter = filters.iterator();
			ArrayList<ConsequenceCallbackFilter> callbackFilters = null;
			String returnedFromAction = returnedResult.toString().length() > 0 ? returnedResult.toString() : null;
			while (iter.hasNext()) {
				Filter f = (Filter) iter.next();
				if (f instanceof AfterConsequenceFilter) {
					AfterConsequenceFilter acf = (AfterConsequenceFilter) f;
					try {
						acf.afterConsequence(action, c, conseqExecuted, actionExecuted, returnedFromAction);
					} catch (Exception e) {
						throw new ServletException("Exception while executing the AfterConsequence filters: " + e.getMessage(), e);
					}
					if (f instanceof ConsequenceCallbackFilter) {
						if (callbackFilters == null) {
							callbackFilters = new ArrayList<ConsequenceCallbackFilter>();
						}
						callbackFilters.add((ConsequenceCallbackFilter) f);
					}
				}
			}
			if(actionExecuted && conseqExecuted && callbackFilters != null
				// e não ocorreu exception no meio do caminho...
			   && !(returnedFromAction!=null && (returnedFromAction.equals(Action.EXCEPTION) ||  returnedFromAction.equals(Action.DYN_EXCEPTION)) )
			) {
				for (ConsequenceCallbackFilter f : callbackFilters) {
					try {
						ConsequenceCallback cc = f.getCallbackClass().newInstance();
						cc.setActionData(action.getCallback());
						cc.setActionReturn(returnedResult.toString());
						cc.setCaller(ac.getName() + "-" + innerAction);
						Thread thread = new Thread(cc);
						thread.start();
					} catch (Exception ex) {
						throw new ServletException("Exception while invoking Consequence Callbacks. " + ex.getMessage());
					}
				}
			}
		}
	}

	private Throwable getRootCause(Throwable t) {
		Throwable curr = t;
		while (curr.getCause() != null) {
			curr = curr.getCause();
		}
		return curr;
	}

	/**
	 * Invoke an action and return the consequence generated by this invocation.
	 * This method also return all filters that were executed together with the
	 * action inside the filters list parameter.
	 *
	 * @param ac  The ActionConfig which contains the consequences for this action.
	 * @param action The action to invoke.
	 * @param innerAction The inner action to execute or null to execute the regular action (execute() method).
	 * @param filters The filters that were applied to the action. (You should pass an empty list here!)
	 * @return A consequence generated by this invocation.
	 * @throws Exception if there was an exception executing the action.
	 *
	 */
	public Consequence invokeAction(ActionConfig ac, Action action, String innerAction, List<Object> filters, StringBuilder returnedResult) throws Exception {

		InvocationChain chain = createInvocationChain(ac, action, innerAction);
		if (chainTL.get() == null) { //no caso do ChainConsequence, o chain mantido é o da action original.
			chainTL.set(chain); //setado para gerenciar o log das exceptions
		}

		// copy all filters executed together with that action to the filters parameter...

		if (filters == null || !filters.isEmpty()) {

			throw new IllegalArgumentException(
					"filters parameter should be non-null and a zero-sized list!");
		}

		for (Filter filter : chain.getFilters()) {
			filters.add(filter);
		}

		// execute chain!
		String result = chain.invoke();
		returnedResult.append(result);

		// If there is an inner action, try to get a consequence for the inner
		// action
		Consequence c = null;
		if (innerAction != null) {
			c = ac.getConsequence(result, innerAction);
		}

		// If not found, try to get a consequene specific for that action
		if (c == null) {
			c = ac.getConsequence(result);
		}

		// If not found, try to get a global consequence
		if (c == null) {
			c = appManager.getGlobalConsequence(result);
		}

		// use the default consequence provider...
		if (c == null) {
			c = defaultConsequenceProvider.getConsequence(ac.getName(), ac.getActionClass(), result, innerAction);
		}

		if (c == null) {
			throw new ServletException("Action has no consequence for result: " + ac.getName() + " - " + result);
		}

		if(Apps.devMode()){
			System.out.println("<#"+ Thread.currentThread().getId()+"#> "+ ac.getName() + "(" + ac.getActionClass().getName() + ")" + ((innerAction!=null)?"."+innerAction:"")+"["+result.toUpperCase()+"] -> "+(c!=null?c.toString():" NULL")); //for DEBUG-MODE
		}

		return c;
	}

	public static void setThredLocalChain(InvocationChain chain) {
		chainTL.set(chain);
	}

	private boolean hasGlobalFilterFreeMarkerFilter(List<Filter> filters, String innerAction) {
		for (Filter f : filters) {
			if (GlobalFilterFreeFilter.class.isAssignableFrom(f.getClass())) {
				GlobalFilterFreeFilter gffmf = (GlobalFilterFreeFilter) f;
				return gffmf.isGlobalFilterFree(innerAction);
			}
		}
		return false;
	}

	private InvocationChain createInvocationChain(ActionConfig ac, Action action, String innerAction) {

		InvocationChain chain = new InvocationChain(ac.getName(), action);
		Object actionImpl = action;

		// first place the "firstFilters" for the action...

		List<Filter> firstFilters = ac.getFirstFilters(innerAction);
		if (firstFilters != null) {
			chain.addFilters(firstFilters);
		}

		// place the global filters that are NOT LAST...
		boolean isGlobalFilterFree = false;
		if (actionImpl instanceof GlobalFilterFree) {
			GlobalFilterFree gff = (GlobalFilterFree) actionImpl;
			isGlobalFilterFree = gff.isGlobalFilterFree(innerAction);
		}

		if (!isGlobalFilterFree) {
			List<Filter> globals = appManager.getGlobalFilters(false);
			if (globals != null) {
				chain.addFilters(globals);
			}
		}

		// place the action specific filters...
		List<Filter> filters = ac.getFilters(innerAction);
		if (filters != null) {
			isGlobalFilterFree = hasGlobalFilterFreeMarkerFilter(filters, innerAction);
			if (isGlobalFilterFree) {
				// remove previously added global filters...
				chain.clearFilters();
			}
			chain.addFilters(filters);
		}

		// place the global filters that are LAST

		if (!isGlobalFilterFree) {
			List<Filter> globals = appManager.getGlobalFilters(true);
			if (globals != null) {
				chain.addFilters(globals);
			}
		}

		if (innerAction != null) {
			chain.setInnerAction(innerAction);
		}
		return chain;
	}

	//Only for prettyURLs
	//[0] module+subAction+Action
	//[1] innerAction
	private String[] getActionUrlPartsExecutionMode(HttpServletRequest req) {

		String context = req.getContextPath();

		String uri = req.getRequestURI();

		return getActionUrlParts(context, uri);
	}

	//Only for prettyURLs
	//[0] module+subAction+Action
	//[1] innerAction
	private String[] getActionUrlPartsDebugMode(HttpServletRequest req) {

		String context = req.getContextPath();

		String uri = req.getRequestURI();

		String debugParameter = req.getParameter("debug");

		String possivelUri1 = context + "/" + Apps.get("START_PAGE_NAME");
		String possivelUri2 = possivelUri1 + "/";

		if (debugParameter!=null
				&& debugParameter.equalsIgnoreCase("ac")
				&& (uri.equalsIgnoreCase(possivelUri1)
						|| uri.equalsIgnoreCase(possivelUri2))) {
			restartController();
		}

		return getActionUrlParts(context, uri);
	}

	public InvocationChain getChain(){
		return chainTL.get();
	}

	public Paths getPaths(){
		return pathsTL.get();
	}

	public String[] getActionUrlParts(String context, String uri) {

		// remove the context from the uri, if present
		if (context.length() > 0 && uri.indexOf(context) == 0) {
			uri = uri.substring(context.length());
		}

		// cut the first '/'
		if (uri.startsWith("/") && uri.length() > 1) {
			uri = uri.substring(1);
		}

		// cut the last '/'
		if (uri.endsWith("/") && uri.length() > 1) {
			uri = uri.substring(0, uri.length() - 1);
		}

		String[] rawParts = uri.split("/");


		if (isModule(rawParts[0])) {
			if (rawParts.length == 1) {
				if (rawParts[0].equals(startPage)) {
					return new String[]{rawParts[0], null};
				} else {
					return new String[]{The.concat(rawParts[0], "/", startPage), null};
				}
			} else if (rawParts.length >= 2) {
				String module = rawParts[0];
				String actionName = null;
				String innerName = null;
				if(this.getAppManager().moduleHasSub(module, rawParts[1])){
					if(rawParts.length!=2){
						int index = rawParts[2].indexOf(innerActionSeparator);
						String actionPart = (index > 0 ? rawParts[2].substring(0, index) : rawParts[2]);
						actionName =  The.concat(module, "/", rawParts[1], "/", actionPart);
						innerName = (index > 0 && index + 1 < rawParts[2].length() ? rawParts[2].substring(index + 1) : null);
					}else{
						actionName =  The.concat(module, "/", rawParts[1], "/", startPage);
						innerName = null;
					}
				}else {
					int index = rawParts[1].indexOf(innerActionSeparator);
					String actionPart = (index > 0 ? rawParts[1].substring(0, index) : rawParts[1]);
					actionName =  The.concat(module, "/", actionPart);
					innerName = (index > 0 && index + 1 < rawParts[1].length() ? rawParts[1].substring(index + 1) : null);
				}
				return new String[]{actionName, innerName};
			}
		} else {
			int index = rawParts[0].indexOf(innerActionSeparator);
			String actionName = (index > 0 ? rawParts[0].substring(0, index) : rawParts[0]);
			String innerName = (index > 0 && index + 1 < rawParts[0].length() ? rawParts[0].substring(index + 1) : null);
			return new String[]{actionName, innerName};
		}

		return null;
	}

	protected void prepareAction(Action action, boolean global, HttpServletRequest req, HttpServletResponse res) {

		if (!global) {
			action.setInput(new PrettyURLRequestInput(req));
		} else {
			action.setInput(new PrettyGlobalURLRequestInput(req));
		}
		action.setOutput(new ResponseOutput(res));
		action.setSession(new SessionContext(req, res));
		action.setApplication(appContext);
		action.setCookies(new CookieContext(req, res));
		action.setLocale(LocaleManager.getLocale(req));
		action.setCallback(new MapContext());
	}

	protected String getObsoleteActionName(HttpServletRequest req) {
		String uri = getURI(req);
		// If there is an Inner Action, cut it off from the action name
		int index = uri.lastIndexOf(innerActionSeparator);
		if (index > 0 && (uri.length() - index) >= 2) {
			uri = uri.substring(0, index);
		}
		return uri;
	}

	protected String getObsoleteInnerActionName(HttpServletRequest req) {
		String uri = getURI(req);
		String innerAction = null;
		int index = uri.lastIndexOf(".");
		if (index > 0 && (uri.length() - index) >= 2) {
			innerAction = uri.substring(index + 1, uri.length());
		}
		return innerAction;
	}

	/**
	 * Returns the URI from this request. URI = URI - context - extension. This
	 * method is used by getActionName and getInnerActionName. You may call this
	 * method in your own controller subclass. Ex: /myapp/UserAction.add.fpg
	 * will return UserAction.add
	 */
	protected String getURI(HttpServletRequest req) {
		String context = req.getContextPath();
		String uri = req.getRequestURI();
		// remove the context from the uri, if present
		if (context.length() > 0 && uri.indexOf(context) == 0) {
			uri = uri.substring(context.length());
		}

		if (uri.equals("/")) {
			return Apps.get("START_PAGE_NAME");
		}

		// cut the extension...
		int index = uri.lastIndexOf(".");

		if (index > 0) {
			uri = uri.substring(0, index);
		}

		// cut the first '/'
		if (uri.startsWith("/") && uri.length() > 1) {
			uri = uri.substring(1, uri.length());
		}
		return uri;
	}

	public char getInnerActionSeparator() {
		return innerActionSeparator;
	}

	private void configureServletOffLine() {
		appMgrClassname = "org.futurepages.core.ApplicationManager"; // default without package...
		appContext = new MapContext();
	}
	private void configureServlet(ServletConfig conf) {
		application = conf.getServletContext();
		appContext = new ApplicationContext(application);
		AbstractApplicationManager.setRealPath(application.getRealPath(""));

		// gets the AplicationManager class
		appMgrClassname = conf.getInitParameter("applicationManager");

		if (appMgrClassname == null || appMgrClassname.trim().equals("")) {
			appMgrClassname = "ApplicationManager"; // default without package...
		}
	}

	public boolean isModule(String pattern) {
		return moduleIDs.contains(pattern);
	}

	public ApplicationManager getAppManager() {
		return appManager;
	}

	private static void restartController()  {
		try {
			INSTANCE.destroy();
			INSTANCE.init(conf);
			System.out.println("**************** Controller reiniciado com sucesso! *******************");
		}
		catch (Exception e) {
			System.out.println("**************** Erro no reinicio do Controller! *******************");
			e.printStackTrace();
		}
	}

	static abstract class  ClassGetActionUrlParts {

		abstract String[] getActionUrlParts(HttpServletRequest req, Controller c);

	}

	static class ClassGetActionUrlPartsExecutionMode extends  ClassGetActionUrlParts{

		@Override
		public String[] getActionUrlParts(HttpServletRequest req, Controller c) {
			return c.getActionUrlPartsExecutionMode(req);
		}

	}

	static class ClassGetActionUrlPartsDebugMode extends ClassGetActionUrlParts{

		@Override
		public String[] getActionUrlParts(HttpServletRequest req, Controller c) {
			return c.getActionUrlPartsDebugMode(req);
		}

	}

}
