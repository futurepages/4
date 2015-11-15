
package org.futurepages.menta.core.resource;

import org.futurepages.core.config.Apps;
import org.futurepages.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author leandro
 */
public class ResourceMinifier {

	public void execute(String minifyMode) {
		boolean js = (minifyMode.equals("js")||minifyMode.equals("both"));
		boolean css = (minifyMode.equals("css")||minifyMode.equals("both"));
		File webDir = new File(Apps.get("WEB_REAL_PATH"));

		if(js){
			List<File> jsFiles = FileUtil.listFilesFromDirectory(webDir, true, ".*\\.js");
			try {
				(new JavaScriptMinifier()).execute(jsFiles);
			} catch (IOException ex) {
				System.out.println("[ Erro ao Compactar Javascript ... ]");
				ex.printStackTrace();
			}
		}
		if(css){
			List<File> cssFiles = FileUtil.listFilesFromDirectory(webDir, true, ".*\\.css");
			try {
				(new CSSMinifier()).execute(cssFiles);
			} catch (IOException ex) {
				System.out.println("[ Erro ao Compactar CSS ... ]");
				ex.printStackTrace();
			}
		}
	}
}
