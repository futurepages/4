package org.futurepages.core.path;

import javax.servlet.http.HttpServletRequest;
import org.futurepages.core.config.Params;
import org.futurepages.util.The;

/**
 * Retorna os endereços (URL) absolutos da aplicação.
 * 
 * @author leandro
 */
public class StaticPaths extends Paths {


	private String hostPath;
	private String contextPath;
	private String modulePath;
	private String modulesActionPath;
	private String resourceInternalPath;
	private String resourcePath;
	private String templatePath;
	private String themePath;

	public StaticPaths(String context) {
		hostPath = Params.get("AUTO_REDIRECT_DOMAIN");
		contextPath = The.concat("/",context);
		modulePath = The.concat(contextPath,"/",Params.MODULES_PATH,"/");
		modulesActionPath = (Params.get("PRETTY_URL").equals("true")) ? The.concat(contextPath,"/") : modulePath;
		resourceInternalPath =  Params.get("RESOURCE_PATH");
		resourcePath =  The.concat(contextPath,"/",resourceInternalPath);
		templatePath = The.concat(contextPath,"/",Params.TEMPLATE_PATH);
		themePath =  The.concat(templatePath,"/",Params.get("THEMES_DIR_NAME"),"/",Params.get("THEME"));

	}

	@Override
   public String getModule(HttpServletRequest req,String module) {
		return The.concat(modulePath,((module!=null)?module:""));
    }

	@Override
    public String getModuleAction(HttpServletRequest req, String moduleId) {
		return The.concat(modulesActionPath,moduleId);
    }

	@Override
    public String getResource(HttpServletRequest req) {
        return resourcePath;
    }

	@Override
    public String getResource(HttpServletRequest req, String module) {
        return The.concat(getModule(req,module),"/",resourceInternalPath);
    }

	@Override
    public String getTemplate(HttpServletRequest req){
        return templatePath;
    }

	@Override
    public String getTheme(HttpServletRequest req){
        return themePath;
    }


	@Override
    public String getHost(HttpServletRequest req){
        return hostPath;
    }

	@Override
	public String getTemplate(HttpServletRequest req, String module) {
		return (module==null? getTemplate(req) : The.concat(modulePath,module,"/",Params.TEMPLATE_PATH));
	}
}