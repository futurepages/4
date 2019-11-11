package org.futurepages.menta.core.control;

import org.futurepages.menta.consequences.AjaxConsequence;
import org.futurepages.menta.consequences.Chain;
import org.futurepages.menta.consequences.Forward;
import org.futurepages.menta.consequences.Redirect;
import org.futurepages.menta.consequences.StreamConsequence;
import org.futurepages.menta.core.action.Manipulable;
import org.futurepages.menta.core.ajax.AjaxRenderer;
import org.futurepages.menta.core.consequence.Consequence;
import org.futurepages.menta.core.context.Context;
import org.futurepages.menta.core.filter.Filter;
import org.futurepages.menta.filters.InjectionFilter;
import org.futurepages.menta.filters.OutjectionFilter;
import org.futurepages.util.The;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The central abstract base manager which controls actions, filters, locales and data lists.
 * You can use this class to register actions and filters through the loadActions() method.
 * You can use this class to specify supported locales through the loadLocales() method.
 * You can use this class to manage the data list loading process.
 * You can use this class to initialize anything for your web application.
 *
 * @author Sergio Oliveira Jr.
 * @author Fernando Boaglio.
 * @author Leandro Santana Pereira.
 */
public abstract class AbstractApplicationManager  implements Manipulable{

	public static String EXTENSION = ".fpg";

    private static String realPath;

	private Map<String, ActionConfig> actions = new HashMap<String, ActionConfig>();
	private Map<String, Map<String, ActionConfig>> innerActions = new HashMap<String, Map<String, ActionConfig>>();
	private List<Filter> globalFilters = new LinkedList<Filter>();
    private List<Filter> globalFiltersLast = new LinkedList<Filter>();
	private Map<String, Consequence> globalConsequences = new HashMap<String, Consequence>();

    private List<Chain> chains = new ArrayList<Chain>();

    private static ActionConfig defaultAction = null;
    private static Context appContext = null;
    private List<String> actionPackages = new LinkedList<String>();

    static void setApplication(Context appContext) {
       AbstractApplicationManager.appContext = appContext;
    }



    public static Context getApplication() {
       return appContext;
    }

    public static void setRealPath(String realpath) {
        realPath = realpath;
    }

    public static void setDefaultAction(ActionConfig ac) {
       AbstractApplicationManager.defaultAction = ac;
    }

    public static ActionConfig getDefaultAction() {
       return defaultAction;
    }

    public StreamConsequence stream(String contentType) {
    	return new StreamConsequence(contentType);
    }

    public void addActionPackage(String actionPackage) {
    	actionPackages.add(actionPackage);
    }

    public void removeActionPackage(String actionPackage) {
    	actionPackages.remove(actionPackage);
    }

    /**
     * Returns this web application's real path.
     * For example: c:\program files\tomcat\webapps\myapplication
     *
     * @return The real path
     */
    public static String getRealPath() {
        return realPath;
    }

    /**
     * Default constructor
     */
    public AbstractApplicationManager(){
    	super();
    }

    /**
     * Reset this application manager. All configuration (actions, filters, etc) is discarded.
     */
    public void reset() {
        actions.clear();
        innerActions.clear();
        globalFilters.clear();
        globalFiltersLast.clear();
        globalConsequences.clear();
    }

    public void service(Context appContext, HttpServletRequest req, HttpServletResponse res) {

    }

	/**
	 * Register an ActionConfig for the controller.
     *
	 *
	 * @param ac The ActionConfig to register
     * @return The ActionConfig it receives to register
     * @throws IllegalStateException if you try to add an action config with no name (internal action config)
	 */
	public ActionConfig addActionConfig(ActionConfig ac) {
        if (ac.getName() == null) throw new IllegalStateException("Cannot add an action config without a name!");
        String innerAction = ac.getNamedInnerAction();
        if (innerAction == null) {
				actions.put(ac.getName(), ac);
        } else {
            Map<String, ActionConfig> map = innerActions.get(ac.getName());
            if (map == null) {
                map = new HashMap<String, ActionConfig>();
                innerActions.put(ac.getName(), map);
            }
            map.put(innerAction, ac);
        }

        String[] parts = ac.getName().split("/");
        if(parts.length > 2){
            Controller.getInstance().getAppManager().addSubModule(parts[0],parts[1]);
        }
//      // DESCOMENTAR PARA CONTABILIZAR CASOS DE USO DO SISTEMA
//		System.out.println(ac.toString());
        return ac;
	}

	/**
	 * Remove an action config from this application manager.
	 *
	 * @param ac The action config to remove
	 * @return true if removed, false if not found
	 * @since 1.8
	 */
	public boolean removeActionConfig(ActionConfig ac) {
		String name = ac.getName();
		if (name == null) throw new IllegalStateException("Cannot remove an action config without a name!");
		String innerAction = ac.getNamedInnerAction();
		if (innerAction == null) {
			return actions.remove(name) != null;
		} else {
			Map<String, ActionConfig> map = innerActions.get(name);
            if (map != null) {
            	return map.remove(innerAction) != null;
            }
            return false;
		}
	}

    /**
     * Shorter version of addActionConfig.
     *
     * @param ac
     * @return The ActionConfig it receives
     */
    public ActionConfig add(ActionConfig ac) {
        return addActionConfig(ac);
    }

    /**
     * Override this method to do any initialization for your web application.
     *
     * @param application The application context of your web application.
     */
    public void init(Context application) {
    }

    /**
     * Called by the controller when the application is exiting.
     *
     * OBS: This is called by the Controller servlet's destroy method.
     */
    public void destroy(Context application) {  }

	/**
	 * Override this method to register actions and filters in this application manager.
	 */
	public void loadActions() { }

	/**
	 * Override this method to specify the supported locales for your application.
	 */
	public void loadLocales() {	}

    /**
     * Override this method to define formatters that can be used by fpg:out tag
     */
    public void loadFormatters() {

    }

	/**
	 * Gets the ActionConfig with the given name or alias.
	 *
	 * @param name The name of the ActionConfig
	 * @return The ActionConfig associated with the given name
	 */
	public ActionConfig getActionConfig(String name) {
		ActionConfig ac = actions.get(name);
		if (ac == null) {
			ac = loadActionConfig(name);
		}
		return ac;
	}


	public Map<String, ActionConfig> getActions() {
		return actions;
	}

	/**
	 * Gets the Inner ActionConfig with the given name and inner action.
	 *
	 * @param name The name of the ActionConfig
     * @param innerAction The inner action of the ActionConfig.
	 * @return The Inner ActionConfig associated with the given name and inner action.
	 */
	public ActionConfig getActionConfig(String name, String innerAction) {

		ActionConfig ac = null;
        Map<String, ActionConfig> map = innerActions.get(name);

        if (map != null) {
        	ac = map.get(innerAction);
        }

        if (ac == null) {
        	ac = loadActionConfig(name);
        }
        return ac;
	}

	private ActionConfig loadActionConfig(String name) {
		StringBuilder sb = new StringBuilder(32);
		Iterator<String> iter = actionPackages.iterator();
		while(iter.hasNext()) {
			String actionPackage = iter.next();
			sb.setLength(0);
			sb.append(actionPackage).append('.').append(name);

			// check if action is in the classpath...
			Class<? extends Object> actionClass = null;
			try {
				actionClass = Class.forName(sb.toString());
			} catch(Exception e) {
				continue;
			}
			ActionConfig ac = new ActionConfig(actionClass);
			addActionConfig(ac);
			return ac;
		}

		return null;
	}

	/**
	 * Register a filter for all actions in this application manager.
	 * The filters registered with this method will be executed <i>before</i>
     * the specific action filters.
     *
	 * @param filter The filter to register as a global filter.
	 */
	public void addGlobalFilter(Filter filter) {
		addGlobalFilter(filter, false);
	}

    /**
     * Shorter version of addGlobalFilter.
     *
     * @param filter
     * @since 1.2
     */
    public void filter(Filter filter) {
        addGlobalFilter(filter);
    }

	/**
	 * Register a list of filters for all actions in this application manager.
	 * The filters registered with this method will be executed <i>before</i>
     * the specific action filters.
     *
	 * @param filters A list of filters.
	 */
	public void addGlobalFilter(List filters) {
		addGlobalFilter(filters, false);
	}

    /**
     * Shorter version of addGlobalFilter.
     *
     * @param filters
     * @since 1.2
     */
    public void filter(List filters) {
        addGlobalFilter(filters);
    }

	/**
	 * Register a filter for all actions in this application manager.
	 *
	 * @param filter The filter to register as a global filter.
     * @param last true if you want this filter to be executed <i>after</i> the specific action filters.
	 */
	public void addGlobalFilter(Filter filter, boolean last) {
        if (last) {

            globalFiltersLast.add(filter);

        } else if (filter.getClass().equals(InjectionFilter.class)
                || filter.getClass().equals(OutjectionFilter.class)) {

        		// force those filters to be the last in the chain, because if they are global that what makes sense...
        		globalFiltersLast.add(filter);

        } else {

		    globalFilters.add(filter);
        }
	}

    /**
     * Shorter version of addGlobalFilter.
     */
    public void filter(Filter filter, boolean last) {
        addGlobalFilter(filter, last);
    }

    /**
     * Shorter version of addFlobalFilter.
     */
    public void filterLast(Filter filter) {
    	addGlobalFilter(filter, true);
    }

	/**
	 * Register a list of filters for all actions in this application manager.
	 */
	public void addGlobalFilter(List filters, boolean last) {
        Iterator iter = filters.iterator();
        while(iter.hasNext()) {
            Filter f = (Filter) iter.next();
            addGlobalFilter(f, last);
        }
	}

    /**
     * Shorter version of addGlobalFilter.
     */
    public void filter(List filters, boolean last) {
        addGlobalFilter(filters, last);
    }

    /**
     * Shorter version of addGlobalFilter.
     *
     * @param filters
     * @since 1.3
     */
    public void filterLast(List filters) {
    	addGlobalFilter(filters, true);
    }

	/**
	 * Register a consequence for all actions in this application manager.
	 * A global consequence has precedence over action consequences.
	 *
	 * @param result The result for what a global consequence will be registered
	 * @param c The consequence to register as a global consequence
	 */
	public void addGlobalConsequence(String result, Consequence c) {
		globalConsequences.put(result, c);
	}

    /**
     * Shorter version of addGlobalConsequence.
     */
    public void on(String result, Consequence c) {
        addGlobalConsequence(result, c);
    }

    /**
     * Shorter verions of addGlobalConsequence that will assume a forward.
     *
     * @param result
     * @param jsp
     * @since 1.9
     */
    public void on(String result, String jsp) {
    	addGlobalConsequence(result, new Forward(jsp));
    }

	/**
	 * Gets the global filters registered in this application manager.
	 *
     * @param last true if you want the global filters registered to be executed <i>after</i> the specific action filters.
	 * @return A java.util.List with all the filters registered in this application manager.
     * @since 1.1.1
	 */
	public List<Filter> getGlobalFilters(boolean last) {
        if (last) return globalFiltersLast;
        return globalFilters;
	}

	/**
	 * Gets all the global filters registered in this application manager.
     * Note that it will sum up in a list the filters executed <i>before</i> and <i>after</i> the specific action filters.
	 *
	 * @return A java.util.List with all the filters registered in this application manager.
	 */
	public List<Filter> getGlobalFilters() {
        List<Filter> list = new LinkedList<Filter>();
        list.addAll(getGlobalFilters(false));
        list.addAll(getGlobalFilters(true));
        return list;
	}

	/**
	 * Gets a global consequence associated with the result.
	 *
	 * @param result The result for what to get a global consequence.
	 * @return A global consequence for the result.
	 */
	public Consequence getGlobalConsequence(String result) {
		return globalConsequences.get(result);
	}

    /*
     * This is useful for filter destroying in the Controller.
     */
    public Set<Filter> getAllFilters() {
        Set<Filter> filters = new HashSet<Filter>();
        filters.addAll(globalFilters);
        filters.addAll(globalFiltersLast);
        Iterator<ActionConfig> iterAc = actions.values().iterator();
		while(iterAc.hasNext()) {
            ActionConfig ac = iterAc.next();
            filters.addAll(ac.getFilters());
        }
		Iterator<Map<String, ActionConfig>> iter = innerActions.values().iterator();
        while(iter.hasNext()) {
        	Map<String, ActionConfig> map = iter.next();
            Iterator iter2 = map.values().iterator();
            while(iter2.hasNext()) {
                ActionConfig ac = (ActionConfig) iter2.next();
                filters.addAll(ac.getFilters());
            }
        }
        return filters;
    }

    public static Consequence fwd(String page) {

        return new Forward(page);

    }

    public static Consequence redir(String page) {
        return new Redirect(page);
    }

    public static Consequence redir(String page, boolean flag) {
        return new Redirect(page, flag);
    }

    public static Consequence redir() {
        return new Redirect();

    }

    public static Consequence redir(boolean appendOutput) {

        return new Redirect(appendOutput);

    }

    public static Consequence chain(ActionConfig ac) {

        return new Chain(ac);

    }

    public static Consequence ajax(AjaxRenderer renderer) {
       return new AjaxConsequence(AjaxConsequence.KEY, renderer);
    }

    public static Consequence chain(ActionConfig ac, String innerAction) {
       return new Chain(ac, innerAction);
    }

    public static Consequence chain(Class<? extends Object> klass) {
       return new Chain(new ActionConfig(klass));
    }

    public static Consequence chain(Class<? extends Object> klass, String innerAction) {

       return new Chain(new ActionConfig(klass, innerAction));
    }


    public ActionConfig action(Class<? extends Object> klass) {

        return addActionConfig(new ActionConfig(klass));
    }

    public ActionConfig action(String name, Class<? extends Object> klass) {
        return addActionConfig(new ActionConfig(name, klass));

    }

    public ActionConfig globalAction(String name, Class<? extends Object> klass) {
		ActionConfig ac = (new ActionConfig(name, klass));
		ac.setGlobal(true);
        return addActionConfig(ac);
    }

	/**
     * This method override the ApplicationManager attributes.
	 * @param parent The parent ApplicationManager
	 * @return
	 */
	public AbstractApplicationManager setParent(AbstractApplicationManager parent) {
    	this.actions = parent.actions;
    	this.globalConsequences = parent.globalConsequences;
    	this.globalFilters = parent.globalFilters;
    	this.globalFiltersLast = parent.globalFiltersLast;
    	this.innerActions = parent.innerActions;
    	this.chains = parent.chains;

    	return this;
	}

	protected Chain addChain(Chain chain){
		this.chains.add(chain);
		return chain;
	}

	/**
	 * Necessário ser chamado após o registro de todas as ActionConfig's, pois se fossem registradas
	 * durante o loadActions, algumas AC's não existiriam ainda no mapa.
	 */
	protected void registerChains(){
		for(Chain chain : chains){
			chain.extractInputParamsFromURI();
			ActionConfig ac = null;
			if(chain.getNamedInnerAction()==null){
				ac = this.getActionConfig(chain.getActionName());
			}else{
				ac = this.getActionConfig(chain.getActionName(), chain.getNamedInnerAction());
			}
			chain.setAc(ac);
		}
	}

	public Set<String> moduleIds(){
		return new HashSet<String>();
	}

	/**
	 * Used only by the framework. Don't use it in the ModuleManagers.
	 */
	protected void afterLoadManagers() {
		Map<String, ActionConfig> actionsAux = actions;
		actions = new HashMap<String, ActionConfig>();
		for (ActionConfig ac : actionsAux.values()) {
			if (!ac.getName().contains(",")) {
				actions.put(ac.getName(), ac);
			} else {
				String[] acNames = ac.getName().split(",");
				for (String acName : acNames) {
					ActionConfig newAc = The.cloneOf(ac);
					newAc.setName(acName);
					actions.put(newAc.getName(), newAc);
				}
			}
		}
		if (innerActions.size() > 0) {
			Map<String, Map<String, ActionConfig>> innerActionsAux = innerActions;
			innerActions = new HashMap<String, Map<String, ActionConfig>>();
			for (String actionName : innerActionsAux.keySet()) {
				Map<String, ActionConfig> actionInnerMap = innerActionsAux.get(actionName);
				if (!actionName.contains(",")) {
					innerActions.put(actionName, actionInnerMap);
				} else {
					String[] acNames = actionName.split(",");
					for (String acName : acNames) {
						Map<String, ActionConfig> newActionInnerMap = new HashMap<String, ActionConfig>();
						for (String innerKey : actionInnerMap.keySet()) {
							ActionConfig newAc = The.cloneOf(actionInnerMap.get(innerKey));
							newAc.setName(acName);
							newActionInnerMap.put(innerKey, newAc);
						}
						innerActions.put(acName, newActionInnerMap);
					}
				}
			}
		}
	}
}