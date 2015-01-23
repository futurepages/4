package org.futurepages.core.resource;

import com.yahoo.platform.yui.compressor.JavaScriptCompressor;
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
public class JavaScriptMinifier {

	public void execute(List<File> jsFiles) throws IOException {
		File alreadyCompressed = new File(Params.get("WEB_REAL_PATH") + "/META-INF/JS_COMPRESSED");
		if (!alreadyCompressed.exists()) { //só comprime se não tiver comprimido ainda.
			alreadyCompressed.createNewFile();
			int pathInit = Params.get("WEB_REAL_PATH").length() - 1;
			FileWriter resultFileWriter = new FileWriter(alreadyCompressed);
			for (File f : jsFiles) {
				FileInputStream fis = null;
				FileWriter fileWriter = null;
				InputStreamReader inReader = null;
				fis = new FileInputStream(f);
				inReader = new InputStreamReader(fis);
				String jsPath = ("[ JS-Min ...  ] " + f.getAbsolutePath().substring(pathInit));
				System.out.println(jsPath);
				resultFileWriter.write(jsPath+"\n");
				try {
					JavaScriptCompressor compressor = new JavaScriptCompressor(inReader, new FpgErrorReporter());
					fileWriter = new FileWriter(f);
					compressor.compress(fileWriter, Integer.MAX_VALUE, true, false, true, false);
					fileWriter.close();
					String jsFileOK = ("[ JS-Min OK!  ]");
					resultFileWriter.write(jsFileOK+"\n");
					System.out.println(jsFileOK);
				} catch (Exception ex) {
					String jsFileNotOK = ("[# JS-ERROR  #]");
					resultFileWriter.write(jsFileNotOK+"\n");
					resultFileWriter.write(ex.getMessage()+"\n");
					resultFileWriter.write(ex.getLocalizedMessage()+"\n");
					resultFileWriter.write(ex.getStackTrace().toString()+"\n");
					System.out.println(jsFileNotOK);
					try {
						fis.close();
						inReader.close();
					} catch (IOException ex2) {
						DefaultExceptionLogger.getInstance().execute(ex2);
					}
				}
			}
			try {
				resultFileWriter.close();
			} catch (IOException ex2) {
				DefaultExceptionLogger.getInstance().execute(ex2);
			}
		}
		System.out.println("\n\n ------------- \n\n");
	}
}
