package org.futurepages.util;

import java.io.File;
import java.io.IOException;

public class FileCopyUtils {

	private static String SEPARATOR = File.separator;

	public static void cleanCopy(String source, String target) throws IOException {
		File origem = new File(source);
		cleanCopy("", target, origem.listFiles());
		removeDeleted(source, target);
	}

	public static void copy(String source, String target) throws IOException {
		File origem = new File(source);
		cleanCopy("", target, origem.listFiles());
	}

	public static void delete(String source, String target) throws IOException {
		File origem = new File(source);
		delete("", target, origem.listFiles());
	}

	private static void delete(String deep, String target, File... contents) throws IOException {

		for (File origem : contents) {
			if (origem.isFile()) {
				File file = new File(target + SEPARATOR + deep);
				if (file.exists()) {
					file.delete();
				}
			} else {
				String nomeFile = origem.getName();
				if (!nomeFile.startsWith(".")) {
					delete(deep + SEPARATOR + nomeFile, target, origem.listFiles());
				}
			}
		}
	}

	private static void cleanCopy(String deep, String target, File... contents) throws IOException {

		for (File origem : contents) {
			if (origem.isFile()) {
				copy(origem, target + SEPARATOR + deep);
			} else {
				String nomeFile = origem.getName();
				if (!nomeFile.startsWith(".")) {
					cleanCopy(deep + SEPARATOR + nomeFile, target, origem.listFiles());
				}
			}
		}
	}

	private static void copy(File origem, String dest) throws IOException {
		File file = new File(dest);
		if (!file.exists()) {
			file.mkdirs();
		}
		FileUtil.copy(origem.getAbsolutePath(), dest);
	}

	private static void removeDeleted(String origem, String target) throws IOException {
		File destino = new File(target);
		removeDeleted("", origem, destino);
	}

	private static void removeDeleted(String sufixo, String source, File destino) throws IOException {
		File origem = new File(source + SEPARATOR + sufixo);
		if (!destino.getName().startsWith(".")) {
			boolean temDestino = destino.exists();
			boolean naoTemOrigem = !origem.exists();
			if (temDestino && naoTemOrigem) {
				if (destino.isDirectory()) {
					removeFileTree(destino);
				}
				destino.delete();

			} else {
				if (destino.isDirectory()) {
					for (File filho : destino.listFiles()) {
						String novoSuf = filho.getName();
						removeDeleted(novoSuf, origem.getAbsolutePath(), filho);
					}
				}
			}
		}
	}

	private static void removeFileTree(File file) {
		File[] files = file.listFiles();
		if (files.length > 0) {
			for (File subFile : files) {
				if (subFile.isDirectory()) {
					removeFileTree(subFile);
				}
				subFile.delete();
				subFile.delete();
			}
		}
	}
}
