package org.futurepages.menta.core.resource;

import org.futurepages.core.config.Apps;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 *
 * @author leandro
 */
public class JavaScriptMinifier {

	public void execute(List<File> jsFiles) throws IOException {
		File alreadyCompressed = new File(Apps.get("WEB_REAL_PATH") + "/META-INF/JS_COMPRESSED");
		if (!alreadyCompressed.exists()) { //só comprime se não tiver comprimido ainda.
			//noinspection ResultOfMethodCallIgnored
			alreadyCompressed.createNewFile();
			int pathInit = Apps.get("WEB_REAL_PATH").length() - 1;
			for (File f : jsFiles) {
				String jsPath = (f.getAbsolutePath().substring(pathInit));
				String outLogJsPath = "[ JS-Min ...  ] " + jsPath;
				System.out.print(outLogJsPath);
				if(new File(f.getParent()+"/.jsminifier-ignore").exists()
					|| jsPath.startsWith("/init/")
					|| f.getAbsolutePath().endsWith(".min.js")
					|| f.getAbsolutePath().endsWith("-min.js")
					|| f.getAbsolutePath().endsWith(".bundle.js")
				) {
					System.out.println("   IGNORED!");
					continue;
				}else{
					System.out.println();
				}
				String s;
				BufferedReader stdInput = null;
				BufferedReader stdError = null;
				try {
					Process p = Runtime.getRuntime().exec("./usr/bin/terser " + f.getAbsolutePath() + " -c -o " + f.getAbsolutePath());
					stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
					stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

					// read the output from the command
					boolean hasSomethingWrong = false;
					while ((s = stdInput.readLine()) != null) {
						System.out.println("\t\t\t\t" + s);
						hasSomethingWrong = true;
					}

					// read any errors from the attempted command
					while ((s = stdError.readLine()) != null) {
						System.out.println("\t\t\t\t" + s);
						hasSomethingWrong = true;
					}
					if (hasSomethingWrong) {
						System.out.println();
					}
					stdInput.close();
					stdError.close();
				} catch (Exception ex) {
					System.out.println("PROBLEM RUNNING: ./usr/bin/terser");
					ex.printStackTrace();
					if (stdInput != null) {
						stdInput.close();
					}
					if (stdError != null) {
						stdError.close();
					}
					break;
				}
			}
		}
		System.out.println("\n\n ------------- \n\n");
	}
}
