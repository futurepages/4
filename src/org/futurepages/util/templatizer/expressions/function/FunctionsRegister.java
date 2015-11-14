package org.futurepages.util.templatizer.expressions.function;

import java.util.HashMap;
import static org.futurepages.util.The.concat;
import org.futurepages.util.templatizer.expressions.exceptions.FunctionWithSameNameAlreadyExistsException;

/**
 *
 * @author thiago
 */
public class FunctionsRegister {
	
	static {
		instance = new FunctionsRegister();
	}
	
	private static FunctionsRegister instance;
	
	public static FunctionsRegister instance() {
		return instance;
	}

	public static FunctionsRegister getInstance() {
		return instance;
	}

	private final HashMap<String, Class<? extends Function>> builtInFunctions;
	private HashMap<String, Class<? extends Function>> customFunctions;
	
	private FunctionsRegister() {
		builtInFunctions = new HashMap<String, Class<? extends Function>>();
		initBuildin();
	}
	
	private void initBuildin() {
		addBuiltInFunction("empty", Empty.class);
		addBuiltInFunction("length", Length.class);
		addBuiltInFunction("str", Str.class);
		addBuiltInFunction("pow", Pow.class);
		addBuiltInFunction("get", Get.class);
	}
	
	private void addBuiltInFunction(String name, Class<? extends Function> f) {
		builtInFunctions.put(name, f);
	}
	
	public void addFunction(String name, Class<? extends Function> f) {
		if (customFunctions == null) {
			customFunctions = new HashMap<String, Class<? extends Function>>();
		}
		
		if (builtInFunctions.containsKey(name)) {
			String className = builtInFunctions.get(name).getClass().getName();
			throw new FunctionWithSameNameAlreadyExistsException(concat("A built in Function whith the same name (", name, ") already exists: [", className, "]"));
		} else if (customFunctions.containsKey(name)) {
			String className = customFunctions.get(name).getClass().getName();
			throw new FunctionWithSameNameAlreadyExistsException(concat("A custom TemplateTag whith the same name (", name, ") already exists: [", className, "]"));
		}

		customFunctions.put(name, f);
	}
	
	public Class<? extends Function> getFunctionClass(String name) {
		Class<? extends Function> fclass = builtInFunctions.get(name);

		if (fclass == null && customFunctions != null) {
			fclass = customFunctions.get(name);
		}

		return fclass;
	}
}
