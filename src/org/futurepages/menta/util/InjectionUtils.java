package org.futurepages.menta.util;

import org.futurepages.core.exception.AppLogger;
import org.futurepages.menta.core.i18n.LocaleManager;
import org.futurepages.menta.core.input.Input;
import org.futurepages.menta.core.output.Output;
import org.futurepages.util.CalendarUtil;
import org.futurepages.util.Is;
import org.futurepages.util.ReflectionUtil;
import org.futurepages.util.The;
import org.futurepages.util.brazil.BrazilDateUtil;
import org.futurepages.util.brazil.NumberUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 
 * @author Sergio Oliveira
 */
public class InjectionUtils {

	public static char PREFIX_SEPARATOR = '.';
	private static Map<Class<? extends Object>, Map<String, Object>> settersMaps = new HashMap<Class<? extends Object>, Map<String, Object>>();
	private static Map<Class<? extends Object>, Map<String, Object>> fieldsMaps = new HashMap<Class<? extends Object>, Map<String, Object>>();

	public static void prepareForInjection(Class<? extends Object> klass, Map<String, Object> setters, Map<String, Object> fields) {

		StringBuffer sb = new StringBuffer(32);
		Method[] methods = klass.getMethods();

		for (int i = 0; i < methods.length; i++) {
			Method m = methods[i];
			String name = m.getName();
			Class[] types = m.getParameterTypes();

			if (name.startsWith("set") && name.length() > 3 && types.length == 1) {
				String var = name.substring(3);
				if (var.length() > 1) {
					sb.delete(0, sb.length());
					sb.append(var.substring(0, 1).toLowerCase());
					sb.append(var.substring(1));
					var = sb.toString();
				} else {
					var = var.toLowerCase();
				}

				m.setAccessible(true);

				if (setters.containsKey(var)) {
					Object obj = setters.get(var);
					if (obj instanceof List) {
						List<Method> list = (List<Method>) obj;
						list.add(m);
					} else if (obj instanceof Method) {
						List<Method> list = new ArrayList<Method>();
						list.add((Method) obj);
						list.add(m);
						setters.put(var, list);
					}
				} else {
					setters.put(var, m);
				}
			}
		}

		if (fields == null) {
			return;
		}

		Field[] f = klass.getDeclaredFields();

		for (int i = 0; i < f.length; i++) {
			Field field = f[i];
			field.setAccessible(true);
			String name = field.getName();
			if (setters.containsKey(name)) {
				Object obj = setters.get(name);

				if (obj instanceof Method) {
					Method m = (Method) obj;
					Class[] types = m.getParameterTypes();
					Class type = field.getType();
					if (type.isAssignableFrom(types[0])) {
						continue; // don't choose a field when we already have
						// a method...
					}
				} else if (obj instanceof List) {
					List<Method> list = (List<Method>) obj;
					Iterator<Method> iter = list.iterator();
					boolean found = false;

					while (iter.hasNext()) {
						Method m = iter.next();
						Class[] types = m.getParameterTypes();
						Class type = field.getType();

						if (type.isAssignableFrom(types[0])) {
							found = true;
							break;
						}
					}
					if (found) {
						continue; // don't choose a field when we already have
						// a method...
					}
				}
			}
			fields.put(name, field);
		}
	}

	public static boolean checkPrimitives(Class target,
			Class<? extends Object> source) {

		if (target.equals(int.class) && source.equals(Integer.class)) {
			return true;
		}
		if (target.equals(boolean.class) && source.equals(Boolean.class)) {
			return true;
		}
		if (target.equals(byte.class) && source.equals(Byte.class)) {
			return true;
		}
		if (target.equals(short.class) && source.equals(Short.class)) {
			return true;
		}
		if (target.equals(char.class) && source.equals(Character.class)) {
			return true;
		}
		if (target.equals(long.class) && source.equals(Long.class)) {
			return true;
		}
		if (target.equals(float.class) && source.equals(Float.class)) {
			return true;
		}
		return target.equals(double.class) && source.equals(Double.class);

	}

	public static Object tryToConvert(Object source, Class targetType, Locale loc) {

		return tryToConvert(source, targetType, loc, false);
	}

	public static Object tryToConvert(Object source, Class targetType, Locale loc, boolean tryNumber) {

		String value = null;

		if (source instanceof String) {
			value = (String) source;
		} else if (tryNumber && source instanceof Number) {
			value = source.toString();
		} else {
			return null;
		}

		Object newValue = null;
		String className = targetType.getName();

		if (className.equals("int") || className.equals("java.lang.Integer")) {
			int x = -1;
			try {
				x = Integer.parseInt(value);
			} catch (Exception e) {
				return null;
			}
			newValue = new Integer(x);
		} else if (className.equals("short") || className.equals("java.lang.Short")) {
			short x = -1;
			try {
				x = Short.parseShort(value);
			} catch (Exception e) {
				return null;
			}
			newValue = new Short(x);

		} else if (className.equals("char") || className.equals("java.lang.Character")) {

			if (value.length() != 1) {
				return null;
			}

			newValue = new Character(value.charAt(0));

		} else if (className.equals("long") || className.equals("java.lang.Long")) {
			long x = -1;
			try {
				x = Long.parseLong(value);
			} catch (Exception e) {
				return null;
			}
			newValue = new Long(x);
		} else if (className.equals("float") || className.equals("java.lang.Float")) {
			float x = -1;
			try {
				x = realToFloat(value);
			} catch (Exception e) {
				return null;
			}
			newValue = new Float(x);
		} else if (className.equals("double") || className.equals("java.lang.Double")) {
			double x = -1;
			try {
				x = realToDouble(value);
			} catch (Exception e) {
				return null;
			}
			newValue = new Double(x);
		} else if (className.equals("java.math.BigDecimal")) {
			try {
				newValue = realToBigDecimal(value);
			} catch (Exception e) {
				return null;
			}
		} else if (className.equals("boolean") || className.equals("java.lang.Boolean")) {
			try {
				int x = Integer.parseInt(value);
				if (x == 1) {
					newValue = Boolean.TRUE;
				} else if (x == 0) {
					newValue = Boolean.FALSE;
				} else {
					return null;
				}
			} catch (Exception e) {
				if (value.equalsIgnoreCase("true") || value.equals("on")) {
					newValue = Boolean.TRUE;
				} else if (value.equalsIgnoreCase("false")) {
					newValue = Boolean.FALSE;
				} else {
					return null;
				}
			}
		}
		else if (className.equals("java.util.Calendar") && loc != null) {
			try {
				Date date = BrazilDateUtil.parseView(value);
				Calendar calendar = CalendarUtil.now();
				calendar.setTime(date);
				newValue = calendar;
			} catch (Exception e) {
				return null;
			}
		} else if (className.equals("java.util.Date") && loc != null && LocaleManager.getDateMask(loc) != null) {
			try {
				newValue = BrazilDateUtil.parseView(value);
			} catch (Exception e) {
				return null;
			}
		} else if (className.equals("java.util.Date") && loc != null) {
			try {
				newValue = BrazilDateUtil.parseView(value);
			} catch (Exception e) {
				return null;
			}
		} //byLeandro
		else if (targetType.isEnum()) {
			try {
				newValue = Enum.valueOf(targetType, value);
			} catch (Exception e) {
				return null;
			}
		}
		return newValue;
	}

	public static Object trimValue(Object source) {
		if (source.getClass().equals(String.class)) {
			source = trimValue((String) source);
		}
		return source;
	}

	public static Object trimValue(String source) {
		String cleanValue = source.trim();
		if (cleanValue.length() == 0) {
			source = cleanValue;
		}
		return source;
	}

	public static Object shouldConvertToNull(Object value, Class<? extends Object> targetType) {
		if (targetType.equals(String.class)) {
			return value;
		} else if (targetType.isPrimitive()) {
			return value;
		}
		return null;
	}

	public static Class getPrimitiveFrom(Object w) {
		if (w instanceof Boolean) {
			return Boolean.TYPE;
		} else if (w instanceof Byte) {
			return Byte.TYPE;
		} else if (w instanceof Short) {
			return Short.TYPE;
		} else if (w instanceof Character) {
			return Character.TYPE;
		} else if (w instanceof Integer) {
			return Integer.TYPE;
		} else if (w instanceof Long) {
			return Long.TYPE;
		} else if (w instanceof Float) {
			return Float.TYPE;
		} else if (w instanceof Double) {
			return Double.TYPE;
		}
		return null;
	}

	public static Class getPrimitiveFrom(Class klass) {
		if (klass.equals(Boolean.class)) {
			return Boolean.TYPE;
		} else if (klass.equals(Byte.class)) {
			return Byte.TYPE;
		} else if (klass.equals(Short.class)) {
			return Short.TYPE;
		} else if (klass.equals(Character.class)) {
			return Character.TYPE;
		} else if (klass.equals(Integer.class)) {
			return Integer.TYPE;
		} else if (klass.equals(Long.class)) {
			return Long.TYPE;
		} else if (klass.equals(Float.class)) {
			return Float.TYPE;
		} else if (klass.equals(Double.class)) {
			return Double.TYPE;
		}
		return null;
	}

	public static Field getField(Object target, String name) {
		return getField(target.getClass(), name);
	}

	public static Field getField(Class<? extends Object> target, String name) {
		Field fields[] = target.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			if (name.equals(fields[i].getName())) {
				return fields[i];
			}
		}
		return null;
	}

	public static Method findMethodToGet(Class<? extends Object> target, String name) {

		StringBuffer sb = new StringBuffer(128);
		sb.append("get").append(name.substring(0, 1).toUpperCase());
		if (name.length() > 1) {
			sb.append(name.substring(1));
		}
		try {
			return target.getMethod(sb.toString(), (Class[]) null);
		} catch (Exception e) {
		}
		sb.setLength(0);
		sb.append("is").append(name.substring(0, 1).toUpperCase());
		if (name.length() > 1) {
			sb.append(name.substring(1));
		}

		try {
			return target.getMethod(sb.toString(), (Class[]) null);
		} catch (Exception e) {
		}

		return null;
	}

	public static Method findMethodToInject(Class<? extends Object> target, String name, Class source) {

		StringBuffer sb = new StringBuffer(128);
		sb.append("set").append(name.substring(0, 1).toUpperCase());

		if (name.length() > 1) {
			sb.append(name.substring(1));
		}
		String methodName = sb.toString();

		Method m = null;

		try {
			m = ReflectionUtil.getMethod(target, methodName, new Class[]{source});
		} catch (Exception e) {
		}

		if (m == null) {
			Class primitive = getPrimitiveFrom(source);
			if (primitive != null) {
				try {
					m = target.getMethod(methodName, primitive);
				} catch (Exception e) {
				}
			}
		}
		if (m != null) {
			m.setAccessible(true);
		}
		return m;
	}

	public static Field findFieldToInject(Class<? extends Object> target, String name, Class<? extends Object> source) {

		Field f = getField(target, name);

		if (f != null) {
			Class<?> type = f.getType();
			if (type.isAssignableFrom(source) || checkPrimitives(type, source)) {
				f.setAccessible(true);
				return f;
			}
		}
		return null;
	}

	public static Object getValueToInject(String name, Input input, String prefix) {

		if (prefix == null) {
			return input.getValue(name);
		}
		StringBuffer sb = new StringBuffer(64);
		sb.append(prefix).append(PREFIX_SEPARATOR).append(name);
		return input.getValue(sb.toString());

	}

	public static boolean hasValueToInject(String name, Input input, String prefix) {

		if (prefix == null) {
			return input.hasValue(name);
		}
		StringBuffer sb = new StringBuffer(64);
		sb.append(prefix).append(PREFIX_SEPARATOR).append(name);
		return input.hasValue(sb.toString());
	}

	private static final boolean isBlank(Object value) {
		if (value != null && value instanceof String) {
			String s = ((String) value).trim();
			return s.length() == 0;
		}
		return false;
	}

	public static boolean inject(Method m, Object target, Object value,
			Locale loc, boolean tryToConvert, boolean tryingToConvertBoolean)
			throws Exception {

		Class<?> type = m.getParameterTypes()[0];

		if (tryingToConvertBoolean) {
			if (value == null && (type.equals(Boolean.class) || type.equals(boolean.class))) {
				value = Boolean.FALSE;
			} else {
				// if trying to convert boolean, convert or don't do anything...
				return false;
			}
		}

		if (value == null || (type.isAssignableFrom(value.getClass())
				|| checkPrimitives(type, value.getClass())
				|| (tryToConvert && ((isBlank(value) && (value = shouldConvertToNull(value, type)) == null)
				|| (value = tryToConvert(value, type, loc)) != null)))) {
			try {
				m.invoke(target, value);
				return true;
			} catch (Exception e) {
				System.err.println("Error injecting by method: " + value + " in " + target + " thru " + m);
				throw e;
			}
		}
		return false;
	}

	public static boolean hasDefaultConstructor(Class<? extends Object> klass) {

		try {

			return klass.getConstructor((Class[]) null) != null;

		} catch (Exception e) {

			return false;
		}
	}

	public static void getObject(Object target, Input input, Locale loc,
			boolean tryField, String prefix, boolean tryToConvert,
			boolean convertBoolean, String... excludedPaths) throws Exception {

		// the default is to allow recursion...
		getObject(target, input, loc, tryField, prefix, tryToConvert, convertBoolean, true, excludedPaths);

	}

	public static void getObject(Object target, Input input, Locale loc,
			boolean tryField, String prefix, boolean tryToConvert,
			boolean convertBoolean, boolean allowRecursion, String... excludedPaths) throws Exception {

		Class<? extends Object> targetClass = target.getClass();
		Map<String, Object> setters, fields;

		// see if we have these in cache...

		synchronized (settersMaps) {
			setters = settersMaps.get(targetClass);
			fields = fieldsMaps.get(targetClass);
		}

		// if not in cache, prepare maps for injection...

		if (setters == null) {
			setters = new HashMap<String, Object>();
			fields = null;
			if (tryField) {
				fields = new HashMap<String, Object>();
			}

			prepareForInjection(targetClass, setters, fields);

			synchronized (settersMaps) {
				settersMaps.put(targetClass, setters);
				fieldsMaps.put(targetClass, fields);
			}
		}

		Iterator<String> iter = setters.keySet().iterator();

		while (iter.hasNext()) {
			String var = iter.next();

			if(excludedPaths!=null){
				boolean avaliateVar = true;
				for(String excludedPath : excludedPaths){
					if(excludedPath.equals(var)){
						avaliateVar = false;
						break;
					}
				}
				if(!avaliateVar){
					continue;
				}
			}
			boolean hasValue = hasValueToInject(var, input, prefix);
			Object value = getValueToInject(var, input, prefix);
			boolean tryingToConvertBoolean = false;
			if (value == null && !hasValue) {
				if (!convertBoolean) {
					continue;
				} else {
					tryingToConvertBoolean = true;
				}
			}

			// if (value == null) continue;
			Object obj = setters.get(var);

			// we may have a list of overloaded methods...
			List list = null;
			Method m = null;

			if (obj instanceof List) {
				list = (List) obj;
			} else {
				m = (Method) setters.get(var);
			}

			if (m != null) {
				if (!inject(m, target, value, loc, tryToConvert, tryingToConvertBoolean) && allowRecursion) {
					// i did not inject... how about a VO object for this setter?
					Class<?> type = m.getParameterTypes()[0];
					if (!type.getName().startsWith("java.lang.") && !type.isPrimitive() && hasDefaultConstructor(type)) {
						String[] newExcludedPaths = null;
						if (!Modifier.isAbstract(type.getModifiers())) {
							Object param = type.newInstance();
							if(excludedPaths!=null){
								List<String> newExcludedOnes = new ArrayList<String>();
								for(String excludedOne : excludedPaths){
									String discardingPath = var + ".";
									if(excludedOne.startsWith(discardingPath)){
										newExcludedOnes.add(The.firstTokenAfter(excludedOne, discardingPath, ""));
									}
								}
								if(newExcludedOnes.size()>0){
									newExcludedPaths = new String[newExcludedOnes.size()];
									for(int i = 0; i < newExcludedPaths.length; i++){
										newExcludedPaths[i] = newExcludedOnes.get(i);
									}
								}
							}
							InjectionUtils.getObject(param, input, loc, true, prefix, true, true, false, newExcludedPaths); // no recursion...
							inject(m, target, param, loc, false, false);
						}
					}
				}

			} else {
				Iterator it = list.iterator();
				boolean injected = false;
				while (it.hasNext()) {
					m = (Method) it.next();
					if (inject(m, target, value, loc, tryToConvert, tryingToConvertBoolean)) {
						injected = true;
						break;
					}
				}

				if (!injected && allowRecursion) {
					// i could not inject anything... how about a VO object for this setter...
					it = list.iterator();
					while (it.hasNext()) {
						m = (Method) it.next();
						Class<?> type = m.getParameterTypes()[0];
						if (!type.getName().startsWith("java.lang.") && !type.isPrimitive() && hasDefaultConstructor(type)) {
							Object param = type.newInstance();
							InjectionUtils.getObject(param, input, loc, true, prefix, true, true, false, excludedPaths); // no recursion...
							if (inject(m, target, param, loc, false, false)) {
								break; // done...
							}
						}
					}
				}
			}
		}

		if (fields != null) {
			iter = fields.keySet().iterator();
			while (iter.hasNext()) {
				String var = iter.next();
				boolean hasValue = hasValueToInject(var, input, prefix);
				Object value = getValueToInject(var, input, prefix);
				Field f = (Field) fields.get(var);
				Class<?> type = f.getType();

				// If there is no value in the action input, assume false for
				// booleans...
				// (checkboxes and radio buttons are not send when not
				// marked...)

				if (convertBoolean && value == null && !hasValue) {
					if (type.equals(Boolean.class) || type.equals(boolean.class)) {
						value = Boolean.FALSE;
					}
				}
				if(   (value == null && !hasValue)
						|| Modifier.isStatic(f.getModifiers())
						|| f.isSynthetic()
						|| Modifier.isFinal(f.getModifiers())
				){
					continue;
				}
				if (
					  (      value == null
					  	|| (type.isAssignableFrom(value.getClass())
					  	|| checkPrimitives(type, value.getClass())
					  	|| (tryToConvert && ((isBlank(value) && (value = shouldConvertToNull(value, type)) == null)
					  	|| (value = tryToConvert(value, type, loc)) != null)))
					  )
				) {
					try {
						f.set(target, value);
					} catch (Exception e) {
						AppLogger.getInstance().silent(e, "Error injecting by field: " + value + " in " + target); // comentar aqui em caso de pane.
					}
				}else{
					if(input.getValue(var)!=null && String[].class.isAssignableFrom(type) && input.getValue(var) instanceof String){
						f.set(target, input.getStringValues(var));
					}
				}
			}
		}
	}

	/**
	 * Extract all properties from bean and place them in output.
	 *
	 * @param bean The beam from where to extract the properties.
	 * @param output The output where to place the properties.
	 * @param prefix The prefix to use when placing the properties in the output.
	 * @param overwrite Overwrite ot not if value is already in the output.
	 */
	public static void setObject(Object bean, Output output, String prefix,
			boolean overwrite) {

		if (bean != null) {
			Method[] methods = bean.getClass().getMethods();
			for (int i = 0; i < methods.length; i++) {
				String name = methods[i].getName();
				if (((name.length() >= 4 && (name.startsWith("get"))) || (name.length() >= 3 && name.startsWith("is"))) && !name.equals("getClass") && methods[i].getParameterTypes().length == 0) {
					String adjusted = adjustName(name, prefix);
					if (!overwrite) {
						if (output.getValue(adjusted) != null) {
							continue;
						}
					}
					try {
						methods[i].setAccessible(true);
						Object value = methods[i].invoke(bean);
						output.setValue(adjusted, value);
					} catch (Exception e) {
						System.err.println("Error calling method in InjectionUtils: " + name);
						AppLogger.getInstance().execute(e);
					}
				}
			}
		}
	}

	/*
	 * This method takes setUsername and returns username.
	 * If we have a prefix, then it returns prefix.username.
	 */
	private static String adjustName(String name, String prefix) {

		StringBuilder sb;

		if (name.length() >= 4 && (name.startsWith("get") || name.startsWith("set"))) {
			sb = new StringBuilder(name.length() - 3);
			sb.append(name.substring(3, 4).toLowerCase());
			if (name.length() > 4) {
				sb.append(name, 4, name.length());
			}
		} else if (name.length() >= 3 && name.startsWith("is")) {
			sb = new StringBuilder(name.length() - 2);
			sb.append(name.substring(2, 3).toLowerCase());
			if (name.length() > 3) {
				sb.append(name, 3, name.length());
			}
		} else {
			throw new IllegalArgumentException("Cannot adjust method: " + name);
		}

		if (prefix != null) {
			StringBuffer sb2 = new StringBuffer(128);
			return sb2.append(prefix).append(PREFIX_SEPARATOR).append(sb.toString()).toString();
		}

		return sb.toString();
	}

	/**
	 * Extract the value of a property of a bean!
	 *
	 * @param bean the target bean
	 * @param nameProperty the property name
	 * @return they value as String. The method toString is always called to every property!
	 * @throws Exception
	 */
	public static String getProperty(Object bean, String nameProperty)
			throws Exception {

		if (Is.empty(nameProperty)) {
			return null;
		}
		Class<? extends Object> clazz = bean.getClass();
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			String methodName = getter(nameProperty);
			if (method.getName().equals(methodName) && method.getParameterTypes().length == 0) {
				Object value = method.invoke(bean);
				if (value == null) {
					return null;
				}
				return value.toString();
			}
		}

		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			if (field.getName().equals(nameProperty)) {
				Object value = field.get(bean);
				if (value == null) {
					return null;
				}
				return value.toString();
			}
		}
		return null;
	}

	private static String getter(String name) {
		StringBuilder sb = new StringBuilder(name.length() + 3);

		sb.append("get").append(name.substring(0, 1).toUpperCase());

		if (name.length() > 1) {
			sb.append(name.substring(1));
		}
		return sb.toString();
	}

	public static void beanToMap(Object bean, Map<String, String> map)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		if (bean != null) {

			for (Method method : bean.getClass().getMethods()) {
				String name = method.getName();

				if (name.length() > 3 && name.startsWith("get") && !name.equals("getClass")
						&& method.getParameterTypes().length == 0) {

					method.setAccessible(true);
					Object value = method.invoke(bean);
					map.put(name, value.toString());
				}
			}
		}
	}

	/**
	 *  byLeandro
	 * @param moneyFormat
	 * @return double a partir de formato monetario brasileiro.
	 */
	public static double realToDouble(String moneyFormat) {
		moneyFormat = moneyFormat.replace(".", "");
		moneyFormat = moneyFormat.replace(",", ".");
		moneyFormat = moneyFormat.replaceAll("[^0-9\\.,\\-]", "");
		return Double.parseDouble(moneyFormat);
	}

	/**
	 *  byLeandro
	 * @param moneyFormat
	 * @return float a partir de formato monetario brasileiro.
	 */
	public static float realToFloat(String moneyFormat) {
		moneyFormat = moneyFormat.replace(".", "");
		moneyFormat = moneyFormat.replace(",", ".");
		moneyFormat = moneyFormat.replaceAll("[^0-9\\.,\\-]", "");
		return Float.parseFloat(moneyFormat);
	}


	public static BigDecimal realToBigDecimal(String moneyFormat) {
		moneyFormat = moneyFormat.replace(".", "");
		moneyFormat = moneyFormat.replace(",", ".");
		moneyFormat = moneyFormat.replaceAll("[^0-9\\.,]", "");
		return new BigDecimal(moneyFormat);
	}
}
