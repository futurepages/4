package org.futurepages.menta.json;

import org.futurepages.menta.consequences.AjaxConsequence;
import org.futurepages.menta.core.ajax.AjaxRenderer;
import org.futurepages.menta.exceptions.ConsequenceException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.Map;

/**
 * Expect a java.util.Map or a <a href="http://www.json.org/java/">org.json.JSONObject</a>
 * in the output.
 * 
 * @author Davi Luan Carneiro
 */
public class JSONMapAjaxRenderer implements AjaxRenderer {

	//public static final String JSON_MAP_ATTR = "json_map";
	
	public JSONMapAjaxRenderer() {
		
	}
	
	public String encode(Object obj, Locale loc, boolean pretty) throws Exception {
		
		JSONObject json;
		
		if (obj instanceof Map) {
			
			json = new JSONObject((Map)obj);
			
		} else if (obj instanceof JSONObject) {
			
			json = (JSONObject)obj;
			
		} else {
			
			throw new ConsequenceException("Value is not a Collection or JSONObject: " + obj);
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
