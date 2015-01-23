package org.futurepages.json;

import java.util.Locale;

import net.sf.json.JSONObject;

import org.futurepages.consequences.AjaxConsequence;
import org.futurepages.core.ajax.AjaxRenderer;

/**
 * A Ajax Render that gets a Java Object and transforms it into a JSON Object.
 * The render expect an object in the actions output, identified by the key
 * "object" by default, but you can change it.
 * 
 * @author Rubem Azenha (rubem.azenha@gmail.com)
 */
public class JSONObjectAjaxRenderer implements AjaxRenderer {

    //private static final String JSONOBJECT_KEY = "object";

    public JSONObjectAjaxRenderer() {
    	
    }

    public String encode(Object obj, Locale loc, boolean pretty) throws Exception {

    	JSONObject json = JSONObject.fromBean(obj);
    	
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
