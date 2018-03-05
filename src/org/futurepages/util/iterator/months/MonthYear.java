package org.futurepages.util.iterator.months;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.futurepages.util.CalendarUtil;
import org.futurepages.util.The;

/**
 * @author Leandro Santana
 */
public class MonthYear implements Comparable<MonthYear> {

	private int month;
	private int year;

	public MonthYear(){
	}

	public MonthYear(int month, int year) {
		this.month = month;
		this.year = year;
	}

	// input: YYYY/MM
	public MonthYear(String monthYearStr) {
		String[] montYearStrParts = monthYearStr.split("/");
		this.year = Integer.parseInt(montYearStrParts[0]);
		this.month = Integer.parseInt(montYearStrParts[1]);
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public MonthYear getNext() {
		int nextMonth;
		int nextYear = year;
		if (month != 12) {
			nextMonth = month+1;
		} else {
			nextYear = year+1;
			nextMonth = 1;
		}
		return new MonthYear(nextMonth, nextYear);
	}

	public MonthYear getPrevious(){
		int previousMonth;
		int previousYear = year;
		if (month != 1) {
			previousMonth = month-1;
		} else {
			previousYear = year-1;
			previousMonth = 12;
		}
		return new MonthYear(previousMonth, previousYear);
	}

		/**
	 * Copara este monthYear com um segundo
	 * @param that
	 * @return -1 (quando sou menor que 'that')
	 *          1 (quando sou maior que 'that')
	 *          0 (quando somos iguais)
	 */
	@Override
	public int compareTo(MonthYear that) {
		Calendar calThis = new GregorianCalendar(this.year,this.month-1,1);
		Calendar calThat = new GregorianCalendar(that.getYear(),that.getMonth()-1,1);
		return CalendarUtil.compareCalendarDate(calThis, calThat);
	}

	public int lastDay(){
		Calendar cal = new GregorianCalendar(this.year,this.month-1,1);
		return cal.getActualMaximum(cal.DAY_OF_MONTH);
	}

	public boolean before(MonthYear that){
		return this.compareTo(that)<0;
	}

	public boolean after(MonthYear that){
		return this.compareTo(that)>0;
	}

	public boolean equals(MonthYear that){
		return this.year==that.year && this.month == that.month;
	}

	@Override
	public String toString() {
		return this.year + "/" + The.intWithLeftZeros(month, 2);
	}

	public static MonthYear get(Calendar cal) {
		return new MonthYear(cal.get(Calendar.MONTH)+1,cal.get(Calendar.YEAR));
	}
}
