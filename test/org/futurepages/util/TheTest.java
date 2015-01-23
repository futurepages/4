package org.futurepages.util;

import org.junit.Assert;
import org.junit.Test;

public class TheTest {

    //Testes para firstTokenOF
    private void procedureTestFirstTokenOf(String string, String separador, String esperado) {
        String atual = The.firstTokenOf(string, separador);
        Assert.assertEquals(esperado, atual);
    }

    public void testeFirstTokenOf_null_null() {
        Assert.assertNull("Era pra ser null",The.firstTokenOf(null, null));
    }

    @Test(expected = NullPointerException.class)
    public void testeFirstTokenOf_vazia_null() {
        The.firstTokenOf("", null);
    }

    @Test
    public void testeFirstTokenOf_null_vazia() {
        Assert.assertNull("Era pra ser null",The.firstTokenOf(null, ""));
    }

    @Test
    public void testeFirstTokenOf_ASDF_sepInexistente() {
        procedureTestFirstTokenOf("asdf", "@", "asdf");
    }

    @Test
    public void testeFirstTokenOf_ASDF_sepPresente() {
        procedureTestFirstTokenOf("as@df", "@", "as");
    }

    @Test
    public void testeFirstTokenOf_ASDF_sepPresenteInicio() {
        procedureTestFirstTokenOf("@asdf@", "@", "asdf");
    }

    @Test
    public void testeFirstTokenOf_ASDF_ASDF() {
        procedureTestFirstTokenOf("asdf", "asdf", null);
    }

    @Test
    public void testeFirstTokenOf_ASDF_SD() {
        procedureTestFirstTokenOf("asdf", "sd", "a");
    }

    @Test
    public void testeFirstTokenOf_ASDF_QW() {
        procedureTestFirstTokenOf("asdf", "qw", "asdf");
    }

    //public void testes para The.tokenAt 
    private void procedureTestTokenAt(int pos, String fonte, String separador, String tokenEsperado) {
        String atual = The.tokenAt(pos, fonte, separador);
        Assert.assertEquals("Erro", tokenEsperado, atual);

    }

    @Test(expected = NullPointerException.class)
    public void testTokenAt_Null() {
        The.tokenAt(0, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testTokenAt_Null_texto_null() {
        The.tokenAt(0, "asdf", null);
    }

    @Test
    public void testTokenAt_PosNegativo() {
        this.procedureTestTokenAt(-1, "asdfgasdf", "w", null);
    }

    @Test
    public void testTokenAt_Pos1() {
        this.procedureTestTokenAt(1, "asdf@qwer", "@", "qwer");
    }

    @Test
    public void testTokenAt_PosDois() {
        this.procedureTestTokenAt(2, "asdf qwer tyiu", " ", "tyiu");
    }

    @Test
    public void testTokenAt_PosTres_Com2Tokens() {
        this.procedureTestTokenAt(3, "asdf asdf", " ", null);
    }

    @Test
    public void testTokenAt_Pos10_Com2Tokens() {
        this.procedureTestTokenAt(10, "asdf@asdf", "@", null);
    }

    @Test
    public void testTokenAt_PosMinus1_Com2Tokens() {
        this.procedureTestTokenAt(-1, "asdf@asdf", "@", null);
    }

    @Test
    public void testTokenAt_PosZero_StringVazia() {
        this.procedureTestTokenAt(0, "", "w", null);
    }

    @Test
    public void testTokenAt_PosMinus1_StringVazia() {
        this.procedureTestTokenAt(-1, "", "w", null);
    }

    //teste para The.explodedToArray
    @Test(expected = NullPointerException.class)
    public void testeExplodeToArray_Null_Null() {
        The.explodedToArray(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testeExplodeToArray_StringVazia_Null() {
        The.explodedToArray("", null);
    }

    @Test(expected = NullPointerException.class)
    public void testeExplodeToArray_Null_SeparadorVazio() {
        The.explodedToArray(null, "");
    }

    @Test
    public void testeExplodeToArray_StringVazia_SeparadorVazio() {
        String[] tokens = The.explodedToArray("", "");
        Assert.assertNotNull("O array não deveria ser nao nulo.", tokens);
        Assert.assertEquals("O array deveria ter tamanho 0.", 0, tokens.length);
    }

    @Test
    public void testeExplodeToArray_StringComTokens_SeparadorExistente() {
        String[] tokens = The.explodedToArray("adadadadada@DADA@7867", "@");
        Assert.assertNotNull("O array não deveria ser nao nulo.", tokens);
        Assert.assertEquals("O array deveria ter tamanho 3.", 3, tokens.length);
    }

    @Test
    public void testeExplodeToArray_SeparadorNaoPresente() {
        String[] tokens = The.explodedToArray("QWER1123TYUIO", "@");
        Assert.assertNotNull("O array não deveria ser nao nulo.", tokens);
        Assert.assertEquals("O array deveria ter tamanho 1.", 1, tokens.length);
    }

    @Test
    public void testeExplodeToArray_StringComMuitosTokens_SeparadorExistente() {
        String[] tokens = The.explodedToArray("a@a@a@a@a@a@a@a@a@a@a@a@a@a", "@");
        Assert.assertNotNull("O array não deveria ser nao nulo.", tokens);
        Assert.assertEquals("O array deveria ter tamanho 14.", 14, tokens.length);
    }
	
}
