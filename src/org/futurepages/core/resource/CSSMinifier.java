package org.futurepages.core.resource;

import com.yahoo.platform.yui.compressor.CssCompressor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import org.futurepages.core.config.Params;
import org.futurepages.core.exception.DefaultExceptionLogger;

/**
 *
 * @author leandro
 */
public class CSSMinifier {

	public void execute(List<File> jsFiles) throws IOException {
		File alreadyCompressed = new File(Params.get("WEB_REAL_PATH") + "/META-INF/CSS_COMPRESSED");
		if (!alreadyCompressed.exists()) { //só comprime se não tiver comprimido ainda.
			int pathInit = Params.get("WEB_REAL_PATH").length() - 1;
			for (File f : jsFiles) {
				FileInputStream fis = null;
				InputStreamReader inReader = null;
				try {
					System.out.println("[ CSS-Min ... ] " + f.getAbsolutePath().substring(pathInit));
					fis = new FileInputStream(f);
					inReader = new InputStreamReader(fis);
					CssCompressor compressor = new CssCompressor(inReader);
					FileWriter fileWriter = new FileWriter(f);
					compressor.compress(fileWriter, Integer.MAX_VALUE);
					fileWriter.close();
					fis.close();
					inReader.close();
					System.out.println("[ CSS-Min OK! ]");
				} catch (Exception ex) {
					System.out.println("[# CSS-ERROR #]");
					DefaultExceptionLogger.getInstance().execute(ex);
					try {
						fis.close();
						inReader.close();
					} catch (IOException ex2) {
						DefaultExceptionLogger.getInstance().execute(ex2);
					}
				}
			}
			alreadyCompressed.createNewFile();
		}
	}
}
