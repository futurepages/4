package org.futurepages.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 *
 * @author leandro
 */
public class SEOUtilTest {

	public void urlFormatTestProcedure(String msg ,String url, String esperado){
		String result = SEOUtil.urlFormat(url);
        assertEquals(esperado, result);
	}
	
	@Test
	public void urlFormat_ComEspacos() {
        String strIn = "Ma sorte em jogo";
        String expResult = "ma-sorte-em-jogo";
        urlFormatTestProcedure("erro quanto a url possui espaços",strIn, expResult);
	}

	@Test
    public void urlFormat_ComHifen() {
        String strIn = "(pes-de-moleque)";
        String expResult = "pes_de_moleque";
        urlFormatTestProcedure("erro quanto a url possui espaços",strIn, expResult);
    }

	@Test
	public void urlFormat_caracteresEspeciais() {
		String strIn = "éá:<>ñ:öåù@";
		String expResult = "eanoau_";
		urlFormatTestProcedure("erro quanto a url possui espaços",strIn, expResult);
	}
}