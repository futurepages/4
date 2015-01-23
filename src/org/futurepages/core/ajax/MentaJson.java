package org.futurepages.core.ajax;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.futurepages.core.exception.DefaultExceptionLogger;

/**
 * 
 * @author Robert Willian
 * 
 * This class get instance of net.sf.json.JSONArray or net.sf.json.JSONObject
 * with main properties changed from
 * ArrayList to LinkedList and HashMap to LinkedHashMap
 */
public class MentaJson {

	/**
	 * 
	 * @return net.sf.json.JSONArray
	 */
	@SuppressWarnings("unchecked")
	public static JSONArray getJSONArray() {

		JSONArray array = new JSONArray();

		try {

			Field elements = array.getClass().getDeclaredField("elements");
			elements.setAccessible(true);
			elements.set(array, new LinkedList());

		} catch (Throwable e) {
			DefaultExceptionLogger.getInstance().execute(e);
		}

		return array;

	}

	/**
	 * 
	 * @return net.sf.json.JSONObject
	 */
	@SuppressWarnings("unchecked")
	public static JSONObject getJSONObject() {

		JSONObject obj = new JSONObject();
		Field properties;
		try {
			properties = obj.getClass().getDeclaredField("properties");
			properties.setAccessible(true);
			properties.set(obj, new LinkedHashMap());

		} catch (Throwable e) {
			DefaultExceptionLogger.getInstance().execute(e);
		}
		return obj;

	}
}
