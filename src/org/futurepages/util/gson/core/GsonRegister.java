package org.futurepages.util.gson.core;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import org.futurepages.core.exception.AppLogger;
import org.futurepages.util.Is;

public abstract class GsonRegister<T extends Object> {

	private Class<T> typeClass;
	private boolean number;

	public GsonRegister(Class<T> typeClass, GsonBuilder gb){
		this.typeClass = typeClass;
		if(  Number.class.isAssignableFrom(typeClass)){
			number = true;
		}
		registerFor(gb);
	}

	public void registerFor(GsonBuilder gb){
		gb.registerTypeAdapter(typeClass, (JsonDeserializer<T>) (json, typeOfT, context) -> {
			try {
				String jsonStr = json.getAsString();
				if(!Is.empty(jsonStr)){
					return fromJson(jsonStr);
				}
			}catch (Exception e) {
				AppLogger.getInstance().execute(e);
			}
			return null;
		});
		gb.registerTypeHierarchyAdapter(typeClass, (JsonSerializer<T>) (data, typeOft, context) -> {
			try {
				if(data!=null){
					if(number){
						return new JsonPrimitive((Number) data);
					}else{
						return new JsonPrimitive(toJson(data));
					}
				}
			}catch (Exception e) {
				AppLogger.getInstance().execute(e);
			}
			return null;
		});
	}

	public abstract T fromJson(String jsonStr);

	public abstract String toJson(T data);
}