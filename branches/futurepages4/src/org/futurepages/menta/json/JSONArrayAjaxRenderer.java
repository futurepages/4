package org.futurepages.menta.json;

import net.sf.json.JSONArray;
import org.futurepages.menta.consequences.AjaxConsequence;
import org.futurepages.menta.core.ajax.AjaxRenderer;

import java.util.Collection;
import java.util.Locale;

/**
 * Expect a java.util.Collection or a JSONArray in the output.
 * 
 * @author Davi Luan Carneiro
 * @author Rubem Azenha (rubem.azenha@gmail.com)
 */
public class JSONArrayAjaxRenderer implements AjaxRenderer {

    //public static final String JSON_ARRAY_ATTR = "json_array";

    public JSONArrayAjaxRenderer() {
    	
    }

    public String encode(Object obj, Locale loc, boolean pretty) throws Exception {
    	
        JSONArray json;
        
        if (obj instanceof Collection) {
        	
            json = new JSONArray((Collection) obj);
            
        } else if (obj instanceof JSONArray) {
        	
            json = (JSONArray) obj;
            
        } else {
        	
            throw new IllegalArgumentException("Value is not a Collection or JSONArray: " + obj);
        }
        
        if (pretty) {
        	
        	return json.toString(3);
        	
        } else {

        	return json.toString();
        	
        }
    }

    public String getContentType() {
       return TEXT_HTML;
    }
    
    public String getCharset() {
       return AjaxConsequence.DEFAULT_CHARSET;
    }
}
