package org.futurepages.util;

import static org.junit.Assert.assertEquals;

import org.futurepages.util.brazil.NumberUtil;
import org.junit.Test;

/**
 *
 * @author zezim
 */
public class NumberUtilTest {

	@Test
	public void numeroPorExteso() throws Exception {
		assertEquals("zero", NumberUtil.numeroPorExteso("0"));
		assertEquals("um", NumberUtil.numeroPorExteso("1"));
		assertEquals("dez", NumberUtil.numeroPorExteso("10"));
		assertEquals("doze", NumberUtil.numeroPorExteso("12"));
		assertEquals("dezessete", NumberUtil.numeroPorExteso("17"));
		assertEquals("trinta e três", NumberUtil.numeroPorExteso("33"));
		assertEquals("cinquenta e cinco", NumberUtil.numeroPorExteso("55"));
		assertEquals("noventa e nove", NumberUtil.numeroPorExteso("99"));
		assertEquals("mil e um", NumberUtil.numeroPorExteso("1001"));
		assertEquals("mil e vinte e um", NumberUtil.numeroPorExteso("1021"));
		assertEquals("dois mil e noventa e nove", NumberUtil.numeroPorExteso("2099"));
		assertEquals("mil novecentos e noventa e três", NumberUtil.numeroPorExteso("1993"));
		assertEquals("um milhão, onze mil novecentos e noventa e três", NumberUtil.numeroPorExteso("1011993"));
		assertEquals("um milhão e nove", NumberUtil.numeroPorExteso("1000009"));
		assertEquals("quinze milhões, cento e dezenove mil novecentos e noventa e três", NumberUtil.numeroPorExteso("15119993"));
	}
}
