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
 * Classe para o teste literalRangeOfDates da classe CalendarUtil
 * @author Danilo
 *
 */
@RunWith(Parameterized.class)
public class BrazilianCalendarUtil_literalRangeOfDatesTest {

	private Calendar calInicio;
	private Calendar calFim;
	private Calendar dataCorrente;
	private String esperado;
	private String msg;
	
	public BrazilianCalendarUtil_literalRangeOfDatesTest(String mask, String dataInicio, String dataFim, String strDataCorrente, String esperado, String msg) {

		calInicio = createCalendar(dataInicio, mask);
		calFim = createCalendar(dataFim, mask);
		dataCorrente = createCalendar(strDataCorrente, mask);
		this.esperado = esperado;
		this.msg = msg;
	}

	private Calendar createCalendar(String strDate, String mask){
		Calendar cal = new GregorianCalendar();
		cal.setTime(BrazilianDateUtil.parse(strDate, mask));
		return cal;
	}

	@Before
	public void setUp(){
	}
	
//	ANO		MÊS		DIA		CORRENTE?

//	IGUAL	IGUAL	IGUAL	SIM
//	IGUAL	IGUAL	IGUAL	NÃO
//	IGUAL	IGUAL	DIFF	SIM
//	IGUAL	IGUAL	DIFF	NÃO

//	IGUAL	DIFF	IGUAL	SIM
//	IGUAL	DIFF	IGUAL	NÃO
//	IGUAL	DIFF	DIFF	SIM
//	IGUAL	DIFF	DIFF	NÃO

//	DIFF	IGUAL	IGUAL	SIM
//	DIFF	IGUAL	IGUAL	NÃO
//	DIFF	IGUAL	DIFF	SIM
//	DIFF	IGUAL	DIFF	NÃO

//	DIFF	DIFF	IGUAL	SIM
//	DIFF	DIFF	IGUAL	NÃO
//	DIFF	DIFF	DIFF	SIM
//	DIFF	DIFF	DIFF	NÃO
	/**
	 * construção de array de parçametros para o construtor:
	 * dataInicio, dataFim, anoCorrente, saída esperada, msg 
	 */
	@Parameters
	public static Collection<Object[]> parameters() {
		String mask = DateFormatEnum.DATE_PT_BR.getMask("/");
		Collection<Object[]> col =  Arrays.asList(new Object[][] {

			{mask, "01/01/2009", "01/01/2009 01:00:00", "01/01/2009", "1 de janeiro",					      "Tudo Igual."},
			{mask, "20/01/2009", "20/01/2009", "19/01/2009", "20 de janeiro",								  "Tudo Igual. Data Corrente antes do evento."},
			{mask, "20/01/2009", "20/01/2009", "21/01/2009", "20 de janeiro",								  "Tudo Igual. Data Corrente após evento."},

			{mask, "01/01/2009", "01/01/2009", "01/01/2008", "1 de janeiro de 2009",						  "ANO não corrente."},
			{mask, "01/01/2009", "02/01/2009", "01/01/2009", "1 a 2 de janeiro",							  "DIA diferente."},
			{mask, "01/01/2009", "02/01/2009", "01/01/2008", "1 a 2 de janeiro de 2009",					  "DIA diferente, ANO não corrente."},

			{mask, "01/01/2009", "01/02/2009", "01/01/2009", "1 de janeiro a 1 de fevereiro",				  "MES diferente."},
			{mask, "01/01/2009", "01/02/2009", "01/01/2008", "1 de janeiro a 1 de fevereiro de 2009",		  "MES diferente, ANO não corrente."},
			{mask, "01/01/2009", "02/02/2009", "01/01/2009", "1 de janeiro a 2 de fevereiro",				  "MES diferente DIA diferente."},
			{mask, "01/01/2009", "02/02/2009", "01/01/2008", "1 de janeiro a 2 de fevereiro de 2009",		  "MES diferente, DIA diferente, ANO não corrente."},

			{mask, "01/01/2009", "01/01/2010", "01/01/2009", "1 de janeiro de 2009 a 1 de janeiro de 2010",   "ANO diferente."},
			{mask, "01/01/2009", "01/01/2010", "01/01/2010", "1 de janeiro de 2009 a 1 de janeiro de 2010",   "ANO diferente, ANO não corrente."},
			{mask, "01/01/2009", "02/01/2010", "01/01/2009", "1 de janeiro de 2009 a 2 de janeiro de 2010",   "ANO diferente, DIA diferente."},
			{mask, "01/01/2009", "02/01/2010", "01/01/2010", "1 de janeiro de 2009 a 2 de janeiro de 2010",   "ANO diferente, DIA diferente, ANO não corrente."},

			{mask, "01/01/2009", "01/02/2010", "01/01/2009", "1 de janeiro de 2009 a 1 de fevereiro de 2010", "ANO diferente, MES diferente."},
			{mask, "01/01/2009", "01/02/2010", "01/01/2010", "1 de janeiro de 2009 a 1 de fevereiro de 2010", "ANO diferente, MES diferente, ANO não corrente."},
			{mask, "01/01/2009", "02/02/2010", "01/01/2009", "1 de janeiro de 2009 a 2 de fevereiro de 2010", "ANO diferente, MES diferente, DIA diferente."},
			{mask, "01/01/2009", "02/02/2010", "01/01/2010", "1 de janeiro de 2009 a 2 de fevereiro de 2010", "ANO diferente, MES diferente, DIA diferente, ANO não corrente."},
			{"dd/MM/yyyy hh:mm:ss", "01/01/2009 00:30:00", "01/01/2009 01:30:00", "01/01/2009 00:00:00", "1 de janeiro de 0h30 à 1h30",	 "HORA FINAL MENOR IGUAL A 1"},
			{"dd/MM/yyyy HH:mm:ss", "01/01/2009 01:00:00", "01/01/2009 23:00:00", "01/01/2009 00:00:00", "1 de janeiro de 1h às 23h",	 		 "DE 1h a 23h MINUTO INICIAL E FINAL IGUAL A ZERO"}
		});
		return col;
	}

	private void literalRangeOfDatesTestProcedure(Calendar calInicio, Calendar calFim,  Calendar dataCorrente, String esperado, String msg) {
		String result = BrazilianCalendarUtil.literalRangeOfDates(calInicio, calFim, dataCorrente);
		String msgErro = "Erro para a seguinte caso: "+msg;
		Assert.assertEquals(msgErro,esperado, result);

	}

	@Test
	public void testLiteralRangeOfDates(){
		this.literalRangeOfDatesTestProcedure(this.calInicio, this.calFim, this.dataCorrente, this.esperado, this.msg);
	}

}
