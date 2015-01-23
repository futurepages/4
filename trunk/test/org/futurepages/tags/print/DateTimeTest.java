package org.futurepages.tags.print;

import org.futurepages.tags.DateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.jsp.JspException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DateTimeTest {

	private DateTime tag;
	
	@Before
	public void setUp(){
		tag = new DateTime();
	}
	
	private void getStringToPrint_testProcedure(String msg,String mask,
			Calendar data, String esperada ) throws JspException{
		tag.setMask(mask);
		tag.setDate(data.getTime());
		Assert.assertEquals(esperada, tag.getStringToPrint());
	}
	
	@Test
	public void testeGetStringToPrint_maskReduzida() throws JspException{
		Calendar dataDia = new GregorianCalendar(2009 , 01 , 03);
		getStringToPrint_testProcedure("", "d/M/yyyy", dataDia, "3/2/2009");
	}
	
	@Test
	public void testeGetStringToPrint_maskReduzida_diaMesMaior09() throws JspException{
		Calendar dataDia = new GregorianCalendar(2009 , 11 , 21);
		getStringToPrint_testProcedure("", "d/M/yyyy", dataDia, "21/12/2009");
	}
}
