package org.futurepages.core.persistence;

import org.junit.Assert;
import org.junit.Test;

public class HQLProviderTest {

	private void ands_TestProcedure(String msg, String expected,String... clauses){
		String result = HQLProvider.ands(clauses);
		Assert.assertEquals(msg,expected, result);
	}
	
	@Test
	public void testAnd_Empty(){
		ands_TestProcedure("Erro quando se passa um array vazio", "", new String[0]);
	}
	
	@Test
	public void testAnd_Null(){
		String[] clauses = null;
		ands_TestProcedure("Erro quando se passa um array nulo", "", clauses);
	}

	@Test
	public void testAnd_1Clause(){
		String expected = "(a=1)";
		String[] clauses = {"a=1"};
		ands_TestProcedure("Erro quando se passa um array com uma cláusula", expected, clauses);
	}

	@Test
	public void testAnd_2Clauses(){
		String expected = "(a=1) AND (b=2)";
		String[] clauses = {"a=1","b=2"};
		ands_TestProcedure("Erro quando se passa um array com 2 cláusulas", expected, clauses);
	}

	@Test
	public void testAnd_3Clauses(){
		String expected = "(a=1) AND (b=2) AND (c=3)";
		String[] clauses = {"a=1","b=2","c=3"};
		ands_TestProcedure("Erro quando se passa um array com 3 cláusulas", expected, clauses);
	}
	
	@Test
	public void testAnd_firstClausesEmpty(){
		String expected = "(b=2) AND (c=3)";
		String[] clauses = {"","b=2","c=3"};
		ands_TestProcedure("Erro quando se passa um array com 3 cláusulas", expected, clauses);
	}
	
	@Test
	public void testAnd_lastClauseEmpty(){
		String expected = "(a=1) AND (b=2)";
		String[] clauses = {"a=1","b=2",""};
		ands_TestProcedure("Erro quando se passa um array com 3 cláusulas", expected, clauses);
	}
	
	@Test
	public void testAnd_middleClauseEmpty(){
		String expected = "(a=1) AND (c=3)";
		String[] clauses = {"a=1","","c=3"};
		ands_TestProcedure("Erro quando se passa um array com 3 cláusulas", expected, clauses);
	}
	@Test
	public void testAnd_allClausesEmpty(){
		String expected = "";
		String[] clauses = {"","",""};
		ands_TestProcedure("Erro quando se passa um array com 3 cláusulas", expected, clauses);
	}
}
