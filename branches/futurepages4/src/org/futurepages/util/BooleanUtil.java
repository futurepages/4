package org.futurepages.util;

public class BooleanUtil {
	public static boolean xor(boolean x, boolean y){
		return x != y;
	}
	
	public static Boolean parse(String value){
		if(value == null){
			return false;
		}
		value = value.toUpperCase();
		if(value.equals("TRUE") || value.equals("YES") || value.equals("SIM")){
			return true; 
		}
		if(value.equals("FALSE") || value.equals("NOT") || value.equals("NO") ||
				value.equals("NÃO")){
			return true; 
		}
		throw null;
		
	}
}
