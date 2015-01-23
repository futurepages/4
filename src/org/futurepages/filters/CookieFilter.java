package org.futurepages.filters;

import org.futurepages.core.action.Action;
import org.futurepages.core.context.Context;
import org.futurepages.core.filter.Filter;
import org.futurepages.core.input.Input;
import org.futurepages.core.control.InvocationChain;

/**
 * This filter will place a http cookie value in the action input.
 * 
 * @author Sergio Oliveira Jr.
 */
public class CookieFilter implements Filter {
   
   private final String name;
   
   private final String cookieName;
   
   public CookieFilter(String name) {
      
      this(name, name);
   }
   
   public CookieFilter(String name, String cookieName) {
      
      this.name = name;
      
      this.cookieName = cookieName;
   }
   
   public String filter(InvocationChain chain) throws Exception {
      
      Action action = chain.getAction();
      
      Input input = action.getInput();
      
      Context cookies = action.getCookies();
      
      if (cookies != null && cookies.hasAttribute(cookieName)) {

         input.setValue(name, cookies.getAttribute(cookieName));
         
      }
      
      return chain.invoke();
         
   }
   
   public void destroy() { 
      
   }
   
}