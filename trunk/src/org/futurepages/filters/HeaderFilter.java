package org.futurepages.filters;

import org.futurepages.core.action.Action;
import org.futurepages.core.filter.Filter;
import org.futurepages.core.input.Input;
import org.futurepages.core.control.InvocationChain;

/**
 * This filter will place a http header value in the action input.
 * 
 * The header value will come from the action input method getHeader.
 * 
 * @author Sergio Oliveira Jr.
 */
public class HeaderFilter implements Filter {
   
   private final String name;
   
   private final String header;
   
   public HeaderFilter(String name) {
      
      this(name, name);
   }
   
   public HeaderFilter(String name, String header) {
      
      this.name = name;
      
      this.header = header;
   }
   
   public String filter(InvocationChain chain) throws Exception {
      
      Action action = chain.getAction();
      
      Input input = action.getInput();
      
      String headerValue = input.getHeader(header);
         
      input.setValue(name, headerValue);
      
      return chain.invoke();
         
   }
   
   public void destroy() { 
      
   }
   
}