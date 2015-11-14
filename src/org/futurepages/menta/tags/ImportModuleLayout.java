package org.futurepages.menta.tags;

import org.futurepages.core.config.Apps;
import org.futurepages.menta.annotations.Tag;
import org.futurepages.menta.annotations.TagAttribute;
import org.futurepages.menta.core.control.Controller;
import org.futurepages.core.path.Paths;
import org.futurepages.menta.core.tags.PrintTag;
import org.futurepages.menta.core.tags.build.ContentTypeEnum;
import org.futurepages.menta.filters.ModuleIdFilter;
import org.futurepages.util.Is;
import org.futurepages.util.The;

import javax.servlet.jsp.JspException;

/**
 * @author Leandro
 */
@Tag(bodyContent = ContentTypeEnum.EMPTY)
public class ImportModuleLayout extends PrintTag {

	@TagAttribute(required = false)
	private String autoImportModule = null;

	public void setAutoImportModule(String autoImportModule) {
		this.autoImportModule = autoImportModule;
	}

	@Override
	public String getStringToPrint() throws JspException {
		try {
			StringBuffer sb = new StringBuffer();
			boolean autoImporting = !Is.empty(autoImportModule);
			String moduleId = ModuleIdFilter.getModuleId(action);
			if (autoImporting) {
				sb.append(cssLayoutTag(autoImportModule));
			}
			if (!Is.empty(moduleId)) {
				if (Apps.get("USE_MODULE_DEPENDENCY").equals("true")) {
					java.util.Set<String> dependencies = Controller.getInstance().getAppManager().getDependenciesOf(moduleId);
					for (String dependency : dependencies) {
						if (!autoImporting || !autoImportModule.equals(dependency)) {
							sb.append(cssLayoutTag(dependency));
						}
					}
				}

				if (!autoImporting || !moduleId.equals(autoImportModule)) {
					sb.append(cssLayoutTag(moduleId));
				}
			}

			return sb.toString();
		} catch (Exception ex) {
			return "";
		}
	}

	private String cssLayoutTag(String moduleId) {
		return (The.concat("<link rel=\"stylesheet\" type=\"text/css\" href=\"" , Paths.module(req, moduleId) , "/template/layout.css" , Apps.get("RELEASE_QUERY") , "\" media=\"all\"/>\n"));
	}
}
