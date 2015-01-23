package org.futurepages.util.html;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Classe para teste de {@link HtmlRegex#attrPatternWithGroups(String)}
 * @author Danilo Medeiros
 */
@RunWith(Parameterized.class)
public class HtmlRegex_attrPatternWithGroups {

	String caso;
	String html;
	String attName;
	Boolean esperado;

	public HtmlRegex_attrPatternWithGroups(String html, String attName, Boolean esperado, String caso) {
		super();
		this.caso = caso;
		this.html = html;
		this.attName = attName;
		this.esperado = esperado;
	}

	@Parameters
	public static Collection<Object[]> parameters() {
		Collection<Object[]> col =  Arrays.asList(new Object[][] {
				{"a=\"x\"","a", 			true, "deve casar no caso simples, apenas um atributo "},
				{"a='x'","a", 				false, "apenas um atributo com aspas simples"},
				{" a = \"x\"", "a", 		true, "deve casar quando html possui vários espaços"},
				{" a = \"x\"", "a", 		true, "deve casar quando html possui vários espaços"},
				{"asdfg", "a", 			false, "não deve casar quando não houver variável"}
		});
		return col;
	}

	@Test
	public void testeIsTransational() throws SecurityException, NoSuchMethodException, InstantiationException, IllegalAccessException{
		String regex = HtmlRegex.attrPatternWithGroups(this.attName);
		boolean result = html.matches(regex);
		Assert.assertEquals(caso, esperado, result);
	}

}