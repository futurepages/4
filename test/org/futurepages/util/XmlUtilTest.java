 package org.futurepages.util;


import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.NodeList;

public class XmlUtilTest {

	private String getXML(){
		String st = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" +
				"<root>"+
					"<bookstore tipo =\"e\">" +
						"<book>" +
						"  <title lang=\"eng\">Harry Potter</title>" +
						"  <price>29.99</price>" +
						"</book>" +
						"<book tipo=\"a\">" +
						"  <title lang=\"eng\">Learning XML</title>" +
						"  <price>39.95</price>" +
						"</book>" +
						"<book>" +
						"  <title lang=\"eng\">Learning XML</title>" +
						"  <price>39.95</price>" +
						"</book>" +
					"</bookstore>"+
					"<bookstore>" +
						"<book>" +
						"  <title lang=\"eng\">Harry Potter</title>" +
						"  <price>29.99</price>" +
						"</book>" +
						"<book>" +
						"  <title lang=\"eng\">Learning XML</title>" +
						"  <price>39.95</price>" +
						"</book>" +
					"</bookstore>"+
				"</root>"
				;
		return st;
	}
	
	@Test
	public void test(){
		String xPathQuery;
		//option[@value='3']
//		String xPathQuery = "//bookstore[@tipo = 'e']/book[@tipo = 'a' and title/text() = 'Learning XML']";
		xPathQuery = "//book/title[text() = 'Harry Potter']";
//		String xPathQuery = "//title[='Learning XML']";
		NodeList list  = XmlUtil.selectNodeList(getXML(), xPathQuery);
		Assert.assertNotNull("LISTA NULA!", list);
		Assert.assertFalse("LISTA VAZIA",list.getLength()==0);
		System.out.println("XmlUtilTest.test(): Número de Elementos :"+list.getLength());
		Assert.assertTrue("LISTA COM MAIS DE 1 ELE",list.getLength()==2);
	}
}
