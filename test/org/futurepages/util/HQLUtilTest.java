package org.futurepages.util;

import org.junit.Assert;
import org.junit.Test;

public class HQLUtilTest {

    private void fieldHasWords_testProcedure(String field, String[] words, String logicalConect, String expResult, String message){
        String result = HQLUtil.fieldHasWords(field, words, logicalConect);
        Assert.assertEquals(message ,expResult, result);

    }
    private void fieldHasAllWords_testProcedure(String message,String field, String[] words, String expResult){
    	String result = HQLUtil.fieldHasAllWordsInSameSequence(field, words);
    	Assert.assertEquals(message ,expResult, result);
    	
    }
    @Test
    public void testFieldAllHasWords_wordsSeveralElements() {
        String field = "field";
        String[] words = new String[]{"word1","word2","word3"};
        String expResult = "field LIKE '%word1%word2%word3%'";
        String message = "Erro quando array de tokens é não null.";
        fieldHasAllWords_testProcedure(message,field, words,expResult);
    }

    @Test
    public void testFieldHasAllWords_wordsEmpty() {
    	String field = "field";
    	String[] words = new String[0];
    	String expResult = "";
    	String message = "Erro quando array de tokens está vazio.";
    	fieldHasAllWords_testProcedure(message,field, words,expResult);
    }

    @Test
    public void testFieldHasAllWords_wordsNull() {
    	String[] words = null;
    	String expResult = "";
    	String message = "Erro quando array de tokens é null.";
    	fieldHasAllWords_testProcedure(message,"field", words,expResult);
    }
    
    
    @Test
    public void testFieldHasAllWords_wordsOneElement() {
    	String field = "field";
    	String[] words = new String[]{"word"};
    	String expResult = "field LIKE '%word%'";
    	String message = "Erro quando array de tokens possui apenas 1 elemento.";
    	fieldHasAllWords_testProcedure(message,field, words,expResult);
    }
    
    
    @Test
    public void testFieldHasWords_wordsEmpty() {
        String field = "field";
        String[] words = new String[0];
        String logicalConect = "AND";
        String expResult = "";
        fieldHasWords_testProcedure(field, words,logicalConect,expResult , "Erro quando não há elementos.");
    }

    @Test
    public void testFieldHasWords_wordsOneElement() {
        String field = "field";
        String[] words = new String[]{"a"};
        String logicalConect = "AND";
        String expResult = "field LIKE '%a%'";
        fieldHasWords_testProcedure(field, words,logicalConect,expResult , "Erro quando há um elemento.");
    }
    
    @Test
    public void testFieldHasWords_wordsSeveralElements() {
        String field = "field";
        String[] words = new String[]{"a","b","c"};
        String logicalConect = "AND";
        String expResult = "field LIKE '%a%' AND field LIKE '%b%' AND field LIKE '%c%'";
        fieldHasWords_testProcedure(field, words,logicalConect,expResult , "Erro quando há vários elementos.");
    }
    
    @Test 
    public void testEsc_AspasSimples(){
    	esc_TestProcedure( "QWER'TYUIO", "QWER''TYUIO");
    }

    @Test 
    public void testEsc_DoubleAspasSimples(){
    	esc_TestProcedure( "QWER''TYUIO", "QWER''''TYUIO");
    }

    @Test 
    public void testEsc_VariasAspasSimples(){
    	esc_TestProcedure( "QW'ER'TYUIO", "QW''ER''TYUIO");
    }
    
    @Test
    public void testEsc_Percentage(){
    	esc_TestProcedure( "QWER%TYUIO", "QWER\\%TYUIO");
    }
    
    @Test
    public void testEsc_DoublePercentage(){
    	esc_TestProcedure( "QWER%%TYUIO", "QWER\\%\\%TYUIO");
    }

    @Test
    public void testEsc_SeveralPercentage(){
    	esc_TestProcedure( "QWER%TYU%IO", "QWER\\%TYU\\%IO");
    }
    
    private void esc_TestProcedure(String initial, String expected){
    	String atual = HQLUtil.escLike(initial);
    	Assert.assertEquals("Erro no escape de :", expected, atual);
    }
    
    @Test
    public void testMatches_buscaVazia(){
    	final String busca = "";
    	final String esperado = "";
    	final String errorMsg = "msg";
    	matchesTestProcedure(errorMsg, "campo" , busca,esperado);
    }

    @Test
    public void testMatches_umTokenPequeno(){
    	final String busca = "aa";
    	final String esperado = "";
    	final String errorMsg = "msg";
    	matchesTestProcedure(errorMsg, "campo" , busca,esperado);
    }

    @Test
    public void testMatches_variosTokensPequenos(){
    	final String busca = " a b ab cd ";
    	final String esperado = "";
    	final String errorMsg = "msg";
    	matchesTestProcedure(errorMsg, "campo", busca,esperado);
    }
    
    @Test
    public void testMatches_minus_tokenPequeno(){
    	final String busca = "-a";
    	final String esperado = "";
    	final String errorMsg = "msg";
    	matchesTestProcedure(errorMsg, "campo", busca,esperado);
    }
    
    @Test
    public void testMatches_minus_separadoDoToken(){
    	final String busca = "- asdf";
    	final String esperado = "campo LIKE '%asdf%'";
    	final String errorMsg = "msg";
    	matchesTestProcedure(errorMsg, "campo", busca,esperado);
    }

    @Test
    public void testMatches_minusInicio_UnicoToken(){
    	final String busca = "-asdf";
    	final String esperado = "campo NOT LIKE '%asdf%'";
    	final String errorMsg = "msg";
    	matchesTestProcedure(errorMsg, "campo", busca,esperado);
    }
    
    @Test
    public void testMatches_minusInicio(){
    	final String busca = "-qwer asdf";
    	final String esperado = "campo LIKE '%asdf%' AND campo NOT LIKE '%qwer%'";
    	final String errorMsg = "msg";
    	matchesTestProcedure(errorMsg, "campo", busca,esperado);
    }

    @Test
    public void testMatches_minusSegundoToken(){
    	final String busca = "qwer -asdf";
    	final String esperado = "campo LIKE '%qwer%' AND campo NOT LIKE '%asdf%'";
    	final String errorMsg = "msg";
    	matchesTestProcedure(errorMsg, "campo", busca,esperado);
    }

    @Test
    public void testMatches_minus_Varios(){
    	final String busca = "-qwer -asdf ";
    	final String esperado = "campo NOT LIKE '%qwer%' AND campo NOT LIKE '%asdf%'";
    	final String errorMsg = "msg";
    	matchesTestProcedure(errorMsg, "campo", busca,esperado);
    }
    
    @Test
    public void testMatches_aspas_tokenPequeno(){
    	final String busca = "\"a\"";
    	final String esperado = "";
    	final String errorMsg = "msg";
    	matchesTestProcedure(errorMsg, "campo", busca,esperado);
    }
    
    @Test
    public void testMatches_aspas_somenteUmGrupo(){
    	final String busca = "\"asdf qwer\"";
    	final String esperado = "campo LIKE '%asdf qwer%'";
    	final String errorMsg = "msg";
    	matchesTestProcedure(errorMsg, "campo", busca,esperado);
    }

    @Test
    public void testMatches_aspas_doisGrupos(){
    	final String busca = " \"primeiro grupo\" \"segundo grupo\"  ";
    	final String esperado = "campo LIKE '%primeiro grupo%' AND campo LIKE '%segundo grupo%'";
    	final String errorMsg = "msg";
    	matchesTestProcedure(errorMsg, "campo", busca,esperado);
    }

    @Test
    public void testMatches_aspas_umGrupo_outrosTokens(){
    	final String busca = " asdf  \"primeiro grupo\" qwer ";
    	final String esperado = "campo LIKE '%primeiro grupo%' AND campo LIKE '%asdf%' AND campo LIKE '%qwer%'";
    	final String errorMsg = "msg";
    	matchesTestProcedure(errorMsg, "campo", busca,esperado);
    }

    @Test
    public void testMatches_aspas_grupoRepetido(){
    	final String busca = " \"grupo abc\" \"grupo abc\" ";
    	final String esperado = "campo LIKE '%grupo abc%'";
    	final String errorMsg = "msg";
    	matchesTestProcedure(errorMsg, "campo", busca,esperado);
    }
    
    @Test
    public void testMatches_aspas_umGrupo_minusToken(){
    	final String busca = " asdf  \"primeiro grupo\" -qwer ";
    	final String esperado = "campo LIKE '%primeiro grupo%' AND campo LIKE '%asdf%' AND campo NOT LIKE '%qwer%'";
    	final String errorMsg = "msg";
    	matchesTestProcedure(errorMsg, "campo", busca,esperado);
    }

    @Test
    public void testMatches_minusEmGrupoAspas(){
    	final String busca = "maria -\"primeiro grupo\"";
    	final String esperado = "campo LIKE '%maria%' AND campo NOT LIKE '%primeiro grupo%'";
    	final String errorMsg = "msg";
    	matchesTestProcedure(errorMsg, "campo", busca,esperado);
    }

    @Test
    public void testMatches_minusAspasTokenUmChar(){
    	final String busca = "-\"a\"";
		final String esperado = "";
		final String errorMsg = "msg";
		matchesTestProcedure(errorMsg, "campo", busca,esperado);
    }

	@Test
    public void testMatches_variosTokens(){
    	final String busca = "ABCD EFGH NOPQRSTUVXZ";
    	final String esperado = "campo LIKE '%ABCD%' AND campo LIKE '%EFGH%' AND campo LIKE '%NOPQRSTUVXZ%'";
    	matchesTestProcedure("", "campo", busca,esperado);
    }

	@Test
    public void testMatches_asteriscoSimples(){
    	final String busca = "\"maria * santana\"";
    	final String esperado = "campo LIKE '%maria%santana%'";
    	matchesTestProcedure("", "campo", busca,esperado);
    }

	@Test
    public void testMatches_asteriscoSemEspacos(){
    	final String busca = "\"maria*santana\"";
    	final String esperado = "campo LIKE '%maria%santana%'";
    	matchesTestProcedure("", "campo", busca,esperado);
    }

	@Test
    public void testMatches_asteriscoForaDasAspas(){
    	final String busca = "\"maria*santana\" **************";
		
    	final String esperado = "campo LIKE '%maria%santana%'";

    	matchesTestProcedure("", "campo", busca,esperado);
    }

	@Test
    public void testMatches_asteriscosMultiplos(){
    	final String busca = "********* * * * \"maria********santana\" **************";

    	final String esperado = "campo LIKE '%maria%santana%'";

    	matchesTestProcedure("", "campo", busca,esperado);
    }
    
	private void matchesTestProcedure(String errorMsg, String field, String busca, String esperado) {
		String result = HQLUtil.matches(field, busca);
		Assert.assertEquals(errorMsg, esperado, result);
	}
}
