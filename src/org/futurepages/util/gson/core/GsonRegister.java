package org.futurepages.util.gson.core;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.futurepages.core.exception.AppLogger;
import org.futurepages.util.Is;

import java.lang.reflect.Type;

public abstract class GsonRegister <T> {

	public GsonRegister(Class<T> typeClass, GsonBuilder gb, boolean defineToJson){
		registerFor(gb, typeClass, defineToJson);
	}

	public void registerFor(GsonBuilder gb, Class<T> typeClass, boolean defineToJson){
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
		if(defineToJson){
			gb.registerTypeHierarchyAdapter(typeClass, (JsonSerializer<T>) (data, typeOft, context) -> {
				try {
					if(data!=null){
						return toJson(data,typeOft, context);
					}
				}catch (Exception e) {
					AppLogger.getInstance().execute(e);
				}
				return null;
			});
		}
	}

	public abstract T fromJson(String jsonStr);

	public JsonElement toJson(T data, Type type, JsonSerializationContext context){
		throw new RuntimeException("You need to implement this method in the subclass");
	}
}