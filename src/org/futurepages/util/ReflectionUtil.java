package org.futurepages.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.commons.lang.StringUtils;

/**
 * Class Helper for reflection operations.
 * @author Jonhnny Weslley
 */
public final class ReflectionUtil {

	/**
	 * Returns the component into the given field name.
	 * @param object
	 * @param fieldName
	 * @return Object
	 * @throws Exception
	 */
	public static Object getParent(Object object, String fieldName) {
		Object child = object;
		StringTokenizer tokenizer = new StringTokenizer(fieldName, ".");
		int countTokens = tokenizer.countTokens() - 1;
		for (int i = 0; i < countTokens; i++) {
			String token = tokenizer.nextToken();
			child = invokeGetMethodOf(child, token);
		}
		return child;
	}

	/**
	 * Invoke the Set access fields of the given object with the given field name.
	 * @param object
	 * @param fieldName
	 * @param value
	 */
	public static void setField(Object object, String fieldName, Object value) {
		Object target = object;
		if (fieldName.contains(".")) {
			int lastPoint = fieldName.lastIndexOf(".");
			target = getParent(object, fieldName);
			fieldName = fieldName.substring(++lastPoint);
		}
		invokeSetMethodOf(target, fieldName, value);
	}

	/**
	 * Invoke the Get access fields of the given object with the given field path.
	 * @param object
	 * @param fieldPath
	 * @return Object
	 */
	public static Object getField(Object object, String fieldPath) {
		Object target = object;
		if (fieldPath.contains(".")) {
			int lastPoint = fieldPath.lastIndexOf(".");
			target = getParent(object, fieldPath);
			fieldPath = fieldPath.substring(++lastPoint);
		}
		return invokeGetMethodOf(target, fieldPath);
	}


	public static Field getObjectField(String name, Class klass){
		if(klass == null){
			return null;
		}else{
			try {
				Field f = klass.getDeclaredField(name);
				if( f != null){
					return f;
				}
			} catch (Exception e) {
			}
			return getObjectField(name, klass.getSuperclass());
		}
	}
	/**
	 * Invokes a Set method from a object with the given name.
	 * @param object
	 * @param fieldName
	 * @return Object
	 */
	public static void invokeSetMethodOf(Object object, String fieldName, Object value) {
		Method[] methods = object.getClass().getMethods();
		String methodName = "set" + StringUtils.capitalize(fieldName);
		for (Method method : methods) {
			if (methodName.equals(method.getName())) {
				Class[] parameters = method.getParameterTypes();
				if (parameters.length == 1 && method.getReturnType() == Void.TYPE) {
					try {
						method.invoke(object, new Object[]{value});
						return;
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
	}

	/**
	 * Invokes a Get method from a object with the given name.
	 * @param object
	 * @param fieldName
	 * @return Object
	 */
	public static Object invokeGetMethodOf(Object object, String fieldName) {
		String []prefixes = {"get", "is"}; //, "has"};

		for (int i = 0, len = prefixes.length; i < len;) {
			String prefix = prefixes[i];
			String methodName = prefix + StringUtils.capitalize(fieldName);

			try {
				Method method = object.getClass().getMethod(methodName);

				return method.invoke(object);
			} catch (Exception ex) {
				i += 1;
			}
		}

		return null;
	}

	/**
	 * Invokes a void and empty parameter method from the given class.
	 * @param object
	 * @param methodName
	 * @return boolean
	 */
	public static boolean invokeVoidMethod(Object object, String methodName) {
		try {
			Method method = object.getClass().getMethod(methodName, new Class[]{});
			method.invoke(object, new Object[]{});
			return true;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void invokeVoidMethod(Object object, String methodName, Object... value) throws Exception {
		Class[] classes = new Class[value.length];
		for (int i = 0; i < value.length; i++) {
			classes[i] = value[i].getClass();
		}

		Method method = getMethodInvoke(object, methodName, classes);

//		Class[] paramClass = new Class[value.length];
//		for (int i = 0; i < value.length; i++)
//			paramClass[i] = value[i].getClass();
//
//		Method method = object.getClass().getMethod(methodName, paramClass);

		if (method.getReturnType() == Void.TYPE) {
			method.invoke(object, value);
		}
	}

	public static Method getFirstMethod(Class<?> theClass, String methodName) {
		while(theClass!=null){
			Method[] methods = theClass.getMethods();
			for (Method m : methods) {
				if (m.getName().equals(methodName)) {
					return m;
				}
			}
			theClass = theClass.getSuperclass();
		}
		return null;
	}


	@SuppressWarnings("unchecked")
	public static Method getMethodInvoke(Object object, String methodName, Class... value) throws ClassNotFoundException {
		Method[] methods = object.getClass().getMethods();
		Method methodInvoke = null;
		int[] controlMethod = new int[value.length];
		int count = 0;
		boolean firstTime = true;
		for (Method method : methods) {
			if (methodName.equals(method.getName())) {
				Class[] paramTypes = method.getParameterTypes();
				if (paramTypes.length != value.length) {
					continue;
				}

				int[] arrayControl = new int[paramTypes.length];
				int j = 0;
				String stringParameters = StringUtils.substringBetween(method.toString(), "(", ")");
				if (!StringUtils.isEmpty(stringParameters)) {
					StringTokenizer token = new StringTokenizer(stringParameters, ",");
					int countParameter = 0;

					boolean invalidsParameters = false;

					while (token.hasMoreTokens()) {
						String type = token.nextToken();
						Class klassValue = value[countParameter];
						Class klassP;
						if (type.equals("byte[]")) {
							klassP = Array.newInstance(byte.class, 10485760).getClass();
						} else {
							klassP = Class.forName(type);
						}

						if (!klassP.isAssignableFrom(klassValue)) {
							invalidsParameters = true;
							break;
						}

						int countHierarchy = 1;

						while (!klassP.equals(klassValue) && !klassValue.equals(Object.class)) {
							klassValue = klassValue.getSuperclass();
							countHierarchy++;
						}

						if (arrayControl[countParameter] <= countHierarchy) {
							arrayControl[countParameter] = countHierarchy;
							if (controlMethod[countParameter] > countHierarchy && !firstTime) {
								j++;
							}
						}
						countParameter++;
					}

					if ((j > count || firstTime) && !invalidsParameters) {
						count = j;
						controlMethod = arrayControl;
						methodInvoke = method;
						firstTime = false;
					}

				} else {
					methodInvoke = method;
					break;
				}
			}
		}

		return methodInvoke;
	}

	public static Object invokeMethod(Object object, String methodName, Object... value) {
		try {
			Class[] classes = new Class[value.length];
			for (int i = 0; i < value.length; i++) {
				classes[i] = value[i].getClass();
			}

			Method method = getMethodInvoke(object, methodName, classes);

//			Class[] paramClass = new Class[value.length];
//			for (int i = 0; i < value.length; i++)
//				paramClass[i] = value[i].getClass();
//
//			Method method = object.getClass().getMethod(methodName, paramClass);

			if (method.getReturnType() != Void.TYPE) {
				return method.invoke(object, value);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	/**
	 * Invokes a void and empty parameter method from the given class.
	 * @param object
	 * @param collectionName
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public static boolean invokeAddMethodOf(Object object, String collectionName, Object value) throws Exception {
		Object collectionField = getField(object, collectionName);
		if (!(collectionField instanceof Collection)) {
			throw new Exception();
		}
		Collection collection = (Collection) collectionField;
		return collection.add(value);
	}

	@SuppressWarnings("unchecked")
	public static boolean invokeRemoveMethodOf(Object object, String collectionName, Object value) throws Exception {
		Object collectionField = getField(object, collectionName);
		if (!(collectionField instanceof Collection)) {
			throw new Exception();
		}
		Collection collection = (Collection) collectionField;
		return collection.remove(value);
	}

	/**
	 * Returns the field type.
	 * @param klass
	 * @param fieldName
	 * @return Class
	 * @throws IllegalAccessException
	 * @throws Exception
	 */
	public static Class getFieldType(Class klass, String fieldName) throws Exception, IllegalAccessException {
		Class type = klass;
		String token = fieldName;
		if (fieldName.contains(".")) {
			StringTokenizer tokenizer = new StringTokenizer(fieldName, ".");
			int countTokens = tokenizer.countTokens() - 1;
			for (int i = 0; i < countTokens; i++) {
				token = tokenizer.nextToken();
				type = invokeGetMethodOf(type.newInstance(), token).getClass();
			}
			token = tokenizer.nextToken();
		}
		return fieldType(type, token);
	}

	public static Class fieldType(Class klass, String fieldName) {
		String methodName = "get" + StringUtils.capitalize(fieldName);
		try {
			return klass.getMethod(methodName).getReturnType();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static Object toPrimitive(Class primitive, String value) throws Exception {
		if (primitive == boolean.class) {
			return Boolean.parseBoolean(value);
		}

		if (primitive == int.class) {
			return Integer.parseInt(value);
		}

		if (primitive == byte.class) {
			return Byte.parseByte(value);
		}

		if (primitive == short.class) {
			return Short.parseShort(value);
		}

		if (primitive == long.class) {
			return Long.parseLong(value);
		}

		if (primitive == float.class) {
			return Float.parseFloat(value);
		}

		if (primitive == double.class) {
			return Double.parseDouble(value);
		}

		if (primitive == String.class) {
			return value;
		}

		throw new Exception();
	}

	/**
	 * Invoke the Get access fields of the given object with the given field name.
	 * @param object
	 * @param fieldName
	 * @return Object
	 */
	public static Object getStaticField(Object object, String fieldName) {
		Object target = object;
		if (fieldName.contains(".")) {
			int lastPoint = fieldName.lastIndexOf(".");
			target = getParent(object, fieldName);
			fieldName = fieldName.substring(++lastPoint);
		}
		return staticField(target.getClass(), fieldName);
	}

	/**
	 * Returns a static field from the given class.
	 * @param klass
	 * @param fieldName
	 * @return Object
	 */
	public static Object staticField(Class klass, String fieldName) {
		Object fieldValue = null;
		try {
			Field field = klass.getField(fieldName);
			fieldValue = field.get(null);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return fieldValue;
	}

	/**
	 * Um objeto 'child' recebe todos os valores herdados de seu pai 'parent'.
	 * Os campos não herdados continuam com o valor nulo.
	 *
	 * @param parent é o pai de onde os campos serão herdados.
	 * @param child é quem herdará os campos.
	 */
	public static void setWithSuperFields(Object parent, Object child) {
		Class clss = child.getClass();
		while (clss.getSuperclass() != null) {
			clss = clss.getSuperclass();
			Field[] fields = clss.getDeclaredFields();
			for (Field field : fields) {
				try {
					field.setAccessible(true);
					field.set(child,field.get(parent));
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			}
		}
	}

	public static List<Field> getAllFields(Object obj) {
		Class clss = obj.getClass();
		List<Field> fieldsList = new ArrayList<>();
		do {
			Field[] fields = clss.getDeclaredFields();
			for (Field field : fields) {
				try {
					field.setAccessible(true);
					fieldsList.add(field);
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			}
			clss = clss.getSuperclass();
		} while (clss != null && clss != Object.class);
		return fieldsList;
	}

	public static <T extends Object> T clone(T fromObj){
		try {
			T newObj = (T) fromObj.getClass().newInstance();
			cloneFields(fromObj, newObj);
			return newObj;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Joga os atributos de um objeto em outro vazio.
	 * @param fromObj
	 * @param toObj
	 * Caso deseja criar novas referências nas dependências, utilize SerializationUtil (apache commons)
	 *
	 */
	public static void cloneFields(Object fromObj, Object toObj) {
		Class clss = toObj.getClass();
		while (clss != null) {
			Field[] fields = clss.getDeclaredFields();
			for (Field field : fields) {
				try {
					field.setAccessible(true);
					if(!Modifier.isFinal(field.getModifiers())){
						field.set(toObj, field.get(fromObj));

					}
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			}
			clss = clss.getSuperclass();
		}
	}

	/**
	 * Finds the most specific applicable method
	 *
	 * @param source Class to find method in
	 * @param name Name of method to find
	 * @param parameterTypes Parameter types to search for
	 */
	public static Method getMethod(Class<? extends Object> source,
			String name,
			Class[] parameterTypes)
			throws NoSuchMethodException {
		return internalFind(source.getMethods(),
				name,
				parameterTypes);
	}

    public static boolean isAnnotationPresentInHierarchy(Class clss, Class annotation) {

        do {
            if (clss.isAnnotationPresent(annotation)) {
                return true;
            }
        } while ((clss = clss.getSuperclass()) != null);

        return false;
    }

	/**
	 * Finds the most specific applicable declared method
	 *
	 * @param source Class to find method in
	 * @param name Name of method to find
	 * @param parameterTypes Parameter types to search for
	 */
	public static Method getDeclaredMethod(Class source, String name, Class[] parameterTypes)
			throws NoSuchMethodException {
		return internalFind(source.getDeclaredMethods(),
				name,
				parameterTypes);
	}

	public static boolean isSomeAnnotationPresent(Class classe, Class... annotations) {
		for (Class anot : annotations) {
			if(classe.isAnnotationPresent(anot)){
				return true;
			}
		}
		return false;
	}

	/**
	 * Internal method to find the most specific applicable method
	 */
	private static Method internalFind(Method[] toTest,
			String name,
			Class[] parameterTypes)
			throws NoSuchMethodException {
		int l = parameterTypes.length;

		// First find the applicable methods
		List<Method> applicableMethods = new LinkedList<Method>();

		for (int i = 0; i < toTest.length; i++) {
			// Check the name matches
			if (!toTest[i].getName().equals(name)) {
				continue;
			}
			// Check the parameters match
			Class[] params = toTest[i].getParameterTypes();

			if (params.length != l) {
				continue;
			}
			int j;

			for (j = 0; j < l; j++) {
				if (!params[j].isAssignableFrom(parameterTypes[j])) {
					break;
				}
			}
			// If so, add it to the list
			if (j == l) {
				applicableMethods.add(toTest[i]);
			}
		}

		/*
		 * If we've got one or zero methods, we can finish
		 * the job now.
		 */
		int size = applicableMethods.size();

		if (size == 0) {
			throw new NoSuchMethodException("No such method: " + name);
		}
		if (size == 1) {
			return applicableMethods.get(0);
		}

		/*
		 * Now find the most specific method. Do this in a very primitive
		 * way - check whether each method is maximally specific. If more
		 * than one method is maximally specific, we'll throw an exception.
		 * For a definition of maximally specific, see JLS section 15.11.2.2.
		 *
		 * I'm sure there are much quicker ways - and I could probably
		 * set the second loop to be from i+1 to size. I'd rather not though,
		 * until I'm sure...
		 */
		int maximallySpecific = -1; // Index of maximally specific method

		for (int i = 0; i < size; i++) {
			int j;
			// In terms of the JLS, current is T
			Method current = applicableMethods.get(i);
			Class[] currentParams = current.getParameterTypes();
			Class currentDeclarer = current.getDeclaringClass();

			for (j = 0; j < size; j++) {
				if (i == j) {
					continue;
				}
				// In terms of the JLS, test is U
				Method test = applicableMethods.get(j);
				Class[] testParams = test.getParameterTypes();
				Class testDeclarer = test.getDeclaringClass();

				// Check if T is a subclass of U, breaking if not
				if (!testDeclarer.isAssignableFrom(currentDeclarer)) {
					break;
				}

				// Check if each parameter in T is a subclass of the
				// equivalent parameter in U
				int k;

				for (k = 0; k < l; k++) {
					if (!testParams[k].isAssignableFrom(currentParams[k])) {
						break;
					}
				}
				if (k != l) {
					break;
				}
			}
			// Maximally specific!
			if (j == size) {
				if (maximallySpecific != -1) {
					throw new NoSuchMethodException("Ambiguous method search - more "
							+ "than one maximally specific method");
				}
				maximallySpecific = i;
			}
		}
		if (maximallySpecific == -1) {
			throw new NoSuchMethodException("No maximally specific method.");
		}
		return applicableMethods.get(maximallySpecific);
	}
}
