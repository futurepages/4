package org.futurepages.util;

import java.util.HashSet;
import java.util.Set;

public class TypeScriptUtil {

	public static String generateFor(String... packages){

		Set<Class> classSet = new HashSet<>();

		Set<String> types = new HashSet<>();
		Set<String> interfaces = new HashSet<>();

		for(String pack : packages){
			// TODO: find all classes from pack...
			// TODO: find all fields from class...
			// TODO: treat all types of fields...
			// TODO: treat all kinds of dependencies.
		}
		return "";
	}
}
