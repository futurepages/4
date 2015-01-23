package org.futurepages.core.input;

import java.io.Serializable;

import org.futurepages.util.ReflectionUtil;
import org.futurepages.util.The;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

public class InputHunter {

	private String path;
	private String[] tokenedPath;

	public InputHunter(String path){
		this.path = path;
		init();
	}
	
	private void init() {
		tokenedPath = The.explodedToArray(path, ".");
	}

	public Object[] getValues(Input input){
		Object[] values = new Object[tokenedPath.length];
		if(tokenedPath.length > 0){
			values[0] = input.getValue(tokenedPath[0]);
		}
		Object obj = values[0];
		for (int i = 1; i < values.length; i++) {
			if(obj != null){
				obj = ReflectionUtil.invokeGetMethodOf(obj, tokenedPath[i]);
			}
			values[i] = obj;
		}
		return values;
	}
	
	public static Serializable getInputFromKey(Input input, Class pkType, String elementName) {

		Serializable keyValue = null;
		if(pkType != null){
			if (pkType.equals(StringType.class)) {
				keyValue =  input.getStringValue(elementName);

			} else if(pkType.equals(LongType.class)) {
				keyValue = input.getLongValue(elementName);

			} else if(pkType.equals(IntegerType.class)) {
				keyValue = input.getIntValue(elementName);
			}
			return keyValue;
		}
		return (Serializable) input.getValue(elementName);
	}

	public String[] getTokenedPath() {
		return tokenedPath;
	}

	public void setTokenedPath(String[] tokenedPath) {
		this.tokenedPath = tokenedPath;
	}
	
}
