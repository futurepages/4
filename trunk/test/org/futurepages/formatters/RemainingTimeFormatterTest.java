package org.futurepages.formatters;

import junit.framework.Assert;
import org.futurepages.formatters.brazil.RemainingTimeFormatter;
import org.futurepages.util.CalendarUtil;
import org.junit.Test;

public class RemainingTimeFormatterTest {


	@Test
	public void testFormat() {
		assertFormatValue("2011-11-07 00:00:00", "2011-11-07 23:31:20", "hoje às 23:31");
		assertFormatValue("2011-11-07 00:30:35", "2011-11-07 23:59:59", "hoje às 23:59");
		assertFormatValue("2011-11-07 23:30:35", "2011-11-07 23:32:20", "hoje às 23:32");
		assertFormatValue("2011-11-07 23:30:35", "2011-11-07 23:59:59", "hoje às 23:59");

		assertFormatValue("2011-11-07 23:30:35", "2011-11-08 00:00:00", "amanhã às 00:00");
		assertFormatValue("2011-11-07 23:30:35", "2011-11-08 04:29:00", "amanhã às 04:29");
		assertFormatValue("2011-11-07 23:30:35", "2011-11-08 06:29:00", "amanhã às 06:29");
		assertFormatValue("2011-11-07 23:30:35", "2011-11-08 12:29:00", "amanhã às 12:29");
		assertFormatValue("2011-11-07 23:30:35", "2011-11-08 15:29:00", "amanhã às 15:29");
		assertFormatValue("2011-11-07 23:30:35", "2011-11-08 22:00:00", "amanhã às 22:00");
		assertFormatValue("2011-11-07 23:30:35", "2011-11-08 23:39:00", "amanhã às 23:39");

		assertFormatValue("2011-11-07 23:30:35", "2011-11-09 23:29:00", "em 9 de novembro");
		assertFormatValue("2011-11-07 23:30:35", "2011-12-09 23:29:00", "em 9 de dezembro");

		assertFormatValue("2011-11-07 23:30:35", "2012-01-10 01:10:00", "em 10 de janeiro");
		assertFormatValue("2011-11-07 23:30:35", "2012-02-10 01:10:00", "em 10 de fevereiro");
		assertFormatValue("2011-11-07 23:30:35", "2012-03-10 01:10:00", "em 10 de março");
		assertFormatValue("2011-11-07 23:30:35", "2012-04-10 01:10:00", "em 10 de abril");
		assertFormatValue("2011-11-07 23:30:35", "2012-05-10 01:10:00", "em 10 de maio");
		assertFormatValue("2011-11-07 23:30:35", "2012-06-10 01:10:00", "em 10 de junho");
		assertFormatValue("2011-11-07 23:30:35", "2012-07-10 01:10:00", "em 10 de julho de 2012");
		assertFormatValue("2011-11-07 23:30:35", "2012-08-10 01:10:00", "em 10 de agosto de 2012");
		assertFormatValue("2011-11-07 23:30:35", "2012-09-10 01:10:00", "em 10 de setembro de 2012");
		assertFormatValue("2011-11-07 23:30:35", "2012-10-01 01:10:00", "em 1º de outubro de 2012");
		assertFormatValue("2011-11-03 23:30:35", "2012-11-01 23:39:00", "em 1º de novembro de 2012");
		assertFormatValue("2011-11-07 23:30:35", "2012-12-01 23:39:00", "em 1º de dezembro de 2012");

		assertFormatValue("2011-11-07 23:30:35", "2013-01-01 23:39:00", "em 1º de janeiro de 2013");
		assertFormatValue("2011-11-07 23:30:35", "2013-02-02 23:39:00", "em 2 de fevereiro de 2013");
		assertFormatValue("2011-11-07 23:30:35", "2013-04-03 23:39:00", "em 3 de abril de 2013");
		assertFormatValue("2011-11-07 23:30:35", "2013-05-04 23:39:00", "em 4 de maio de 2013");
		assertFormatValue("2011-11-07 23:30:35", "2013-06-05 23:39:00", "em 5 de junho de 2013");
		assertFormatValue("2011-11-07 23:30:35", "2013-07-10 23:39:00", "em 10 de julho de 2013");
		assertFormatValue("2011-11-07 23:30:35", "2013-08-29 23:39:00", "em 29 de agosto de 2013");
		assertFormatValue("2011-11-07 23:30:35", "2013-09-30 23:39:00", "em 30 de setembro de 2013");
		assertFormatValue("2011-11-07 23:30:35", "2013-10-02 23:39:00", "em 2 de outubro de 2013");
		assertFormatValue("2011-11-07 23:30:35", "2013-11-01 23:39:00", "em 1º de novembro de 2013");
		assertFormatValue("2011-11-07 23:30:35", "2013-12-01 23:39:00", "em 1º de dezembro de 2013");
		
		assertFormatValue("2011-01-01 23:30:35", "2011-02-01 23:39:00", "em 1º de fevereiro");
		assertFormatValue("2011-01-01 23:30:35", "2011-04-01 23:39:00", "em 1º de abril");
		assertFormatValue("2011-01-01 23:30:35", "2011-06-01 23:39:00", "em 1º de junho");
		assertFormatValue("2011-01-01 23:30:35", "2011-08-01 23:39:00", "em 1º de agosto");
		assertFormatValue("2011-01-01 23:30:35", "2011-10-01 23:39:00", "em 1º de outubro");
		assertFormatValue("2011-01-01 23:30:35", "2011-12-01 23:39:00", "em 1º de dezembro");

		assertFormatValue("2011-01-01 23:30:35", "2012-01-01 23:39:00", "em 1º de janeiro de 2012");
		assertFormatValue("2011-01-01 23:30:35", "2012-02-01 23:39:00", "em 1º de fevereiro de 2012");
		assertFormatValue("2011-01-01 23:30:35", "2012-04-01 23:39:00", "em 1º de abril de 2012");
		assertFormatValue("2011-01-01 23:30:35", "2012-06-01 23:39:00", "em 1º de junho de 2012");
		assertFormatValue("2011-01-01 23:30:35", "2012-10-01 23:39:00", "em 1º de outubro de 2012");
		assertFormatValue("2011-01-01 23:30:35", "2012-12-01 23:39:00", "em 1º de dezembro de 2012");
		assertFormatValue("2011-01-01 23:30:35", "2013-01-01 23:39:00", "em 1º de janeiro de 2013");
		assertFormatValue("2011-01-01 23:30:35", "2014-01-01 23:39:00", "em 1º de janeiro de 2014");
	}

	private void assertFormatValue(String bdBaseDateTime, String bdElapsedDateTime, String expectedText) {
		System.out.println(RemainingTimeFormatter.formatValue(CalendarUtil.dbDateTimeToCalendar(bdBaseDateTime), CalendarUtil.dbDateTimeToCalendar(bdElapsedDateTime)));


		Assert.assertEquals(
				expectedText,
				RemainingTimeFormatter.formatValue(CalendarUtil.dbDateTimeToCalendar(bdBaseDateTime),
				CalendarUtil.dbDateTimeToCalendar(bdElapsedDateTime))
				);
	}
}
