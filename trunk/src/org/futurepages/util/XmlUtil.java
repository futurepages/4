package org.futurepages.util;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.xml.transform.TransformerException;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import com.sun.org.apache.xpath.internal.XPathAPI;
import org.futurepages.core.exception.DefaultExceptionLogger;
/**
 * Classe útil para manipulação XML
 * @author Danilo
 *
 */
public class XmlUtil {

	public static Element getRootElement(String xmlResource) throws JDOMException, IOException {
		File f = new File(xmlResource);
		SAXBuilder sb = new SAXBuilder();
		Document d = sb.build(f);
		Element rootElement = d.getRootElement();
		return rootElement;
	}
	/**
	 * @param xPathQuery - A valid XPath string. 
	 */

	public static NodeList selectNodeList(String node,String xPathQuery) {
		return selectNodeList(parseXMLFromString(node), xPathQuery);
	}

	public static NodeList selectNodeList(Node node,String xPathQuery) {
		NodeList result = null;
		try {
			result = XPathAPI.selectNodeList(node, xPathQuery);
		} catch (TransformerException e) {
			DefaultExceptionLogger.getInstance().execute(e);
		}
		return result;
	}

	public static NodeList getElementsByNameAtribute(String nodeContent, String name) {
		org.w3c.dom.Document doc = XmlUtil.parseXMLFromString(nodeContent);
		return getElementsByNameAttribute(doc,name);
	}

	public static NodeList getElementsByNameAttribute(Node node,String name) {
		String query = "//*[@name="+name+"]";
		return XmlUtil.selectNodeList(node,query);
	}

	public static org.w3c.dom.Document parseXMLFromString(String source) {
		DOMParser parser = new DOMParser();

		try {
			parser.parse(new InputSource(new StringReader(source)));			
		} catch (SAXException e) {
			DefaultExceptionLogger.getInstance().execute(e);
		} catch (IOException e) {
			DefaultExceptionLogger.getInstance().execute(e);
		}

		return parser.getDocument();
	}

	public static Element searchElementByName(String identifier,Document doc){
		Element root = doc.getRootElement();
		return getElement(identifier,root);
	}
	
	/**
	 * 
	 * @param identifier 'id' or 'name' attribute from a html tag
	 * @param element
	 * @return
	 */
	private static Element getElement(String identifier, Element element) {
		List<Element> children = element.getChildren();
		for (Element child : children) {
			Element found = getElement(identifier, child);
			if(found != null){
				return found;
			}
		}
		Attribute attribute = element.getAttribute("name");
		if(attribute != null){
			if(attribute.getValue().equals(identifier)){
				return element;
			}
		}

		attribute = element.getAttribute("id");
		if(attribute != null){
			if(attribute.getValue().equals(identifier)){
				return element;
			}
		}
		return null;
	}

}
