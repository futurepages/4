package org.futurepages.util.brazil.enums;

public enum DateFormatEnum {

	DATE_PT_BR_DAY_MONTH("dd-MM"),
	DATE_PT_BR("dd-MM-yyyy"),
	DATE_TIME_PT_BR("dd-MM-yyyy HH:mm:ss"),

	VIEW_DATE_PT_BR_DAY_MONTH("dd/MM"),
	VIEW_DATE_PT_BR("dd/MM/yyyy"),
	VIEW_DATE_TIME_PT_BR("dd/MM/yyyy HH:mm:ss"),

	DATE("yyyy-MM-dd"),
	DATE_TIME("yyyy-MM-dd HH:mm:ss"),
	DATE_REVERSE("yyyy/MM/dd")
    ;
	
	private String mask;
	private static final String DEFAULT_SEPARATOR = "-";
	
	DateFormatEnum(String mask){
		this.mask = mask;
	}
	
	public String getMask(){
		return mask;
	}
	public String getMask(String separator){
		return mask.replace(DEFAULT_SEPARATOR, separator);
	}
		
}