package org.futurepages.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.futurepages.util.brazil.BrazilCalendarUtil;
import org.futurepages.util.brazil.enums.UnitTimeEnum;
import org.futurepages.util.brazil.BrazilDateUtil;
import org.junit.Assert;
import org.junit.Test;

public class CalendarUtilTest {
	private static final String CALENDAR_MASK = "dd/MM/yyyy HH:mm:ss";
	/**
	 * CalMinuendo -  CalSubtraendo = CalDifefença
	 * @throws ParseException
	 */
	public static void testGetElapsedTime_CALENDAR_START_CALENDAR_END__TestProcedure(String start, String end, 
			int[] expectedResult, String msg) 
		throws ParseException{
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(CALENDAR_MASK);
		
		Calendar calStart = Calendar.getInstance(); 
		Calendar calEnd = Calendar.getInstance(); 
		calStart.setTime(dateFormat.parse(start));
		calEnd.setTime(dateFormat.parse(end));
		
		testGetElapsedTime_CALENDAR_START_CALENDAR_END__TestProcedure(
				expectedResult, calStart, calEnd, msg);
 
	}

	private static void testGetElapsedTime_CALENDAR_START_CALENDAR_END__TestProcedure(int[] expectedResult, 
			Calendar calStart, Calendar calEnd, String msg) {
		int[] atualResult = BrazilCalendarUtil.getElapsedTime(calStart, calEnd);
		Assert.assertEquals(msg+" ANO" ,expectedResult[0], atualResult[0]);
		Assert.assertEquals(msg+" MES" ,expectedResult[1], atualResult[1]);
		Assert.assertEquals(msg+" DIA" ,expectedResult[2], atualResult[2]);
		Assert.assertEquals(msg+" HORA" ,expectedResult[3], atualResult[3]);
		Assert.assertEquals(msg+" MINUTO" ,expectedResult[4], atualResult[4]);
	}

	@Test
	public void testSubtract_CalendarsEquals() throws ParseException{
		String end   = "04/04/2009 00:00:00";
		String start = "04/04/2009 00:00:00";
		testGetElapsedTime_CALENDAR_START_CALENDAR_END__TestProcedure(start, end, new int[]{0,0,0,0,0}, "");
	}
	
	@Test
	public void testSubtract_umAnoApos() throws ParseException{
		String end   = "04/04/1999 00:00:00";
		String start = "04/04/1998 00:00:00";
		testGetElapsedTime_CALENDAR_START_CALENDAR_END__TestProcedure(start, end, new int[]{1,0,0,0,0}, "");
	}

	@Test
	public void testSubtract_doisAnoApos() throws ParseException{
		String end   = "04/04/2010 00:00:00";
		String start = "04/04/2008 00:00:00";
		testGetElapsedTime_CALENDAR_START_CALENDAR_END__TestProcedure(start, end, new int[]{2,0,0,0,0}, "");
	}

	@Test
	public void testSubtract_umMesApos() throws ParseException{
		String end   = "04/05/2009 00:00:00";
		String start = "04/04/2009 00:00:00";
		testGetElapsedTime_CALENDAR_START_CALENDAR_END__TestProcedure(start, end, new int[]{0,1,0,0,0}, "");
	}

	@Test
	public void testSubtract_umDiaApos() throws ParseException{
		String end   = "05/04/2009 00:00:00";
		String start = "04/04/2009 00:00:00";
		testGetElapsedTime_CALENDAR_START_CALENDAR_END__TestProcedure(start, end, new int[]{0,0,1,0,0}, "");
	}
	
	@Test
	public void testSubtract_umDiaUmMesUmAnoApos() throws ParseException{
		String end   = "05/04/2009 00:00:00";
		String start = "04/04/2008 00:00:00";
		testGetElapsedTime_CALENDAR_START_CALENDAR_END__TestProcedure(start, end, new int[]{1,0,1,0,0}, "");
	}
	
	@Test
	public void testSubtract_anosDiferentes_DiferencaMenorUmAno() throws ParseException{
		String end   = "01/01/2009 00:00:00";
		String start = "31/12/2008 00:00:00";
		testGetElapsedTime_CALENDAR_START_CALENDAR_END__TestProcedure(start, end, new int[]{0,0,1,0,0}, "");
	}
	
	@Test
	public void testSubtract_mesesDiferentes_DiferencaMenorUmMes() throws ParseException{
		String end   = "01/04/2008 00:00:00";
		String start = "31/03/2008 00:00:00";
		testGetElapsedTime_CALENDAR_START_CALENDAR_END__TestProcedure(start, end, new int[]{0,0,1,0,0}, "");
	}

	@Test
	public void testSubtract_mesesDiferentes_FimAntesInicio() throws ParseException{
		String start   = "01/04/2008 00:00:00";
		String end = "31/03/2008 00:00:00";
		testGetElapsedTime_CALENDAR_START_CALENDAR_END__TestProcedure(start, end, new int[]{0,0,1,0,0}, "");
	}
	
	@Test
	public void testSubtract_umaHoraApos() throws ParseException{
		String start = "01/03/2008 00:00:00";
		String end =   "01/03/2008 01:00:00";
		testGetElapsedTime_CALENDAR_START_CALENDAR_END__TestProcedure(start, end, new int[]{0,0,0,1,0}, "");
	}
	
	@Test
	public void testSubtract_horasDiferentes_DiferencaMenorUmaHora() throws ParseException{
		String start = "01/03/2008 00:49:00";
		String end =   "01/03/2008 01:00:00";
		testGetElapsedTime_CALENDAR_START_CALENDAR_END__TestProcedure(start, end, new int[]{0,0,0,0,11}, "");
	}
	
	@Test
	public void testSubtract_viradaDoDia() throws ParseException{
		String start = "01/03/2008 23:59:00";
		String end =   "02/03/2008 00:01:00";
		testGetElapsedTime_CALENDAR_START_CALENDAR_END__TestProcedure(start, end, new int[]{0,0,0,0,2}, "");
	}

	@Test
	public void testSubtract_umaMinutoApos() throws ParseException{
		String start = "01/03/2008 00:58:00";
		String end =   "01/03/2008 00:59:00";
		testGetElapsedTime_CALENDAR_START_CALENDAR_END__TestProcedure(start, end, new int[]{0,0,0,0,1}, "");
	}
	
	@Test
	public void testSubtract_leandro() throws ParseException{
		String start = "29/02/2003 00:00:00";
		String end =   "29/02/2004 00:59:00";
		testGetElapsedTime_CALENDAR_START_CALENDAR_END__TestProcedure(start, end, new int[]{0,11,28,0,59}, "");
	}

	@Test
	public void testSubtract_startNull() throws ParseException{
		testGetElapsedTime_CALENDAR_START_CALENDAR_END__TestProcedure(new int[]{0,0,0,0,0},null, Calendar.getInstance(), "");
	}
	
	private void getElapsedTimeStatementTestProcedure(String msg, int[] time, String expected) throws CalendarUtil.TooBigDateException{
		getElapsedTimeStatementTestProcedure(msg, time, expected, UnitTimeEnum.YEAR,1);
	}

	private void getElapsedTimeStatementTestProcedure(String msg, int[] time, String expected,UnitTimeEnum unitLimit,int limitValue) throws CalendarUtil.TooBigDateException{
		String result = BrazilCalendarUtil.getElapsedTimeStatement(time, unitLimit, limitValue);
		Assert.assertEquals(msg, expected, result);
	}

	@Test
	public void testGetElapsedTimeStatement_maiorAno_UM() throws CalendarUtil.TooBigDateException{
		int[] time = new int[]{1,2,3,4,5};
		getElapsedTimeStatementTestProcedure("", time, "1 ano e 2 meses");
	}
	
	@Test
	public void testGetElapsedTimeStatement_maiorMes_UM() throws CalendarUtil.TooBigDateException{
		int[] time = new int[]{0,1,3,4,5}; 
		getElapsedTimeStatementTestProcedure("", time, "1 mês e 3 dias");
	}
	
	@Test
	public void testGetElapsedTimeStatement_maiorDia_UM() throws CalendarUtil.TooBigDateException{
		int[] time = new int[]{0,0,1,4,5};
		getElapsedTimeStatementTestProcedure("", time, "1 dia e 4h");
	}
	
	@Test
	public void testGetElapsedTimeStatement_maiorHora_UM() throws CalendarUtil.TooBigDateException{
		int[] time = new int[]{0,0,0,1,5}; 
		getElapsedTimeStatementTestProcedure("", time, "1h e 5min");
	}
	
	@Test
	public void testGetElapsedTimeStatement_maiorMinuto_UM() throws CalendarUtil.TooBigDateException {
		int[] time = new int[]{0,0,0,0,1}; 
		getElapsedTimeStatementTestProcedure("", time, "1min");
	}
	
	@Test
	public void testGetElapsedTimeStatement_maiorTodosZerados() throws CalendarUtil.TooBigDateException {
		int[] time = new int[]{0,0,0,0,0}; 
		getElapsedTimeStatementTestProcedure("", time, "menos de 1 minuto");
	}
	
	@Test
	public void testGetElapsedTimeStatement_maiorAnoMaior_UM() throws CalendarUtil.TooBigDateException {
		int[] time = new int[]{2,2,3,4,5};
		getElapsedTimeStatementTestProcedure("", time, "2 anos e 2 meses",UnitTimeEnum.YEAR,2);
	}
	
	@Test
	public void testGetElapsedTimeStatement_maiorMesMaior_UM() throws CalendarUtil.TooBigDateException {
		int[] time = new int[]{0,2,3,4,5}; 
		getElapsedTimeStatementTestProcedure("", time, "2 meses e 3 dias");
	}

	@Test
	public void testGetElapsedTimeStatement_maiorMes_IgualUM() throws CalendarUtil.TooBigDateException {
		int[] time = new int[]{0,1,3,4,5}; 
		getElapsedTimeStatementTestProcedure("", time, "1 mês e 3 dias");
	}
	
	@Test
	public void testGetElapsedTimeStatement_maiorDiaMaior_UM() throws CalendarUtil.TooBigDateException {
		int[] time = new int[]{0,0,2,4,5};
		getElapsedTimeStatementTestProcedure("", time,  "2 dias e 4h");
	}
	
	/*
	 * os valores para os minutos(após hora) estão zerados
	 */
	@Test
	public void testGetElapsedTimeStatement_maiorDiaMaior_ProximoValorZero() throws CalendarUtil.TooBigDateException{
		int[] time = new int[]{0,0,2,0,5};
		getElapsedTimeStatementTestProcedure("", time,  "2 dias");
	}
	
	@Test
	public void testGetElapsedTimeStatement_maiorHoraMaior_UM() throws CalendarUtil.TooBigDateException{
		int[] time = new int[]{0,0,0,2,5}; 
		getElapsedTimeStatementTestProcedure("", time, "2h e 5min");
	}
	
	@Test
	public void testGetElapsedTimeStatement_valoresNegativos() throws CalendarUtil.TooBigDateException{
		int[] time = new int[]{0,-3,0,0,2}; 
		getElapsedTimeStatementTestProcedure("", time, "menos de 1 minuto");
	}

	@Test(expected=CalendarUtil.TooBigDateException.class)
	public void testGetElapsedTimeStatement_unidadeLimiteInferiorMaiorUnidade() throws CalendarUtil.TooBigDateException{
		int[] time = new int[]{1,0,0,0,2}; 
		getElapsedTimeStatementTestProcedure("",time,"",UnitTimeEnum.MONTH,8);
	}
	
	@Test(expected=CalendarUtil.TooBigDateException.class)
	public void testGetElapsedTimeStatement_unidadeLimiteIgualMaiorUnidade_valorLimiteInferior() throws CalendarUtil.TooBigDateException{
		int[] time = new int[]{0,3,0,0,2}; 
		getElapsedTimeStatementTestProcedure("",time,"",UnitTimeEnum.MONTH,2);
	}
	
	@Test
	public void testGetElapsedTimeStatement_unidadeLimiteIgualMaiorUnidade_valorLimiteSuperior() throws CalendarUtil.TooBigDateException{
		int[] time = new int[]{0,3,0,0,2}; 
		getElapsedTimeStatementTestProcedure("",time,"3 meses",UnitTimeEnum.MONTH,4);
	}
	
	@Test
	public void testGetElapsedTimeFromNowStatement() throws ParseException{
		Calendar start = Calendar.getInstance();
		start.add(Calendar.YEAR,-1);
		String result = BrazilCalendarUtil.getElapsedTimeUntilNowStatement(start, UnitTimeEnum.MONTH, 0);
		Assert.assertEquals(BrazilDateUtil.viewDateTime(start),result);
	}
	
	
	private void compareCalendarDateTestProcedure(String msg, String cal1,String cal2,int esperado ) throws ParseException{
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Calendar calendar1 = Calendar.getInstance(); 
			calendar1.setTime(dateFormat.parse(cal1));
		Calendar calendar2 = Calendar.getInstance(); 
			calendar2.setTime(dateFormat.parse(cal2));
		int result = BrazilCalendarUtil.compareCalendarDate(calendar1, calendar2);
		Assert.assertEquals(msg,esperado, result);
	}
	
	@Test
	public void testcompareCalendarDate_PrimeiroAnoMaior() throws ParseException{
		compareCalendarDateTestProcedure("Primeiro ano maior.", "01/03/2008", "01/03/2007", 1);
	}
	
	@Test
	public void testcompareCalendarDate_AnoIgualMesDeferente() throws ParseException{
		compareCalendarDateTestProcedure("Primeiro mes menor.", "01/02/2008", "01/03/2008", -1);
	}
	
	@Test
	public void testcompareCalendarDate_AnoIgualMesIGualDiaDiferente() throws ParseException{
		compareCalendarDateTestProcedure("Primeiro dia maior.", "02/03/2008", "01/03/2008", 1);
	}
	
	@Test
	public void testcompareCalendarDate_AnoMesDiaIgual() throws ParseException{
		compareCalendarDateTestProcedure("Primeiro ano maior.", "01/03/2008", "01/03/2008", 0);
	}
	

	private void getDifferenceInDays_testProcedure(Calendar c1,Calendar c2, int esperado, String msg){
		int result = BrazilCalendarUtil.getDifferenceInDays(c1, c2);
		Assert.assertEquals(msg,esperado, result);
	}

	@Test
	public void testGetDifferenceInDays_datasIguais(){
		Calendar c1 = Calendar.getInstance();
		getDifferenceInDays_testProcedure(c1, c1, 0, "Erro quando as duas datas são iguais");
	}

	@Test
	public void testIsCalendarDateEquals(){
		Calendar c1 = CalendarUtil.dbDateTimeToCalendar("2011-11-03 00:06:00");
		Calendar c2 = CalendarUtil.dbDateTimeToCalendar("2011-11-03 07:06:00");
		Calendar c3 = CalendarUtil.dbDateTimeToCalendar("2011-11-03 11:59:00");

		Calendar c3_ = CalendarUtil.dbDateTimeToCalendar("2012-11-03 20:59:00");
		Calendar c4 = CalendarUtil.dbDateTimeToCalendar("2011-11-13 01:59:00");
		Calendar c5 = CalendarUtil.dbDateTimeToCalendar("2011-12-03 18:59:00");

		Assert.assertTrue(BrazilCalendarUtil.isCalendarDateEquals(c1, c1));
		Assert.assertTrue(BrazilCalendarUtil.isCalendarDateEquals(c1, c2));
		Assert.assertTrue(BrazilCalendarUtil.isCalendarDateEquals(c2, c1));
		Assert.assertTrue(BrazilCalendarUtil.isCalendarDateEquals(c2, c2));
		Assert.assertTrue(BrazilCalendarUtil.isCalendarDateEquals(c1, c3));
		Assert.assertTrue(BrazilCalendarUtil.isCalendarDateEquals(c3, c1));
		Assert.assertTrue(BrazilCalendarUtil.isCalendarDateEquals(c3, c3));

		Assert.assertFalse(BrazilCalendarUtil.isCalendarDateEquals(c3, c3_));
		Assert.assertFalse(BrazilCalendarUtil.isCalendarDateEquals(c3_, c3));
		Assert.assertFalse(BrazilCalendarUtil.isCalendarDateEquals(c1, c4));
		Assert.assertFalse(BrazilCalendarUtil.isCalendarDateEquals(c4, c1));
		Assert.assertFalse(BrazilCalendarUtil.isCalendarDateEquals(c1, c5));
	}

	@Test
	public void testIsNeighborDays(){
		Calendar c1 = CalendarUtil.dbDateTimeToCalendar("2011-11-03 00:06:00");
		Calendar c2 = CalendarUtil.dbDateTimeToCalendar("2011-11-04 07:06:00");
		Calendar c3 = CalendarUtil.dbDateTimeToCalendar("2011-11-05 11:59:00");
		Calendar c4 = CalendarUtil.dbDateTimeToCalendar("2011-11-06 20:59:00");
		Calendar c5 = CalendarUtil.dbDateTimeToCalendar("2011-11-07 01:59:00");
		Calendar c6 = CalendarUtil.dbDateTimeToCalendar("2011-11-08 18:59:00");
		Calendar c7 = CalendarUtil.dbDateTimeToCalendar("2012-11-09 18:59:00");
		Calendar c8 = CalendarUtil.dbDateTimeToCalendar("2012-10-08 00:06:00");
		Assert.assertFalse(BrazilCalendarUtil.isNeighborDays(c1, c1));
		Assert.assertTrue(BrazilCalendarUtil.isNeighborDays(c1, c2));
		Assert.assertFalse(BrazilCalendarUtil.isNeighborDays(c1, c3));
		Assert.assertFalse(BrazilCalendarUtil.isNeighborDays(c1, c4));
		Assert.assertFalse(BrazilCalendarUtil.isNeighborDays(c1, c5));
		Assert.assertFalse(BrazilCalendarUtil.isNeighborDays(c1, c6));
		Assert.assertFalse(BrazilCalendarUtil.isNeighborDays(c1, c7));
		Assert.assertFalse(BrazilCalendarUtil.isNeighborDays(c1, c8));
		Assert.assertFalse(BrazilCalendarUtil.isNeighborDays(c1, c1));

		Assert.assertTrue(BrazilCalendarUtil.isNeighborDays(c2, c3));
		Assert.assertTrue(BrazilCalendarUtil.isNeighborDays(c3, c4));
		Assert.assertTrue(BrazilCalendarUtil.isNeighborDays(c4, c5));
		Assert.assertTrue(BrazilCalendarUtil.isNeighborDays(c5, c6));
		Assert.assertFalse(BrazilCalendarUtil.isNeighborDays(c6, c7));
		Assert.assertFalse(BrazilCalendarUtil.isNeighborDays(c7, c8));
	}
	
	@Test
	public void testGetDifferenceInDays_segundaDataMaior_DiferencaPequena(){
		for (int i = 0; i < 99999; i++) {
			getDiffernece_segundaDataMaiorTestProcedure(i);
		}
	}
	
	private void getDiffernece_segundaDataMaiorTestProcedure(int diff) {
		Calendar c1 = BrazilCalendarUtil.buildCalendar(Calendar.DATE, -diff);
		Calendar c2 = Calendar.getInstance();
		getDifferenceInDays_testProcedure(c1, c2, diff, "Erro com quando a segunda data é Maior e a diferença é "+diff);
	}
	
	@Test
	public void testGetDifferenceInDays_segundaDataMenor_DiferencaPequena(){
		for (int i = 0; i < 9999; i++) {
			getDiffernece_segundaDataMenorTestProcedure(i);
		}
	}

	private void getDiffernece_segundaDataMenorTestProcedure(int diff) {
		Calendar c1 = BrazilCalendarUtil.buildCalendar(Calendar.DATE, -diff);
		Calendar c2 = Calendar.getInstance();
		getDifferenceInDays_testProcedure(c2, c1, diff, "Erro com quando a segunda data é Menor e a diferença é "+diff);
	}
	
	private void isLeapYearTestProcedure(String msg, int year, boolean expected){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		boolean result = BrazilCalendarUtil.isLeapYear(cal);
		Assert.assertEquals("Erro no método quando o ano é "+msg, expected, result);
	}
	
	@Test
	public void testeIsLeapYear_anoBisexto(){
		isLeapYearTestProcedure("bisexto", 2008, true);
	}

	@Test
	public void testeIsLeapYear_anoNaoBisexto(){
		isLeapYearTestProcedure("não bisexto", 2009, false);
	}
	
	private void getOrdinalDayOfYearTestProcedure(String errorMsg, String data, int esperado) {
		Date date = DateUtil.getInstance().parse(data, "dd/MM/yyyy");
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int result = BrazilCalendarUtil.getOrdinalDayOfYear(cal);
		Assert.assertEquals(errorMsg, esperado, result);
	}

	/**
	 * jan: 31
	 * fev: 29
	 * corretivo: 0
	 */
	@Test
	public void testeGetOrdinalDayOfYear_bisexto_antesMarco(){
		getOrdinalDayOfYearTestProcedure("ano bisexto e dia antes de março.","29/02/2008", 60);
	}

	/**
	 * jan: 31
	 * fev: 29
	 * mar: 1
	 * corretivo: 0
	 */
	@Test
	public void testeGetOrdinalDayOfYear_bisexto_Marco(){
		getOrdinalDayOfYearTestProcedure("ano bisexto e dia em março.","01/03/2008", 61);
	}

	/**
	 * jan: 31
	 * fev: 28
	 * corretivo: 0
	 */
	@Test
	public void testeGetOrdinalDayOfYear_naoBisexto_antesMarco(){
		getOrdinalDayOfYearTestProcedure("ano não bisexto e dia antes de março.","28/02/2009", 59);
	}
	
	/**
	 * jan: 31
	 * fev: 28
	 * mar: 1
	 * corretivo: 1
	 */
	@Test
	public void testeGetOrdinalDayOfYear_naoBisexto_Marco(){
		getOrdinalDayOfYearTestProcedure("ano não bisexto e dia em março","01/03/2009", 61);
	}

	@Test
	public void testeGetOrdinalDayOfYear_naoBisexto_Dezembro(){
		getOrdinalDayOfYearTestProcedure("ano não bisexto e dia em março","31/12/2009", 366);
	}
	
}