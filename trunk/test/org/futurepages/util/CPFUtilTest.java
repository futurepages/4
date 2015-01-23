package org.futurepages.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CPFUtilTest {

    @Test
    public void geraCPF() {
        String cpfGerado = CPFUtil.geraCPF();
        assertNotNull("O CPF não deveria ser nulo.",cpfGerado);
        assertEquals("O tamanho do CPF deveria ser 11.",11,cpfGerado.length());
        assertTrue("O CPF gerado não é válido.",CPFUtil.validaCPF(cpfGerado));
    }

	@Test
    public void formataCPF() {
        assertEquals("O CPF 00351063366 não foi formatado corretamente.","003.510.633-66",CPFUtil.formata("00351063366"));
    }

    @Test
    public void validaCPF() {
        String cpfComLetras             = "abcdefghijk";
        String cpfInvalidoIguais        = "11111111111";
        String cpfInvalidoQualquer      = "22022030303";
        String cpfInvalidoMenor         = "22";
        String cpfCaracteresSeparadores = "003.510.633-66";
        String cpfComEspacos   			= " 00351063366 ";
        String cpfValido       			= "00351063366";

        assertFalse("CPF null não deveria ser válido", CPFUtil.validaCPF(null));
        assertFalse("CPF com letras não deveria ser válido",CPFUtil.validaCPF(cpfComLetras));        
        assertFalse("O CPF com caracteres iguais não deveria ser válido", CPFUtil.validaCPF(cpfInvalidoIguais));
        assertFalse("Um CPF não-válido foi validado.",CPFUtil.validaCPF(cpfInvalidoQualquer));
        assertFalse("Um CPF com menos de 11 caracteres não deveria ser válido.",CPFUtil.validaCPF(cpfInvalidoMenor));
        assertFalse("O CPF não deveria ser válido", CPFUtil.validaCPF(cpfCaracteresSeparadores));
        assertFalse("O CPF não deveria ser válido", CPFUtil.validaCPF(cpfComEspacos));
        
        assertTrue("O CPF deveria ser válido", CPFUtil.validaCPF(cpfValido));
    }
}