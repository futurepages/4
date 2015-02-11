package org.futurepages.util.brazil.enums;

import java.io.Serializable;
import java.util.Calendar;

public enum MonthEnum implements Serializable {

	JANEIRO  (1,  "janeiro"  ),
	FEVEREIRO(2,  "fevereiro"),
	MARCO    (3,  "março"    ),
	ABRIL    (4,  "abril"    ),
	MAIO     (5,  "maio"     ),
	JUNHO    (6,  "junho"    ),
	JULHO    (7,  "julho"    ),
	AGOSTO   (8,  "agosto"   ),
	SETEMBRO (9,  "setembro" ),
	OUTUBRO  (10, "outubro"  ),
	NOVEMBRO (11, "novembro" ),
	DEZEMBRO (12, "dezembro" );

	private int id;
	private String name;

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