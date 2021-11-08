package org.futurepages.util.brazil.enums;

import java.io.Serializable;
import java.util.Calendar;

public enum MonthEnum implements Serializable {

	JANUARY   (1,  "janeiro"  ),
	FEBRUARY  (2,  "fevereiro"),
	MARCH     (3,  "março"    ),
	APRIL     (4,  "abril"    ),
	MAY       (5,  "maio"     ),
	JUNE      (6,  "junho"    ),
	JULY      (7,  "julho"    ),
	AUGUST    (8,  "agosto"   ),
	SEPTEMBER (9,  "setembro" ),
	OCTOBER   (10, "outubro"  ),
	NOVEMBER  (11, "novembro" ),
	DECEMBER  (12, "dezembro" );

	private int id;
	private final String name;

	private MonthEnum(int id, String name) {

		this.id = id;
		this.name = name;
	}

	public static String get(int i) {
		return MonthEnum.values()[i - 1].name;
	}

	public static String get(Calendar cal) {
		return MonthEnum.values()[cal.get(Calendar.MONTH)].name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public String getAbbr() {
		return name.substring(0, 3);
	}

	public static int daysCount(int ano, int mes) {
		if (mes != 2) {
			int[] mesesQuant = new int[]{0, 31, 0, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
			return mesesQuant[mes];
		} else { //fevereiro e seus bisextos
			return ((ano % 100 != 0) ? (((ano % 4) != 0) ? 28 : 29) : ((ano / 100 % 4 != 0) ? 28 : 29));
		}
	}

	@Override
	public String toString() {
		return this.name;
	}
}