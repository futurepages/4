package org.futurepages.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtil {

	public static Gson pretty(){
		GsonBuilder gb = new GsonBuilder();
		gb.setPrettyPrinting();
		return gb.create();
	}
}
