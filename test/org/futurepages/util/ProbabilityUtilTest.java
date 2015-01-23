package org.futurepages.util;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author leandro
 */
public class ProbabilityUtilTest {


	private void listCombinations_testProcedure(String[] elements, int groupSize, String expResult){
		List<String[]> combinations = ProbabilityUtil.listCombinations(elements,groupSize);
		StringBuffer sb = new StringBuffer();
		for (String[] combination : combinations) {
			for(String element : combination){
				sb.append(element+" ");
			}
			sb.append("/ ");
		}
		Assert.assertEquals("Combinação não esperada.", expResult, sb.toString());
	}

	@Test
	public void testListCombinations_C_5_3(){
		String[] elements = new String[]{"a","b","c","d","e"};
		int groupSize = 3;
		String expResult = "a b c / a b d / a b e / a c d / a c e / a d e / b c d / b c e / b d e / c d e / ";
		listCombinations_testProcedure(elements, groupSize, expResult);
	}

	@Test
	public void testListCombinations_C_3_1(){
		String[] elements = new String[]{"a","b","c"};
		String expResult = "a / b / c / ";
		listCombinations_testProcedure(elements, 1, expResult);
	}

	@Test
	public void testListCombinations_C_3_2(){
		String[] elements = new String[]{"a","b","c"};
		String expResult = "a b / a c / b c / ";
		listCombinations_testProcedure(elements, 2, expResult);
	}

	@Test
	public void testListCombinations_C_4_4(){
		String[] elements = new String[]{"a","b","c","d"};
		String expResult = "a b c d / ";
		listCombinations_testProcedure(elements, 4, expResult);
	}

	@Test
	public void testListCombinations_C_1_1(){
		String[] elements = new String[]{"a"};
		String expResult = "a / ";
		listCombinations_testProcedure(elements, 1, expResult);
	}

	@Test
	public void testListCombinations_groupSizeMaior_NumeroElementos(){
		String[] elements = new String[]{"a","b"};
		listCombinations_testProcedure(elements, 3, "");
	}

	@Test
	public void testListCombinations_C_0_0(){
		String[] elements = new String[]{};
		listCombinations_testProcedure(elements, 0, "");
	}


	private void numberOfCombinations_testProcedure(String msg, int numberOfElements, int groupSize, int expResult){
		int result = ProbabilityUtil.numberOfCombinations(numberOfElements, groupSize);
		Assert.assertEquals("Erro para:"+msg, expResult, result);
	}

	@Test
	public void testeNumberOfCombinations_C_0_0(){
		numberOfCombinations_testProcedure("S:0,N:0", 0, 0, 1);
	}

	@Test
	public void testeNumberOfCombinations_C_0_1(){
		numberOfCombinations_testProcedure("S:0,N:1", 0, 1, 1);
	}

	@Test
	public void testeNumberOfCombinations_C_1_0(){
		numberOfCombinations_testProcedure("S:1,N:0", 1, 0, 1);
	}

	@Test
	public void testeNumberOfCombinations_C_1_1(){
		numberOfCombinations_testProcedure("S:1,N:1", 1, 1, 1);
	}

	@Test
	public void testeNumberOfCombinations_C_0_10(){
		numberOfCombinations_testProcedure("S:0,N:10", 0, 10, 0);
	}

	@Test
	public void testeNumberOfCombinations_C_10_0(){
		numberOfCombinations_testProcedure("S:10,N:0", 10, 0, 1);
	}
	
	@Test
	public void testeNumberOfCombinations_C_10_1(){
		numberOfCombinations_testProcedure("S:10,N:1", 10, 1, 10);
	}

	@Test
	public void testeNumberOfCombinations_C_1_10(){
		numberOfCombinations_testProcedure("S:1,N:10", 1, 10, 0);
	}

	@Test
	public void testeNumberOfCombinations_C_10_10(){
		numberOfCombinations_testProcedure("S:10,N:10", 10, 10, 1);
	}
	@Test
	public void testeNumberOfCombinations_C_20_10(){
		numberOfCombinations_testProcedure("S:20,N:10", 20, 10, 184756);
	}
	
	@Test
	public void testeNumberOfCombinations_C_10_20(){
		numberOfCombinations_testProcedure("S:20,N:10", 10, 20, 0 );
	}

}
