package org.futurepages.util.iterator.months;

import java.util.Iterator;

/**
 *  
 * @author Leandro Santana
 */
public class MonthYearIterator implements Iterator<MonthYear>, Iterable<MonthYear> {

	private int initialYear;
	private int initialMonth;
	private int endYear;
	private int endMonth;

	private int year;
	private int month;

	public MonthYearIterator(int initialYear, int initialMonth, int endYear, int endMonth) {
		this.initialYear = initialYear;
		this.initialMonth = initialMonth;
		this.endYear = endYear;
		this.endMonth = endMonth;
		init();
	}

	public MonthYearIterator(MonthYear initial, MonthYear end) {
		this.initialYear = initial.getYear();
		this.initialMonth = initial.getMonth();
		this.endYear = end.getYear();
		this.endMonth = end.getMonth();
		init();
	}


	private void init() {
		year  = initialYear;
		month = initialMonth;
	}
	

	@Override
	public Iterator<MonthYear> iterator() {
		return this;
	}

	@Override
	public void remove() {}

	@Override
	public boolean hasNext() {
		return  year < endYear || (year == endYear && month <= endMonth);
	}

	@Override
	public MonthYear next() {
		MonthYear mYear = new MonthYear(month,year);
		if(hasNext()){
			if(month!=12){
				month++;
			}else{
				year++;
				month = 1;
			}
			return mYear;
		}else{
			return null;
		}
	}
}