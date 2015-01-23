package org.futurepages.tags;

import javax.servlet.jsp.JspException;

import org.futurepages.annotations.Tag;
import org.futurepages.annotations.TagAttribute;
import org.futurepages.core.config.Params;
import org.futurepages.core.control.Controller;
import org.futurepages.core.path.Paths;
import org.futurepages.core.tags.PrintTag;
import org.futurepages.core.tags.build.ContentTypeEnum;
import org.futurepages.filters.ModuleIdFilter;
import org.futurepages.util.Is;

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
				if (Params.get("USE_MODULE_DEPENDENCY").equals("true")) {
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
		return ("<link rel=\"stylesheet\" type=\"text/css\" href=\"" + Paths.module(req, moduleId) + "/template/layout.css" + Params.get("RELEASE_QUERY") + "\" media=\"all\"/>\n");
	}
}
