package org.futurepages.core.path;

import org.futurepages.core.config.Apps;
import javax.servlet.http.HttpServletRequest;
import org.futurepages.util.The;

/**
 * Retorna os endereços (URL) absolutos da aplicação.
 * 
 * @author leandro
 */
public class Paths {

	private static Paths INSTANCE = null;

	
	public static void initialize() {
		initialize(null);
	}

	public static void initialize(String staticContext) {
		if(staticContext != null){
			INSTANCE = new StaticPaths(staticContext);
		}else{
			INSTANCE = new Paths();
		}
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
	//#### ATENÇÃO: AO ALTERAR QUALQUER LÓGICA DESTES MÉTODOS, DEVERÁ ALTERAR TAMBÉM EM StaticPaths
	
   /**
     * @param req Requisição
     * @param module id do módulo
     * @return a url completa do módulo
     */
    public static String module(HttpServletRequest req,String module) {
        return INSTANCE.getModule(req, module);
    }

    public static String moduleAction(HttpServletRequest req, String moduleId) {
       return INSTANCE.getModuleAction(req, moduleId);
    }

    /**
     * @param req Requisição
     * @return a url completa da pasta de recursos da aplicação
     */
    public static String resource(HttpServletRequest req) {
        return INSTANCE.getResource(req);
    }

	/**
     * @param req Requisição
     * @return a url completa da pasta de recursos da aplicação
     */
    public static String resource(HttpServletRequest req, String module) {
        return INSTANCE.getResource(req,module);
    }

    /**
     * @param req Requisição
     * @return a url completa da pasta de arquivos do template da aplicação
     */
    public static String template(HttpServletRequest req){
        return INSTANCE.getTemplate(req);
    }

    /**
     *
     * @param req Requisição
     * @return o endereço do contexto da aplicação, exemplo: /application
     */
    public static String context(HttpServletRequest req){
        return INSTANCE.getContext(req);
    }

    /**
     *
     * @param req Requisição
     * @return a url completa do host server da aplicação
     */
    public static String host(HttpServletRequest req){
        return INSTANCE.getHost(req);
    }

	public static String template(HttpServletRequest req, String module) {
		return INSTANCE.getTemplate(req, module);
	}
}