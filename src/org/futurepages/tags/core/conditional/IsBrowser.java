package org.futurepages.tags.core.conditional;

import javax.servlet.jsp.JspException;
import org.futurepages.core.tags.ConditionalTag;

/**
 *
 * NÃO ESTÁ SENDO CHAMADO NO ARQUIVO TLD
 *
 * @author Sergio Oliveira
 */
public class IsBrowser extends ConditionalTag {
    
    private String value;
    
    public void setValue(String value) {
        
        this.value = value;
        
    }
    
    public boolean testCondition() throws JspException {
       
       String userAgent = req.getHeader("user-agent");
       
       if (userAgent == null || userAgent.equals("")) return false;
       
       if (value.equalsIgnoreCase("Firefox")) {
          
          if (userAgent.indexOf("Firefox") > 0) return true;
          
       } else if (value.equalsIgnoreCase("IE6")) {
          
          if (userAgent.indexOf("MSIE 6") > 0) return true;
          
       } else if (value.equalsIgnoreCase("IE7")) {
          
          if (userAgent.indexOf("MSIE 7") > 0) return true;
          
       } else if (value.equalsIgnoreCase("Safari")) {
          
          if (userAgent.indexOf("Safari") > 0) return true;
          
       } else if (value.equalsIgnoreCase("Opera")) {
          
          if (userAgent.indexOf("Opera") > 0) return true;
          
       } else {
          
          throw new JspException("Invalid argument for isBrowser tag!");
          
       }
       
       return false;
    }
}