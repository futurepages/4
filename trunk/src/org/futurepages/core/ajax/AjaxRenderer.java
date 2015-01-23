package org.futurepages.core.ajax;

import java.util.Locale;


/**
 * This class will generate a document representation from a Java object.
 * 
 * The document representation is usually XML or JSON.
 * 
 * @author soliveira
 * @author Rubem Azenha
 */
public interface AjaxRenderer {
    
    /**
     * Content-type for text/html.
     */
    public static final String TEXT_HTML = "text/html";
    
    /**
     * Content-type for text/xml.
     */
    public static final String TEXT_XML = "text/xml";
    
	public String encode(Object object, Locale loc, boolean pretty) throws Exception;
    
   public String getContentType();
    
   public String getCharset(); 
    
}
 