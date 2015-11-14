package org.futurepages.menta.filters;

import org.futurepages.menta.core.control.InvocationChain;
import org.futurepages.menta.core.filter.Filter;
import org.futurepages.menta.core.input.Input;
import org.futurepages.util.CalendarUtil;
import org.futurepages.util.DateUtil;
import org.futurepages.util.brazil.BrazilDateUtil;
import org.futurepages.util.brazil.enums.DateFormatEnum;

import java.util.Calendar;
import java.util.Date;

/**
 * Captura dois campos a partir de uma chave "x" , x_date e x_time,
 * onde por exemplo, x_date = '30/12/2012' e x_time = '12:50'
 * o filtro injetará em x um Calendar, um Date ou uma String única
 * contendo a seguinte informação de data/hora: '2012-12-30 12:50:00'
 *
 * O tipo, se será Calendar, Date ou String pode ser passado por parâmetro, mas
 * o padrão é Calendar.
 *
 * O input ficará com null caso não seja convertido corretamente.
 * 
 * @author leandro
 */
public class DateTimeInjectionFilter implements Filter {

	private Class dateTimeType;
	private String keyToInject;

	boolean html5Input = false;


	public DateTimeInjectionFilter(String keyToInject) {
		this.keyToInject = keyToInject;
		this.dateTimeType = Calendar.class;
	}
	public DateTimeInjectionFilter(String keyToInject, boolean html5Input) {
		this(keyToInject);
		this.html5Input = html5Input;
	}

	@Override
	public String filter(InvocationChain chain) throws Exception {
		Input input = chain.getAction().getInput();
		try {
			String date = (html5Input)? input.getStringValue(keyToInject + "_date") : BrazilDateUtil.dbDate(input.getStringValue(keyToInject + "_date"));
			String time = input.getStringValue(keyToInject + "_time");
			String dbDateTime = date + " " + time + (time.length()==5?":00":"");
			if (dateTimeType == Calendar.class) {
				input.setValue(keyToInject, CalendarUtil.dbDateTimeToCalendar(dbDateTime));
			} else if (dateTimeType == Date.class) {
				input.setValue(keyToInject, DateUtil.getInstance().parse(dbDateTime, DateFormatEnum.DATE_TIME));
			} else {
				input.setValue(keyToInject, dbDateTime);
			}
		} catch (Exception ex) {
			input.setValue(keyToInject, null);
		}
		return chain.invoke();
	}

	@Override
	public void destroy() {
	}
}