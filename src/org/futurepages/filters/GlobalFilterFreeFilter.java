package org.futurepages.filters;

import org.futurepages.core.filter.Filter;
import org.futurepages.core.control.InvocationChain;

/*
 * Marca quais innerActions devem ser livres dos filtros globais.
 *
 * Se nenuma innerAction é especificada, então toda a action é livre dos filtros globais.
 */
public class GlobalFilterFreeFilter implements Filter {
   
   private final String[] innerActions;
   
   public GlobalFilterFreeFilter() {
      
      this.innerActions = null;
      
   }
   
   public GlobalFilterFreeFilter(String ... innerActions) {
      
      this.innerActions = innerActions;
   }
   
   public boolean isGlobalFilterFree(String innerAction) {
      
      if (innerActions == null) return true;
      
      if (innerAction == null) return false; // inner actions are specified...
      
      for(String s : innerActions) {
         
         if (s.equals(innerAction)) return true;
      }
      
      return false;
      
   }
   
   public String filter(InvocationChain chain) throws Exception {
      
      return chain.invoke();
   }
   
   public void destroy() { 
      
   }
   
}