package org.futurepages.util.brazil.enums;

import java.io.Serializable;
import java.util.Calendar;

public enum UnitTimeEnum implements Serializable {

	YEAR  ("ano"    , "anos"    , "ano",            Calendar.YEAR         ,0),
	MONTH ("mês"    , "meses"   , "mês",   "mes",	Calendar.MONTH        ,1),
	DAY   ("dia"    , "dias"    , "d",              Calendar.DATE         ,2),
	HOUR  ("hora"   , "horas"   , "h",              Calendar.HOUR_OF_DAY  ,3),
	MINUTE("minuto" , "minutos" , "min",            Calendar.MINUTE       ,4),
	SECOND("segundo", "segundos", "seg",            Calendar.SECOND       ,5),
	WEEK  ("semana" , "semanas" , "semana",         Calendar.WEEK_OF_YEAR ,6);

	private final String singularName;
	private final String pluralName;
	private final String abbreviation;
	private final String simpleName;
	private final int order;
	private final int calendarConstant;
	
	UnitTimeEnum(String singular, String plural, String abrev, String simpleName, int constt, int ord){
		this.abbreviation = abrev;
		this.singularName = singular;
		this.pluralName = plural;
		this.simpleName = simpleName;
		this.order = ord;
		this.calendarConstant = constt;
	}
	
	private UnitTimeEnum(String singular, String plural,String abrev,int constt, int ord){
		this(singular, plural, abrev, singular, constt, ord);
	}

	public String getSingularName() {
		return singularName;
	}

	public String getPluralName() {
		return pluralName;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public String getSimpleName() {
		return simpleName;
	}

	public int getOrder() {
		return order;
	}
	
	public int getCalendarConstant() {
		return calendarConstant;
	}

	public static UnitTimeEnum getByOrder(int order){
		for (UnitTimeEnum unit : UnitTimeEnum.values()) {
			if(unit.order == order){
				return unit;
			}
		}
		return null;
	}

	public static UnitTimeEnum getBySingularName(String name){
		for (UnitTimeEnum unit : UnitTimeEnum.values()) {
			if(unit.singularName.equals(name)){
				return unit;
			}
		}
		return null;
	}
	
	public static UnitTimeEnum getBySimpleName(String simpleName){
		for (UnitTimeEnum unit : UnitTimeEnum.values()) {
			if(unit.simpleName.equals(simpleName)){
				return unit;
			}
		}
		return null;
	}

	public String apropriateUnitDescription(int valor, boolean noAbbrs) {
		String unitName;
		if(this.getOrder()<3 || noAbbrs){
			if(valor>1){
				unitName = this.getPluralName();
			}else{
				unitName = this.getSingularName();
			}
		}else{
			unitName = this.getAbbreviation();
		}
		return unitName;
	}
}