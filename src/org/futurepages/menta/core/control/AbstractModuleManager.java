package org.futurepages.menta.core.control;

import org.futurepages.core.config.Apps;
import org.futurepages.menta.actions.AjaxAction;
import org.futurepages.menta.actions.DynAction;
import org.futurepages.menta.consequences.Chain;
import org.futurepages.menta.consequences.Forward;
import org.futurepages.menta.consequences.Redirect;
import org.futurepages.menta.core.consequence.Consequence;
import org.futurepages.menta.json.JSONGenericRenderer;
import org.futurepages.util.ModuleUtil;
import org.futurepages.util.The;

public abstract class AbstractModuleManager extends AbstractApplicationManager {

	protected String webPath;
    protected String moduleId;
	protected String[] directDependencies;
    protected boolean integrationModule = false;

    private char innerActionSeparator;
    
    public AbstractModuleManager() {
        this.moduleId = AbstractModuleManager.moduleId(this.getClass());

		innerActionSeparator = Controller.getInstance().getInnerActionSeparator();
		
        webPathIt();
    }

	/**
	 *  Para realizar testes (retire o abstract da classe)
	 */
//	public AbstractModuleManager(String moduleId, boolean withPrettyURL, char innerActionSeparator) {
//		this.moduleId = moduleId;
//		this.withPrettyURL = withPrettyURL;
//		this.innerActionSeparator = innerActionSeparator;
//        webPathIt();
//	}


    protected void webPathIt() {
        this.webPath =  moduleId + "/";
    }

	private String modulePath(String moduleId){
        return moduleId + "/";
	}

    @Override
    public ActionConfig action(String act, Class<? extends Object> actionClass) {
        return super.action(withPath(act), actionClass);
    }

	@Override
	public ActionConfig globalAction(String act, Class<? extends Object> actionClass) {
        return super.globalAction(act, actionClass);
    }


    @Override
    public ActionConfig action(Class<? extends Object> actionClass) {
    	String lowerName = actionClass.getSimpleName().toLowerCase();
	    String actionName = actionClass.getSimpleName();
	    actionName = !actionName.equals(lowerName)? actionName+","+lowerName:actionName;
	    return this.action(actionName,actionClass);
    }
    
	public ActionConfig ajaxAction(String act, Class<? extends AjaxAction> actionClass) {
        return super.action(withPath(act), actionClass)
				.on(SUCCESS, ajax(new JSONGenericRenderer()))
				.on(ERROR, ajax(new JSONGenericRenderer()))
		;
    }

	public ActionConfig ajaxAction(Class<? extends AjaxAction> actionClass) {
        return action(actionClass)
				.on(SUCCESS, ajax(new JSONGenericRenderer()))
				.on(ERROR, ajax(new JSONGenericRenderer()))
		;
    }


	/**
	 *
	 * @param actionClass that implements DynAction
	 * @return actionConfig para DynAction => fwIn("dyn/Action.jsp") on SUCCESS and ERROR
	 */
	public ActionConfig dynAction(Class<? extends DynAction> actionClass) {
        return action(actionClass)
				.on(SUCCESS,fwIn("dyn/"+actionClass.getSimpleName().substring(3)+".jsp"))
				.on(ERROR,fwIn("dyn/"+actionClass.getSimpleName().substring(3)+".jsp"))
		;
    }

//TODO: se for descomentar, criar com a ideia de lowerCase como em action(Class)
//	public ActionConfig dynAction(String submodule, Class<? extends DynAction> actionClass) {
//        return action(submodule+"/"+actionClass.getSimpleName(),actionClass)
//				.on(SUCCESS,fwIn("dyn/"+submodule+"/"+actionClass.getSimpleName().substring(3)+".jsp"))
//				.on(ERROR,fwIn("dyn/"+submodule+"/"+actionClass.getSimpleName().substring(3)+".jsp"))
//		;
//    }

	
    protected Consequence fwIn(String page) {
        return (new Forward(withPath(null, page , false)));
    }


    protected Consequence fwd(String moduleId, String page){
        return (new Forward(withPath(moduleId, page, true)));
    }

    protected Consequence rdIn(String page) {
        return (new Redirect(withPath(null, page, false)));
    }

    protected Consequence rdIn(String page, boolean redirectParams) {
        return (new Redirect(withPath(null, page, false), redirectParams));
    }

    protected Consequence redir(String moduleId, String page){
        return (new Redirect(withPath(moduleId, page, true)));
    }

    protected Consequence redir(String moduleId, String page, boolean redirectParams){
        return (new Redirect(withPath(moduleId, page, true), redirectParams));
    }

    protected Consequence chain(String actionName) {
        return addChain(actionName);
    }

	protected Consequence chain(String moduleId, String actionName){
		return addChain(modulePath(moduleId), actionName);
	}

    protected Consequence chainIn(String actionName) {
        return addChain(modulePath(moduleId), actionName);
    }

	public String getModuleId(){
		return this.moduleId;
	}

	private Consequence addChain(String... actionPath){
		return addChain(new Chain(The.concat(actionPath)));
	}
	
    public static String moduleId(Class klass){
           return ModuleUtil.moduleId(klass);
    }

	public char getInnerActionSeparator() {
		return innerActionSeparator;
	}

	public void setInnerActionSeparator(char innerActionSeparator) {
		this.innerActionSeparator = innerActionSeparator;
	}

	protected void dependsOf(String... dependencies){
		if(directDependencies!=null){
			throw new RuntimeException("Não utilize mais de uma chamada de 'dependsOf()' dentro do mesmo loadDependencies() do ModuleManager");
		}
		directDependencies = dependencies;
	}

    public void loadDependencies() {
		directDependencies = new String[]{};
	}

    public String[] getDirectDependencies() {
		return directDependencies;
	}

	public void integrationModule(){
		this.integrationModule = true;
	}

	public boolean isIntegrationModule() {
		return this.integrationModule;
	}


    private String prettyCorrect(String page, boolean putModulePath) {
        if(page.contains(".")){ //.page , .jsp
            return Apps.MODULES_PATH+"/";
        }

        return "";
    }

	private String withPath(String actionPath){
		return withPath(null,actionPath,false);
	}

	/**
	 *
	 * @param moduleId
	 * @param actionPath
	 * @param prettyCorrect
	 * @return
	 */
	private String withPath(String moduleId, String actionPath, Boolean prettyCorrect){
		if(actionPath.contains(",")){
			String[] actions = actionPath.split(",");
			String[] actionsWithPath = new String[actions.length];
			int i = 0;
			for (String action : actions) {
				actionsWithPath[i] = The.concat((prettyCorrect != null ? prettyCorrect(action, prettyCorrect.booleanValue()) : ""),
						(moduleId != null ? moduleId + "/" : webPath),
						action
				);

				i++;
			}
			String actionWithPath = The.implodedArray(actionsWithPath, ",", null);
			return actionWithPath;
		}else{
			return The.concat(
					(prettyCorrect != null ? prettyCorrect(actionPath, prettyCorrect.booleanValue()) : ""),
					(moduleId != null ? moduleId + "/" : webPath),
					actionPath
			);
		}
	}

//remove abstract modifier from class signature to test...
//	public static void main(String[] args) {
//		AbstractModuleManager mm = new AbstractModuleManager("global", true, '-');
//		System.out.println(mm.redir("admin","Action")); //NAO!!!
//		System.out.println(mm.rdIn("Action,Axxx")); //OK
//		System.out.println(mm.redir("system","Action,XX")); //NAO!!
//		System.out.println(mm.redir("Action.jsp"));
//		System.out.println(mm.rdIn("Action-execute.page"));
//		System.out.println(mm.fwIn("Action-execute.page"));
//		System.out.println(mm.fwd("global","Action-execute.page"));
//	}

}