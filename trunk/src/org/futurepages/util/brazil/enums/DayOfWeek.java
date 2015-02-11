package org.futurepages.util.brazil.enums;

import java.io.Serializable;
import java.util.Calendar;

public enum DayOfWeek implements Serializable{
	
	SUNDAY(Calendar.SUNDAY,   	 "Domingo"       , "Domingo"),
	MONDAY(Calendar.MONDAY,   	 "Segunda-feira" , "Segunda"),
	TUESDAY(Calendar.TUESDAY, 	 "Terça-feira"   , "Terça"),
	WEDNESDAY(Calendar.WEDNESDAY,"Quarta-feira"  , "Quarta"),
	THURSDAY(Calendar.THURSDAY,  "Quinta-feira"  , "Quinta"),
	FRIDAY(Calendar.FRIDAY,		 "Sexta-feira"   , "Sexta"),
	SATURDAY(Calendar.SATURDAY,  "Sábado"        , "Sábado");
	

	private int key;
	private String completeDescription;
	private String smallDescription;

	private DayOfWeek(int key,String completDescription, String smallDescription){
		this.key = key;
		this.completeDescription= completDescription;
		this.smallDescription= smallDescription;
		
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public String getCompleteDescription() {
		return completeDescription;
	}

	public void setCompleteDescription(String completeDescription) {
		this.completeDescription = completeDescription;
	}

	public String getSmallDescription() {
		return smallDescription;
	}

	public void setSmallDescription(String smallDescription) {
		this.smallDescription = smallDescription;
	}
	
	public static DayOfWeek getDayByKey(int key){
		for (DayOfWeek day : DayOfWeek.values()) {
			if(day.key == key){
				return day;
			}
		}
		return null;
	}
}
