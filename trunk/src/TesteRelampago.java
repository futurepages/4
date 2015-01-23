
//import com.yahoo.platform.yui.compressor.DefaultErrorReporter;
//import com.yahoo.platform.yui.compressor.JavaScriptCompressor;
import com.yahoo.platform.yui.compressor.JarClassLoader;
import com.yahoo.platform.yui.compressor.JavaScriptCompressor;
import com.yahoo.platform.yui.compressor.YUICompressor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import org.futurepages.core.resource.FpgErrorReporter;

/**
 *
 * @author leandro
 */
public class TesteRelampago {

	public static void main(String[] args) {
		FileInputStream fis = null;
		InputStreamReader inReader = null;
			System.setProperty("file.encoding", "UTF-8");
			System.setProperty("sun.jnu.encoding", "UTF-8");
		try {
			File fIn  = new File("E:\\Users\\leandro\\Documents\\Workspaces\\netbeans\\YUICompressor\\src\\in.js");
			File fOut = new File("E:\\Users\\leandro\\Documents\\Workspaces\\netbeans\\YUICompressor\\src\\out.js");
			System.out.println("COMPACTANDO " + fIn.getAbsolutePath() + "...");
			fis = new FileInputStream(fIn);
			System.out.println(System.getProperty("file.encoding"));
			inReader = new InputStreamReader(fis);
			JavaScriptCompressor compressor = new JavaScriptCompressor(inReader, new FpgErrorReporter());
			FileWriter fileWriter = new FileWriter(fOut);
			compressor.compress(fileWriter, Integer.MAX_VALUE, true, true, true, false);
			fileWriter.close();
			fis.close();
			inReader.close();
			System.out.println("COMPACTADO: " + fIn.getAbsolutePath() + "\n -----> "+fOut.getAbsolutePath());
		} catch (Exception ex) {
			System.out.println("ERRO AO COMPACTAR ARQUIVO JS");
			ex.printStackTrace();
			try {
				fis.close();
				inReader.close();
			} catch (IOException ex2) {
				ex2.printStackTrace();
			}
		}
	}
}