package org.futurepages.menta.core.i18n;

import org.futurepages.menta.core.control.AbstractApplicationManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

public class LocaleManager {

	public static final String LOCALE_KEY = "locale";
	private static String DEFAULT_DIR = "i18n";
	private static String dir = DEFAULT_DIR;
	private static List<Locale> locales = new ArrayList<Locale>(20);
	private static final String SEP = File.separator;
	private static String master = DEFAULT_DIR + SEP + "master"; // i18n/master
	private static boolean useMasterForEverything = false;
	public static Locale DEFAULT_LOCALE = new Locale("pt", "BR");
	private static String defaultDateMask = "MM/dd/yyyy";
	private static String defaultTimeMask = "HH:mm";
	private static Map<Locale, String> dateMasks = new HashMap<Locale, String>();
	private static Map<Locale, String> timeMasks = new HashMap<Locale, String>();
	private static boolean masterChanged = false;
//	private static Thread thread = null;
//	private static volatile boolean running;
	private static Set<Locale> dirLocales = new HashSet<Locale>();
//	private static long SCAN_TIME = 1000 * 60 * 5;
	private static final Map<String, SimpleDateFormat> simpleDateFormatters = new HashMap<String, SimpleDateFormat>();

	static {

		setDateMask(new Locale("en"), "MM/dd/yyyy");
		setDateMask(new Locale("en", "US"), "MM/dd/yyyy");
		setDateMask(new Locale("pt"), "dd/MM/yyyy");
		setDateMask(new Locale("pt", "BR"), "dd/MM/yyyy");
	}
	private static final Map<String, SimpleDateFormat> simpleTimeFormatters = new HashMap<String, SimpleDateFormat>();

	private static Locale checkLocale(Locale loc) {

		if (loc == null) {
			return null;
		}

		Locale first = null;

		synchronized (dirLocales) {

			Iterator<Locale> iter = dirLocales.iterator();

			while (iter.hasNext()) {

				Locale l = iter.next();

				if (first == null) {
					first = l;
				}

				if (l.equals(loc)) {

					return l;
				}
			}

			iter = dirLocales.iterator();

			while (iter.hasNext()) {

				Locale l = iter.next();

				if (l.getLanguage().equals(loc.getLanguage())) {

					return l;
				}
			}
		}

		return first;
	}

	public static Set<Locale> scanLocales(String dir) throws Throwable {

		return scanLocales(dir, null);
	}

	public static Set<Locale> scanLocales(String dir, String filename) throws Throwable {

		StringBuilder sb = new StringBuilder(128);

		if (AbstractApplicationManager.getRealPath() == null) {
			return null;
		}

		sb.append(AbstractApplicationManager.getRealPath());

		if (dir.startsWith("/") && dir.length() > 1) {

			sb.append(dir);

		} else if (!dir.equals("")) {

			sb.append(File.separator).append(dir);
		}

		File file = new File(sb.toString());

		if (!file.exists() || !file.isDirectory()) {
			return null;
		}

		String[] files = file.list();

		Set<Locale> set = new HashSet<Locale>();

		if (filename != null && !filename.endsWith("_")) {
			filename += "_";
		}

		for (int i = 0; i < files.length; i++) {

			if (files[i].endsWith(".i18n")) {

				if (filename != null && !files[i].startsWith(filename)) {
					continue;
				}

				int lastIndex = files[i].lastIndexOf(".");

				String[] s = files[i].substring(0, lastIndex).split("_");

				int size = s.length;

				if (size < 2) {
					continue;
				}

				if (size >= 3 && s[size - 1].length() == 2 && s[size - 2].length() == 2) {

					Locale l = new Locale(s[size - 2], s[size - 1]);

					set.add(l);

				} else if (size == 2 && s[size - 1].length() == 2) {

					Locale l = new Locale(s[size - 1]);

					set.add(l);
				}
			}
		}

		return set;
	}

	/**
	 * @since 1.1.2
	 */
	public static void setUseMasterForEverything(boolean useMaster) {
		useMasterForEverything = useMaster;
	}

	/**
	 * @since 1.12
	 */
	public static void useMasterI18N(boolean useMaster) {

		setUseMasterForEverything(useMaster);
	}

	private static String cutSlashes(String name) {

		if (name.startsWith("/") && name.length() > 1) {

			name = name.substring(1, name.length());

		}

		if (name.endsWith("/") && name.length() > 1) {

			name = name.substring(0, name.length() - 1);
		}

		return name;
	}

	/**
	 * Set the directory from where to look for i18n files.
	 *
	 * Default directory is "i18n" on the root dir. ("/i18n")
	 *
	 * To change for WEB-INF/classes/i18n you can call:
	 *
	 * LocaleManager.setDir("WEB-INF/classes/i18n");
	 *
	 * Notice that this method will also change the location of the master file,
	 * if and only if you never called the setMaster method!
	 *
	 * @param directory The new directory for the i18n files.
	 * @since 1.9
	 */
	public static void setDir(String directory) {

		directory = directory.replace('\\', '/');

		String newDir = cutSlashes(directory);

		String oldDir = dir;

		if (!masterChanged) {

			// move the master to the new directory as well...

			master = master.replaceFirst(oldDir, newDir);

		}

		dir = newDir;
	}

	/**
	 * Return the current dir for i18n files.
	 *
	 * @return The current dir
	 * @since 1.9
	 */
	public static String getDir() {

		return dir;
	}

	/**
	 * @since 1.1.2
	 */
	public static boolean isUseMasterForEverything() {
		return useMasterForEverything;
	}

	public static void add(Locale loc) {

		Iterator<Locale> iter = locales.iterator();

		while (iter.hasNext()) {
			Locale l = iter.next();
			if (l.equals(loc)) {
				return;
			}
		}

		locales.add(loc);
	}

	public static void setMaster(String s) {

		masterChanged = true;

		master = s.replace('\\', '/');

		if (master.startsWith("/") && master.length() > 1) {
			master = master.substring(1);
		}
	}

	public static String getMaster() {
		return master;
	}

	public static void add(String loc) {
		Locale l = getLocaleFromString(loc);
		add(l);
	}

	public static Locale getDefaultLocale() {
		if (locales.size() == 0) {
			return DEFAULT_LOCALE;
		}
		return locales.get(0);
	}

	public static Locale[] getLocales() {
		Locale[] locs = new Locale[locales.size()];
		locales.toArray(locs);
		return locs;
	}

	public static int[] getLocaleIds() {
		int[] ids = new int[locales.size()];
		for (int i = 0; i < locales.size(); i++) {
			ids[i] = i;
		}
		return ids;
	}

	public static Locale getLocaleFromString(String text) {
		StringTokenizer st = new StringTokenizer(text, "_");
		if (st.countTokens() == 1) {
			return new Locale(st.nextToken());
		} else if (st.countTokens() == 2) {
			return new Locale(st.nextToken(), st.nextToken());
		} else if (st.countTokens() == 3) {
			return new Locale(st.nextToken(), st.nextToken(), st.nextToken());
		}
		return null;
	}

	public static boolean isSupportedLocale(Locale loc) {
		Iterator<Locale> iter = locales.iterator();
		while (iter.hasNext()) {
			Locale l = iter.next();
			if (l.equals(loc)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isSupportedLocale(String text) {
		Locale loc = getLocaleFromString(text);
		if (loc == null) {
			return false;
		}
		return isSupportedLocale(loc);
	}

	public static Locale getLocale(int id) {
		if (id >= locales.size()) {
			return null;
		}
		return locales.get(id);
	}

	public static int getId(Locale loc) {
		for (int i = 0; i < locales.size(); i++) {
			if (locales.get(i).equals(loc)) {
				return i;
			}
		}
		return -1;
	}

	public static Locale getSupportedLocale(Locale loc) {

		if (isSupportedLocale(loc)) {
			return loc;
		}

		Locale onlyLang = new Locale(loc.getLanguage());

		Iterator<Locale> iter = locales.iterator();
		while (iter.hasNext()) {
			Locale l = iter.next();
			if (l.equals(onlyLang)) {
				return l;
			}
		}

		iter = locales.iterator();
		while (iter.hasNext()) {
			Locale l = iter.next();
			if (l.getLanguage().equals(loc.getLanguage())) {
				return l;
			}
		}

		Locale autoScan = checkLocale(loc);

		if (autoScan != null) {
			return autoScan;
		}

		return getDefaultLocale();
	}

	public static Locale getLocale(HttpServletRequest req) {
		return getLocale(req, true);
	}

	public static Locale getLocale(HttpServletRequest req, boolean onlySupported) {
		Locale loc = null;
		HttpSession session = req.getSession(true);
		Object obj = session.getAttribute(LOCALE_KEY);
		if (obj != null && obj instanceof Locale) {
			loc = (Locale) obj;
		} else {
			loc = req.getLocale();
		}
		if (onlySupported) {
			return getSupportedLocale(loc);
		} else {
			return loc;
		}
	}

	public static void setDefaultDateMask(String defaultDateMask) {

		LocaleManager.defaultDateMask = defaultDateMask;
	}

	public static String getDefaultDateMask() {

		return defaultDateMask;
	}

	public static void setDateMask(Locale loc, String dateMask) {

		dateMasks.put(loc, dateMask);
	}

	public static String getDateMask(Locale loc) {

		String s = dateMasks.get(loc);

		if (s == null) {
			return defaultDateMask;
		}

		return s;
	}

	public static void setDefaultTimeMask(String defaultTimeMask) {

		LocaleManager.defaultTimeMask = defaultTimeMask;
	}

	public static String getDefaultTimeMask() {

		return defaultTimeMask;
	}

	public static void setTimeMask(Locale loc, String timeMask) {

		timeMasks.put(loc, timeMask);
	}

	public static String getTimeMask(Locale loc) {

		String s = timeMasks.get(loc);

		if (s == null) {
			return defaultTimeMask;
		}

		return s;
	}

	public static SimpleDateFormat getSimpleDateFormat(String mask) {

		synchronized (simpleDateFormatters) {

			SimpleDateFormat f = simpleDateFormatters.get(mask);

			if (f != null) {
				return f;
			}

			f = new SimpleDateFormat(mask);

			simpleDateFormatters.put(mask, f);

			return f;
		}
	}

	public static SimpleDateFormat getSimpleDateFormat(Locale loc) {

		String mask = getDateMask(loc);

		if (mask == null) {
			return null;
		}

		return getSimpleDateFormat(mask);
	}

	public static SimpleDateFormat getSimpleTimeFormat(String mask) {

		synchronized (simpleTimeFormatters) {

			SimpleDateFormat f = simpleTimeFormatters.get(mask);

			if (f != null) {
				return f;
			}

			f = new SimpleDateFormat(mask);

			simpleTimeFormatters.put(mask, f);

			return f;
		}
	}

	public static SimpleDateFormat getSimpleTimeFormat(Locale loc) {

		String mask = getTimeMask(loc);

		if (mask == null) {
			return null;
		}

		return getSimpleTimeFormat(mask);
	}
}
