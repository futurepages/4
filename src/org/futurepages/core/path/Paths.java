package org.futurepages.core.path;

import org.futurepages.core.config.Apps;
import javax.servlet.http.HttpServletRequest;

import org.futurepages.menta.core.control.Controller;
import org.futurepages.util.The;

/**
 * Retorna os endereços (URL) absolutos da aplicação.
 * 
 * @author leandro
 */
public class Paths {


	public static Paths getInstance(){
		Paths instance = Controller.getInstance().getPaths();
		if(instance==null){
			return getStatic();
		}else {
			return instance;
		}
	}

	public Paths(){}

	public Paths(HttpServletRequest req) {
		staticPaths = new StaticPaths(req);
	}

	private static StaticPaths staticPaths;

	public static StaticPaths getStatic(){
		if(staticPaths == null){
			staticPaths = new StaticPaths();
		}
		return staticPaths;
	}
	//#### ESCOPO DINÂMICO ########################################################################

    public String getModule(HttpServletRequest req,String module) {
        return getContext(req)+"/"+ Apps.MODULES_PATH+"/"+((module!=null)?module:"");
    }

    public String getModuleAction(HttpServletRequest req, String moduleId) {
        return getContext(req)+"/"+moduleId;
    }

    public String getResource(HttpServletRequest req) {
        return The.concat(getContext(req),"/", Apps.get("RESOURCE_PATH"));
    }

    public String getResource(HttpServletRequest req, String module) {
        return The.concat(getModule(req,module),"/", Apps.get("RESOURCE_PATH"));
    }

    public String getTheme(HttpServletRequest req){
        return The.concat(getTemplate(req),"/",Apps.get("THEMES_DIR_NAME"),"/",Apps.get("THEME"));
    }

    public String getTemplate(HttpServletRequest req){
        return getContext(req)+"/"+ Apps.TEMPLATE_PATH;
    }

    public String getContext(HttpServletRequest req){
        return req.getContextPath();
    }

    public String getHost(HttpServletRequest req){
        return The.concat(req.getScheme(),"://",req.getServerName(),(req.getServerPort()!=80 && req.getServerPort()!= 443 ? ":"+req.getServerPort() : "" ));
    }

	public String getTemplate(HttpServletRequest req, String module) {
		return (module==null? getTemplate(req) : getModule(req, module)+"/"+ Apps.TEMPLATE_PATH);
	}

	
	//#### ESCOPO ESTÁTICO ########################################################################

    public static String module(HttpServletRequest req,String module) {
        return getInstance().getModule(req, module);
    }

    public static String moduleAction(HttpServletRequest req, String moduleId) {
       return getInstance().getModuleAction(req, moduleId);
    }

    public static String resource(HttpServletRequest req) {
        return getInstance().getResource(req);
    }

    public static String resource(HttpServletRequest req, String module) {
        return getInstance().getResource(req,module);
    }

    public static String template(HttpServletRequest req){
        return getInstance().getTemplate(req);
    }

    public static String context(HttpServletRequest req){
        return getInstance().getContext(req);
    }

    public static String host(HttpServletRequest req){
        return getInstance().getHost(req);
    }

	public static String template(HttpServletRequest req, String module) {
		return getInstance().getTemplate(req, module);
	}

    public static String theme(HttpServletRequest req){
        return getInstance().getTheme(req);
    }
}