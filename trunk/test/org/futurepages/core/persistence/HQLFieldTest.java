package org.futurepages.core.persistence;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class HQLFieldTest {

	private final String fieldName = "_field_Name_";
	private HQLField field;

	@Before
	public void setUp() {
		field = new HQLField(fieldName);
	}
	
	private void in_STRING_TestProcedure(String msg, String expected,String... tokens) {
		String atual = field.in(tokens);
		Assert.assertEquals(msg, expected, atual);
	}

	@Test
	public void testIN_STRING_Elements(){
		final String msg = "Erro quando não é passado nenhum elemento";
		in_STRING_TestProcedure(msg,"");
	}

	@Test
	public void testIN_STRING_emptyOneToken(){
		final String msg = "Erro quando não é apenas um elemento";
		String esperado = fieldName + " IN ('AAA')";
		in_STRING_TestProcedure(msg,esperado,"AAA");
	}

	@Test
	public void testIN_STRING_TwoTokens(){
		final String msg = "Erro quando não são passado dois elementos";
		String esperado = fieldName + " IN ('AAA','BBB')";
		in_STRING_TestProcedure(msg,esperado ,"AAA","BBB");
	}
	
	private void notIn_STRING_TestProcedure(String msg, String expected,String... tokens) {
		String atual = field.notIn(tokens);
		Assert.assertEquals(msg, expected, atual);
	}
	
	@Test
	public void testNotIn_STRING_Elements(){
		final String msg = "Erro quando não é passado nenhum elemento";
		notIn_STRING_TestProcedure(msg,"");
	}
	
	@Test
	public void testNotIn_STRING_emptyOneToken(){
		final String msg = "Erro quando não é passado nenhum elemento";
		String esperado = fieldName + " NOT IN ('AAA')";
		notIn_STRING_TestProcedure(msg,esperado,"AAA");
	}
	
	@Test
	public void testNotIn_STRING_TwoTokens(){
		final String msg = "Erro quando não é passado nenhum elemento";
		String esperado = fieldName + " NOT IN ('AAA','BBB')";
		notIn_STRING_TestProcedure(msg,esperado ,"AAA","BBB");
	}

	private void in_LONG_TestProcedure(String msg, String expected,long... tokens) {
		String atual = field.in(tokens);
		Assert.assertEquals(msg, expected, atual);
	}
	
	@Test
	public void testIn_LONG_emptyLongs(){
		final String msg = "Erro quando não é passado nenhum elemento";
		in_LONG_TestProcedure(msg, "");
	}
	
	@Test
	public void testIn_LONG_emptyOneLong(){
		final String msg = "Erro quando não é passado nenhum elemento";
		String esperado = fieldName + " IN (3)";
		in_LONG_TestProcedure(msg, esperado, 3L);
	}
	
	@Test
	public void testIn_LONG_emptyTwoLongs(){
		final String msg = "Erro quando não é passado nenhum elemento long";
		String esperado = fieldName + " IN (12,49)";
		in_LONG_TestProcedure(msg,esperado , 12L, 49L);
	}

	private void notIn_LONG_TestProcedure(String msg, String expected,long... tokens) {
		String atual = field.notIn(tokens);
		Assert.assertEquals(msg, expected, atual);
	}
	
	@Test
	public void testNotIn_LONG_emptyLongs(){
		final String msg = "Erro quando não é passado nenhum elemento";
		notIn_LONG_TestProcedure(msg, "");
	}
	
	@Test
	public void testNotIn_LONG_emptyOneLong(){
		final String msg = "Erro quando não é passado nenhum elemento";
		String esperado = fieldName + " NOT IN (3)";
		notIn_LONG_TestProcedure(msg, esperado, 3L);
	}
	
	@Test
	public void testNotIn_LONG_emptyTwoLongs(){
		final String msg = "Erro quando não é passado nenhum elemento long";
		String esperado = fieldName + " NOT IN (12,49)";
		notIn_LONG_TestProcedure(msg,esperado , 12L, 49L);
	}
	
	@Test
	public void testEqualsTo_(){
		final String msg = "Erro quando são passadas duas stringas iguais";
		String esperado = fieldName + " = 'ASDF'";
		equalsTo_String_TestProcedure(msg,esperado , "ASDF");
	}
	
	private void equalsTo_String_TestProcedure(String msg, String esperado, String value) {
		String result = field.equalsTo(value);
		Assert.assertEquals(msg, esperado, result);
	}

	private void betweenTestProcedure(String msg, String dataBegin, String dataEnd, String esperado){
		String result = field.between(dataBegin, dataEnd);
		Assert.assertEquals(msg, esperado, result);
	}
	
	@Test
	public void testBetween_notNull(){
		field = new HQLField("DATA");
		betweenTestProcedure("", "AAA", "BBB", "((DATA >= 'AAA')  AND  (DATA <= 'BBB'))");
	}	

	@Test
	@Ignore
	public void testBetween_firstParameterNull(){
		field = new HQLField("DATA");
		betweenTestProcedure("", "", "BBB", "(DATA <= 'BBB'))");
	}	
	@Test
	@Ignore
	public void testBetween_secondParameterNull(){
		field = new HQLField("DATA");
		betweenTestProcedure("", "AAA", "", "(DATA >= 'BBB'))");
	}	
	
}
