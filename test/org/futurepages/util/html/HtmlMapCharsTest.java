package org.futurepages.util.html;

import org.futurepages.util.html.HtmlMapChars;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 *
 * @author leandro
 */
public class HtmlMapCharsTest {

    public HtmlMapCharsTest() {
    }

    @Test
    public void testGetSimple() {
        char ch = '\"';
        String expResult = "&quot;";
        String result = HtmlMapChars.getSimple(ch);
        assertEquals("Não retornou o valor correto da tabela de caracteres para aspa dupla.",expResult, result);
    }

    @Test
    public void testSimpleHtmlValue() {
        String strIn = "<\"teste\">";
        String expResult = "&lt;&quot;teste&quot;&gt;";
        String result = HtmlMapChars.htmlSimpleValue(strIn);
        assertEquals("Não retornou o valor esperado em html.",expResult, result);
    }

}