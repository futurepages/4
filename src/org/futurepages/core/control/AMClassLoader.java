package org.futurepages.core.control;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.futurepages.core.exception.DefaultExceptionLogger;

/**
 * @author Sergio Oliveira
 */
class AMClassLoader extends ClassLoader {

	private static final char SEP = File.separatorChar;
	private static Map<File, Class> classes = new HashMap<File, Class>();
	private static Map<File, Long> timestamps = new HashMap<File, Long>();

	public AMClassLoader() {
		super(AMClassLoader.class.getClassLoader());
	}

	@Override
	public Class loadClass(String className) throws ClassNotFoundException {
		boolean isJava = className.startsWith("java.") || className.startsWith("javax.") || className.startsWith("sun.");
		if (!isJava) {
			Class klass = findClass(className);
			if (klass != null) {
				return klass;
			}
		}
		return super.loadClass(className);
	}

	@Override
	public Class findClass(String className) {
		try {
			StringBuffer sb = new StringBuffer(150);
			sb.append(AbstractApplicationManager.getRealPath());
			sb.append(SEP).append("WEB-INF").append(SEP).append("classes").append(SEP);
			sb.append(className.replace('.', SEP)).append(".class");

			File f = new File(sb.toString());
			if (!f.exists()) {
				return null;
			}

			long last = f.lastModified();
			long ts = 0;
			if (timestamps.containsKey(f)) {
				Long l = timestamps.get(f);
				ts = l.longValue();
			}
			Class amKlass = null;
			if (classes.containsKey(f)) {
				amKlass = classes.get(f);
			}

			if (ts == last && amKlass != null) {
				return amKlass;
			}

			timestamps.put(f, new Long(last));


			byte[] data = loadClassData(f);
			amKlass = defineClass(className, data, 0, data.length, null);
			classes.put(f, amKlass);
			return amKlass;

		} catch (Exception e) {
			DefaultExceptionLogger.getInstance().execute(e);
		}
		return null;
	}

	private byte[] loadClassData(File f) throws IOException {
		int size = (int) f.length();
		byte[] buff = new byte[size];
		FileInputStream fis = new FileInputStream(f);
		DataInputStream dis = new DataInputStream(fis);
		dis.readFully(buff);
		dis.close();
		return buff;
	}
}
