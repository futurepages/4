package org.futurepages.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import org.futurepages.core.exception.AppLogger;

public class EncodingUtil {

	public static String getSystemEncoding() {
		return System.getProperty("file.encoding");
	}

	public static String correctPath(String wrongPath) throws UnsupportedEncodingException {
		return URLDecoder.decode(wrongPath, EncodingUtil.getSystemEncoding());
	}

	public static void transform(File source, String srcEncoding, File target, String tgtEncoding) throws IOException {
		BufferedReader br = null;
		BufferedWriter bw = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(source), srcEncoding));
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(target), tgtEncoding));
			char[] buffer = new char[16384];
			int read;
			while ((read = br.read(buffer)) != -1) {
				bw.write(buffer, 0, read);
			}
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} finally {
				if (bw != null) {
					bw.close();
				}
			}
		}
	}

	public static String convertOutputString(String source, String charset) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		OutputStreamWriter out = new OutputStreamWriter(baos, charset);
		out.write(source);
		out.close();
		return baos.toString();
	}

	public static String convertInputString(String input, String charset)
			throws IOException {
		int CHAR_BUFFER_SIZE = 8096;
		ByteArrayInputStream bais = new ByteArrayInputStream(input.getBytes());
		InputStreamReader in = new InputStreamReader(bais, charset);
		StringBuilder output = new StringBuilder();
		char buff[] = new char[CHAR_BUFFER_SIZE];
		int count = 0;
		while ((count = in.read(buff, 0, CHAR_BUFFER_SIZE)) > 0) {
			output.append(buff, 0, count);
		}
		in.close();
		bais.close();
		return output.toString();
	}

	public static String convert(String str, String fromCharset, String toCharset) throws IOException {
		if (str == null) {
			return "";
		}
		str = convertInputString(str, fromCharset);
		return convertOutputString(str, toCharset);
	}

	public static String decodeUrl(String strIn) {
		if (strIn != null) {
			try {
				String decodedUrl = URLDecoder.decode(strIn, "UTF-8");
				return decodedUrl.replaceAll("&frasl;", "/"); //bug - tomcat não reconhece barra.
			} catch (Exception ex) {
				AppLogger.getInstance().execute(ex);
			}
		}
		return null;
	}

	public static String encodeUrl(String strIn) {
		if (strIn != null) {
			try {
				return URLEncoder.encode(strIn, "UTF-8");
			} catch (Exception ex) {
				AppLogger.getInstance().execute(ex);
			}
		}
		return null;
	}

	public static String toISO(String str) {
		if (str != null) {
			try {
				return convert(str, "UTF-8", "ISO-8859-1");
			} catch (IOException ex) {
				AppLogger.getInstance().execute(ex);
			}
		}
		return null;
	}

	public static String toUTF8(String str) {
		if (str != null) {
			try {
				return convert(str, "ISO-8859-1", "UTF-8");
			} catch (IOException ex) {
				AppLogger.getInstance().execute(ex);
			}
		}
		return null;
	}

	//Migração de projeto de ISO para UTF-8
	public static void main(String[] args) throws Exception {
		String path = "E:\\Users\\leandro\\Documents\\Workspaces\\Idea\\Projects\\intranet";
		String pathWEB = path + "\\web";
//		String pathSRC = path + "\\src";
//		String pathTEST = path + "\\test";

		migratingISOtoUTF8(pathWEB, ".*\\.tag");
//		migratingISOtoUTF8(pathWEB, ".*\\.jsp");
//		migratingISOtoUTF8(pathWEB, ".*\\.sql");
//		migratingISOtoUTF8(pathWEB, ".*\\.htm");
//		migratingISOtoUTF8(pathWEB, ".*\\.html");
//		migratingISOtoUTF8(pathWEB, ".*\\.js");
//		migratingISOtoUTF8(pathWEB, ".*\\.css");
//		migratingISOtoUTF8(pathWEB, ".*\\.txt");
//		migratingISOtoUTF8(pathWEB, ".*\\.xml");
//		migratingISOtoUTF8(pathWEB, "[^\\.]*");


//		migratingISOtoUTF8(pathSRC, ".*\\.sql");
//		migratingISOtoUTF8(pathSRC, ".*\\.txt");
//		migratingISOtoUTF8(pathSRC, ".*\\.properties");
//		migratingISOtoUTF8(pathSRC, "SelecionadoDao.java");
//		migratingISOtoUTF8(pathSRC, ".*\\.htm");
//		migratingISOtoUTF8(pathSRC, ".*\\.html");
//		migratingISOtoUTF8(pathSRC, "[^\\.]*");
		
//		migratingISOtoUTF8(pathTEST, ".*\\.java");
//		migratingISOtoUTF8(pathTEST, ".*\\.txt");
//		migratingISOtoUTF8(pathTEST, ".*\\.properties");
//		migratingISOtoUTF8(pathTEST, ".*\\.htm");
//		migratingISOtoUTF8(pathTEST, ".*\\.html");
//		migratingISOtoUTF8(pathTEST, "[^\\.]*");
	}

	public static void migratingISOtoUTF8(String path, String patternRegex) throws IOException {
		System.out.println("");
		System.out.println("================== " + patternRegex + " =========================");
		List<File> files = FileUtil.listFilesFromDirectory(new File(path), true, patternRegex);
		for (File file : files) {
			if (!file.isDirectory()&&!file.getAbsolutePath().contains("\\.svn\\")) {
				File tempFile = File.createTempFile("88591", ".tmp");
				EncodingUtil.transform(file, "ISO-8859-1", tempFile, "UTF-8");
				FileUtil.copy(tempFile.getAbsolutePath(), file.getAbsolutePath());
				System.out.println(file.getAbsolutePath());
			}
		}
	}
	public static void migratingUTF8toISO(String path, String patternRegex) throws IOException {
		System.out.println("");
		System.out.println("================== " + patternRegex + " =========================");
		List<File> files = FileUtil.listFilesFromDirectory(new File(path), true, patternRegex);
		for (File file : files) {
			if (!file.isDirectory()&&!file.getAbsolutePath().contains("\\.svn\\")) {
				File tempFile = File.createTempFile("UTF8", ".tmp");
				EncodingUtil.transform(file, "UTF-8", tempFile, "ISO-8859-1");
				FileUtil.copy(tempFile.getAbsolutePath(), file.getAbsolutePath());
				System.out.println(file.getAbsolutePath());
			}
		}
	}
}
