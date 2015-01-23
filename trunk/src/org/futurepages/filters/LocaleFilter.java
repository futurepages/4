package org.futurepages.filters;

import java.util.Locale;

import org.futurepages.core.action.Action;
import org.futurepages.core.filter.Filter;
import org.futurepages.core.input.Input;
import org.futurepages.core.control.InvocationChain;

/**
 * This filter will place the action locale in the action input.
 * 
 * @author Sergio Oliveira Jr.
 */
public class LocaleFilter implements Filter {
   
   private final String name;
   
   public LocaleFilter(String name) {
      
      this.name = name;
   }
   
   public String filter(InvocationChain chain) throws Exception {
      
      Action action = chain.getAction();
      
      Input input = action.getInput();
      
      Locale loc = action.getLocale();
      
      if (loc != null) {
         
         input.setValue(name, loc);
         
      }
      
      return chain.invoke();
         
   }
   
   public void destroy() { 
      
   }
   
}