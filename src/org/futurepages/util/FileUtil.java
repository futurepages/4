package org.futurepages.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.futurepages.core.exception.DefaultExceptionLogger;

/**
 * Encapsulates all file operations.
 */
public class FileUtil {

	private static FileUtil instance;

	private FileUtil() {}

	public static FileUtil getInstance() {
		if (instance == null) {
			instance = new FileUtil();
		}
		return instance;
	}

	public static String getStringContent(String path) throws FileNotFoundException, IOException {
		File file = new File(path);
		return getStringContent(file);
	
	}

	public static String getStringContent(File file) throws FileNotFoundException, IOException {
		if (!file.exists()) {
			return null;
		}

		FileReader fr = new FileReader(file);
		char[] buffer = new char[(int) file.length()];
		fr.read(buffer);
		fr.close();
		//antes era new String(buffer); //como tivemos problema com utf-8, que criou-se caracteres \0, trocamos.
		return buildString(buffer);
	}

	//MÉTODO SOB OBSERVAÇÃO!!
	public static String buildString(char[] buffer){
		StringBuilder sb = new StringBuilder();
		for(int i = 0 ; i < buffer.length && buffer[i]!='\0' ; i++){
			sb.append(buffer[i]);
		}
		return sb.toString();
	}

    public static byte[] getBytesFromUrl(String url, StringBuilder sbContentType, Long maxAllowed)  throws IOException {
		ByteArrayOutputStream bais = new ByteArrayOutputStream();
		InputStream is = null;
		URL theURL = new URL(url.replace(" ","%20").replace("+","%20"));
        URLConnection c =  theURL.openConnection();
		try {
		  if(sbContentType!=null){
			sbContentType.append(c.getContentType());
		  }
		  if(maxAllowed!=null){
			if(maxAllowed.longValue() < Long.parseLong(c.getHeaderField("Content-Length"))){
				return null;
			}
		  }
	      is = c.getInputStream();
		  byte[] byteChunk = new byte[4096];
		  int n;
		  while ( (n = is.read(byteChunk)) > 0 ) {
			bais.write(byteChunk, 0, n);
		  }
		  return bais.toByteArray();
		}
		catch (IOException e) {
		  throw e;
		}
		finally {
		  if (is != null) {
			  bais.close();
			  is.close();
		  }
		}
    }

	public static String getStringContent(Class cls, String path) throws FileNotFoundException, IOException {
		return getStringContent(classRealPath(cls)+path);
	}

	public static String[] getStringLines(Class cls, String path) throws FileNotFoundException, IOException {
		return getStringLines(classRealPath(cls)+"/"+path);
	}


	public static String[] getStringLines(String path) throws FileNotFoundException, IOException {
		return getStringLines(new File(path));
	}

	public static String[] getStringLines(File file) throws FileNotFoundException, IOException {
		return getStringContent(file).split("[\r\n|\n]");
	}

	public static void putKeyValue(Map<String, String> map, URL srcURL, String targetPath) throws Exception {
		putStrings(map, srcURL, targetPath);
	}

	public static void putStrings(Map<String, String> map, URL sourceUrl, String targetUrl) throws Exception {
		BufferedInputStream source = new BufferedInputStream(sourceUrl.openStream());
		BufferedOutputStream target = null;
		try {
			byte[] content = replaceAll(map, source,false).getBytes();
			target = new BufferedOutputStream(new FileOutputStream(targetUrl));
			target.write(content);
			target.flush();
		} catch (Exception e) {
			DefaultExceptionLogger.getInstance().execute(e);
		} finally {
			if (target != null) {
				target.close();
			}
		}
	}

	private static String replaceAll(Map<String, String> map, InputStream source, boolean regexKey) throws IOException {
		try {
			if (source == null) {
				throw new Exception(new FileNotFoundException("File doesn't exist."));
			}
			byte[] content = new byte[source.available()];
			source.read(content);
			String layout = new String(content);

			for (String key : map.keySet()) {
				if(regexKey){
					layout = layout.replaceAll(key, map.get(key));
				}else{
					layout = StringUtils.replace(layout, key, map.get(key));
				}
			}
			return layout;
		} catch (Exception e) {
			return "";
		} finally {
			if (source != null) {
				source.close();
			}
		}
	}

	/**
	 * Puts the given items into the given key's place by replacing it.
	 */
	public static void putKeyValue(String key, String value, String sourceUrl) throws Exception {
		putKeyValue(key, value, sourceUrl, sourceUrl);
	}

	public static void putKeyValue(Map<String, String> map, String sourceUrl) throws Exception {
		putKeyValue(map, sourceUrl, sourceUrl);
	}

	/**
	 * Puts the given items into the given key's place by replacing it.
	 * @param key
	 * @param value
	 * @param sourceUrl
	 * @param targetUrl
	 * @throws Exception
	 */
	public static void putKeyValue(String key, String value, String sourceUrl, String targetUrl) throws Exception {
		Map<String, String> strings = new HashMap<String, String>();
		strings.put(key, value);
		putStrings(strings, sourceUrl, targetUrl);
	}

	public static void putKeyValue(Map<String, String> map, String sourceUrl, String targetUrl) throws Exception {
		putStrings(map, sourceUrl, targetUrl);
	}

	/**
	 * Replaces the file keys with the given key-value map.
	 * where the key is not a regex, it's a plain text.
	 */
	public static String replaceAll(Map<String, String> map, String sourceUrl) throws Exception {
		return replaceAll(map, new FileInputStream(sourceUrl), false);
	}

	/**
	 * Replaces the file keys with the given key-value map.
	 * @param map
	 * @param sourceUrl
	 * @param regexKey - true when trying to replace using key as a regex pattern
	 * @return String
	 * @throws Exception
	 */
	public static String replaceAll(Map<String, String> map, String sourceUrl, boolean regexKey) throws Exception {
		return replaceAll(map, new FileInputStream(sourceUrl), regexKey);
	}

	/**
	 * Puts the given strings into the file.
	 * @param map
	 * @param sourceUrl
	 * @param targetUrl
	 * @throws Exception
	 */
	public static void putStrings(Map<String, String> map, String sourceUrl, String targetUrl) throws Exception {
		putStrings(map, sourceUrl, targetUrl, false);
	}

	public static void putStrings(Map<String, String> map, String sourceUrl, String targetUrl, boolean regexKey) throws Exception {
		FileOutputStream target = null;
		try {
			byte[] content = replaceAll(map, sourceUrl,regexKey).getBytes();
			target = new FileOutputStream(targetUrl);
			target.write(content);
		} catch (Exception e) {
			DefaultExceptionLogger.getInstance().execute(e);
		} finally {
			if (target != null) {
				target.close();
			}
		}
	}

	public static void createTextFile(String content, String targetUrl) throws Exception {
		FileOutputStream target = null;
		try {
			byte[] contentBytes = content.getBytes();
			target = new FileOutputStream(targetUrl);
			target.write(contentBytes);
		} catch (Exception e) {
			DefaultExceptionLogger.getInstance().execute(e);
		} finally {
			if (target != null) {
				target.close();
			}
		}
	}

	public static boolean deleteFile(String pathArquivo) {
		boolean success = (new File(pathArquivo)).delete();
		return success;
	}

	public static void moveFileToDirectory(String pathArquivo, String pathDiretorio) throws Exception {
		// File (or directory) to be moved
		File file = new File(pathArquivo);

		// Destination directory
		File dir = new File(pathDiretorio);

		// Move file to new directory
		boolean success = file.renameTo(new File(dir, file.getName()));
		if (!success) {
			throw new Exception("Não foi possível renomear/mover arquivo.");
		}
	}

	public static String extensionFormat(String pathFileName) {
		String[] explodedName = pathFileName.split("\\.");
		return explodedName[explodedName.length - 1].toLowerCase();
	}

	public static String classRealPath(Class<?> klass) throws UnsupportedEncodingException {
		return EncodingUtil.correctPath(klass.getResource("").getPath());
	}

	public static String classesPath(Class<?> klass) throws UnsupportedEncodingException {
		return EncodingUtil.correctPath(klass.getResource("/").getPath());
	}

	public static void copy(String fromFileName, String toFileName) throws IOException {

		File fromFile = new File(fromFileName);
		File toFile = new File(EncodingUtil.correctPath(toFileName));
		if (toFile.isDirectory()) {
			toFile = new File(toFile, fromFile.getName());
		}

		if (!toFile.exists()) {
			String parent = toFile.getParent();
			if (parent == null) {
				parent = System.getProperty("user.dir");
			}
		}

		FileInputStream from = null;
		FileOutputStream to = null;
		try {
			from = new FileInputStream(fromFile);
			to = new FileOutputStream(toFile);
			byte[] buffer = new byte[4096];
			int bytesRead;

			while ((bytesRead = from.read(buffer)) != -1) {
				to.write(buffer, 0, bytesRead); // write
			}
		} finally {
			if (from != null) {
				try {
					from.close();
				} catch (IOException e) {
					DefaultExceptionLogger.getInstance().execute(e);
				}
			}
			if (to != null) {
				try {
					to.close();
				} catch (IOException e) {
					DefaultExceptionLogger.getInstance().execute(e);
				}
			}
		}
	}

	/**
	 * Creates the path indicated if there isn't
	 * @param fileDir - path to create
	 * @return - True if creates path. False if path exists
	 */
	public static boolean createPath(String fileDir) {
		return createPath(new File(fileDir));
	}

	/**
	 * Creates the path indicated if there isn't
	 * @param fileDir - path to create
	 * @return - True if creates path. False if path exists
	 */
	public static boolean createPath(File fileDir) {
		if (!fileDir.exists()) {
			return fileDir.mkdirs();
		}
		return false;
	}

	/**
	 * Copies the specified folder to destination folder.
	 * @param folder - Origin folder
	 * @param destiny - Destiny folder
	 * @param showLogCopy - Show only the file name copied
	 * @param showPath - Shows the full path to the file name copied
	 * @param overwrite - Oveerwrite if file exists
	 * @throws IOException
	 */
	public static void copyFolderTo(File folder, File destiny, boolean showLogCopy, boolean showPath, boolean overwrite) throws IOException {
		copyFolderContent(folder, new File(destiny + "\\" + folder.getName()), showLogCopy, showPath, overwrite);
	}

	/**
	 * Copies content from origin folder to destination folder.
	 * @param origin - Origin folder
	 * @param destiny - Destiny folder
	 * @param showLogCopy - Show only the file name copied
	 * @param showPath - Shows the full path to the file name copied
	 * @param overwrite - Oveerwrite if file exists
	 * @throws IOException
	 */
	public static void copyFolderContent(File origin, File destiny, boolean showLogCopy, boolean showPath, boolean overwrite) throws IOException {
		createPath(destiny);
		if (!origin.isDirectory()) {
			throw new UnsupportedOperationException("Origin must be a directory");
		}
		if (!destiny.isDirectory()) {
			throw new UnsupportedOperationException("Destiny must be a directory");
		}
		if (destiny.equals(origin)) {
			throw new UnsupportedOperationException("Origin and Destiny are the same directory");
		}
		if (destiny.getPath().regionMatches(true, 0, origin.getAbsolutePath(), 0, origin.getAbsolutePath().length())){
			throw new UnsupportedOperationException("Destiny folder is a subfolder of origin folder");
		}

		File[] files = origin.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				copyFolderContent(file, new File(destiny + "\\" + file.getName()), showLogCopy, showPath, overwrite);
			} else {
				String msgLog = "";
				File newFile = new File(destiny + "\\" + file.getName());
				if (overwrite || !newFile.exists()) {
					FileUtil.copy(file.getAbsolutePath(), newFile.getAbsolutePath());
					if (showLogCopy) {
						msgLog = ((overwrite)?"Overwriting":"Copying")+" file: " + ((showPath)?file.getAbsolutePath():file.getName());
						System.out.println(msgLog);
					}
				}
			}
		}
	}
	
	public static String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String we use the BufferedReader.readLine()
		 * method. We iterate until the BufferedReader return null which means
		 * there's no more data to read. Each line will appended to a StringBuilder
		 * and returned as String.
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
		} catch (IOException e) {
			DefaultExceptionLogger.getInstance().execute(e);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				DefaultExceptionLogger.getInstance().execute(e);
			}
		}

		return sb.toString();
	}

    public static void writeFile(final URL fromURL, final File toFile)
            throws IOException {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new BufferedInputStream(fromURL.openStream());
            out = new BufferedOutputStream(new FileOutputStream(toFile));
            int len;
            byte[] buffer = new byte[4096];
            while ((len = in.read(buffer, 0, buffer.length)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
        } finally {
            in.close();
            out.close();
        }
    }

    public static void writeFile(final URL fromURL, final String toFile)
            throws IOException {
        writeFile(fromURL, new File(toFile));
    }

	class PatternFileParser extends FileParser<File>{
		private Pattern pattern = null;


		public PatternFileParser(String patternRegex) {
			if(!Is.empty(patternRegex) ){
				this.pattern = Pattern.compile(patternRegex);
			}
		}

		@Override
		File parse(File file){
			if(this.pattern!=null){
				Matcher m = pattern.matcher(file.getName());
				if(m.matches()){
					return file;
				}
			}
			return null;
		}
	}

	/**
	 * @return all the files from the directory 'directory'
	 */
	public static ArrayList<File> listFilesFromDirectory(File directory, boolean recursive) {
		return new ArrayList<File>(FileUtil.getInstance().filesFromDirectory(directory, recursive, ".*"));
	}

	/**
	 * @return all the files from the directory 'directory' which matches with 'pattern'
	 */
	public Collection<File> filesFromDirectory(File directory, boolean recursive, String pattern) {
		return listResourcesFromDirectory(directory, new PatternFileParser(pattern), recursive);
	}

	public static ArrayList<File> listFilesFromDirectory(File directory, boolean recursive, String pattern) {
		final Collection<File> collection = FileUtil.getInstance().filesFromDirectory(directory, recursive, pattern);
		return new ArrayList<File>(collection);
	}

	<T extends Object> Collection<T> listResourcesFromDirectory(File directory, FileParser<T> parser, boolean recursive) {
		Set<T> files = new HashSet<T>();
		if(directory != null){
			resourcesFromDirectory(directory, parser, recursive, files);
		}
		return files;
	}

	private <T extends Object> void resourcesFromDirectory(File directory, FileParser<T> parser, boolean recursive, Set<T> resources) {

		if(directory.exists()){

			if(directory.isFile()){
				T parsed = parser.parse(directory);
				if( parsed != null ){
					resources.add(parsed);
				}

			}else{
				final File[] subFiles = directory.listFiles();
				for (File file : subFiles) {
					if(!file.isFile()){
						if(recursive){
							resourcesFromDirectory(file, parser, recursive, resources);
						}
					} else{
						resourcesFromDirectory(file, parser, recursive, resources);
					}
				}
			}
		}
	}

	public File getSubFile(File file, String name) {
		if(file!=null && file.isDirectory()){
			if(name.startsWith("/") || name.startsWith("\\")){
				name = name.substring(1);
			}
			file = new File(file.getAbsolutePath() + "/"+ name);
			if(file.exists()){
				return file;
			}
		}
		return null;
	}
}