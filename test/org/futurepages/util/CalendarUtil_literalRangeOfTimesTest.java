package org.futurepages.util;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;

import org.futurepages.enums.DateFormatEnum;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Classe para o teste literalRangeOfTimes da classe CalendarUtil
 * @author wilton
 *
 */
@RunWith(Parameterized.class)
public class CalendarUtil_literalRangeOfTimesTest {

	private Calendar calInicio;
	private Calendar calFim;
	private String esperado;
	private String msg;

	public CalendarUtil_literalRangeOfTimesTest(String dataInicio, String dataFim, String esperado,String msg) {
		calInicio = createCalendar(dataInicio);
		calFim = createCalendar(dataFim);
		this.esperado = esperado;
		this.msg = msg;
	}

	private Calendar createCalendar(String strDate){
		Calendar cal = new GregorianCalendar();
		cal.setTime(DateUtil.parse(strDate,DateFormatEnum.DATE_TIME_PT_BR.getMask("/")));
		return cal;
	}

	@Before
	public void setUp(){
	}

	/**
	 * construção de array de parçametros para o construtor:
	 * dataInicio, dataFim, saída esperada, msg
	 */
	@Parameters
	public static Collection parameters() {
		Collection col =  Arrays.asList(new Object[][] {
			{"01/01/2009 10:00:00", "01/01/2009 10:15:00", "de 10h às 10h15",          "INTERVALO DO MESMO DIA COM HORARIO DE INICIO COM MINUTOS IGUAIS A ZERO"},
			{"01/01/2009 10:15:00", "02/01/2009 22:20:59", "",          "INTERVALO COM DIAS DIFERENTES"},
			{"01/01/2009 00:00:00", "01/01/2009 23:59:59", "",          "SEM HORÁRIO INICIO e FIM PREENCHIDO"},
			{"01/01/2009 10:15:00", "01/01/2009 10:15:00", "às 10h15",          "INTERVALO DO MESMO DIA COM MESMO HORARIO DE INICIO E FIM"},
			{"01/01/2009 00:00:00", "01/01/2009 13:30:00", "até 13h30",			 "SEM HORÁRIO INICIO PREENCHIDO"},
			{"01/01/2009 12:00:00", "01/01/2009 23:59:00", "a partir de 12h",	 "HORARIO FINAL VAZIO SO TEM HORARIO INICIAL"},
			{"01/01/2009 00:30:00", "01/01/2009 23:49:00", "de 0h30 às 23h49",	 "HORA MAIOR QUE 1 E MENOR QUE 23"},
			{"01/01/2009 00:30:00", "01/01/2009 01:30:00", "de 0h30 à 1h30",	 "HORA FINAL MENOR IGUAL A 1"},
                        {"01/01/2009 00:00:00", "01/01/2009 00:00:00", "",			 "SEM HORÁRIO DE INICIO E FIM PREENCHIDO"}

		});
		return col;
	}

	private void literalRangeOfTimesProcedure(Calendar calInicio, Calendar calFim, String esperado, String msg) {
		String result = CalendarUtil.literalRangeOfTimes(calInicio, calFim);
		String msgErro = "Erro para a seguinte caso: "+msg;
		Assert.assertEquals(msgErro,esperado, result);
	}

	@Test
	public void testliteralRangeOfTimes(){
		this.literalRangeOfTimesProcedure(this.calInicio, this.calFim, this.esperado, this.msg);
	}

}

