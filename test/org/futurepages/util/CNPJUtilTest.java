package org.futurepages.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.futurepages.util.brazil.CNPJUtil;
import org.junit.Test;

public class CNPJUtilTest {

	@Test
    public void formataCPF() {
        assertEquals("O CNPJ 78425986003615 não foi formatado corretamente.","78.425.986/0036-15", CNPJUtil.formata("78425986003615"));
    }	
	
    @Test
    public void validaCNPJ() {
        assertTrue("O CNPJ 04412146000103 deveria ser válido",CNPJUtil.validaCNPJ("04412146000103"));
        assertTrue("O CNPJ 73399354000124 deveria ser válido",CNPJUtil.validaCNPJ("73399354000124"));
        assertTrue("O CNPJ 06981344000105 deveria ser válido",CNPJUtil.validaCNPJ("06981344000105"));
        
        assertFalse("O CNPJ não deveria ser válido",CNPJUtil.validaCNPJ("06.981.344/0001-05"));
        assertFalse("O CNPJ não deveria ser válido",CNPJUtil.validaCNPJ("ABCDEFGHIJKLMN"));
        assertFalse("O CNPJ não deveria ser válido",CNPJUtil.validaCNPJ("00000000000000"));
        assertFalse("O CNPJ não deveria ser válido",CNPJUtil.validaCNPJ("11111111111111"));
        assertFalse("O CNPJ não deveria ser válido",CNPJUtil.validaCNPJ("22222222222222"));
        assertFalse("O CNPJ não deveria ser válido",CNPJUtil.validaCNPJ("33333333333333"));
        assertFalse("O CNPJ não deveria ser válido",CNPJUtil.validaCNPJ("44444444444444"));
        assertFalse("O CNPJ não deveria ser válido",CNPJUtil.validaCNPJ("55555555555555"));
        assertFalse("O CNPJ não deveria ser válido",CNPJUtil.validaCNPJ("66666666666666"));
        assertFalse("O CNPJ não deveria ser válido",CNPJUtil.validaCNPJ("77777777777777"));
        assertFalse("O CNPJ não deveria ser válido",CNPJUtil.validaCNPJ("88888888888888"));
        assertFalse("O CNPJ não deveria ser válido",CNPJUtil.validaCNPJ("99999999999999"));
        assertFalse("O CNPJ não deveria ser válido",CNPJUtil.validaCNPJ(""));
        assertFalse("O CNPJ não deveria ser válido",CNPJUtil.validaCNPJ("0"));
        assertFalse("O CNPJ não deveria ser válido",CNPJUtil.validaCNPJ("12"));
        assertFalse("O CNPJ não deveria ser válido",CNPJUtil.validaCNPJ("123"));
        assertFalse("O CNPJ não deveria ser válido",CNPJUtil.validaCNPJ("1234"));
        assertFalse("O CNPJ não deveria ser válido",CNPJUtil.validaCNPJ("12345"));
        assertFalse("O CNPJ não deveria ser válido",CNPJUtil.validaCNPJ("123456"));
        assertFalse("O CNPJ não deveria ser válido",CNPJUtil.validaCNPJ("1234567"));
        assertFalse("O CNPJ não deveria ser válido",CNPJUtil.validaCNPJ("12345678"));
        assertFalse("O CNPJ não deveria ser válido",CNPJUtil.validaCNPJ("123456789"));
        assertFalse("O CNPJ não deveria ser válido",CNPJUtil.validaCNPJ("1234567890"));
        assertFalse("O CNPJ não deveria ser válido",CNPJUtil.validaCNPJ("12345678901"));
        assertFalse("O CNPJ não deveria ser válido",CNPJUtil.validaCNPJ("123456789012"));
        assertFalse("O CNPJ não deveria ser válido",CNPJUtil.validaCNPJ("1234567890123"));
        assertFalse("O CNPJ não deveria ser válido",CNPJUtil.validaCNPJ("12345678901234"));
        assertFalse("O CNPJ não deveria ser válido",CNPJUtil.validaCNPJ("123456789012345"));
        assertFalse("O CNPJ não deveria ser válido",CNPJUtil.validaCNPJ("1234567890123456"));
    }

}