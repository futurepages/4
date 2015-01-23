package org.futurepages.filters;

import org.futurepages.core.filter.Filter;
import org.futurepages.core.control.InvocationChain;

/**
 * Marca quais innerActions devem ser redirecionadas após o login.
 *
 * Se nenhuma innerAction for definida, toda a action é redirecionada.
 *
 * Deve ser usado numa action especifica.
 * 
 * @author leandro
 */
public class ShouldRedirectFilter implements Filter {
   
   private final String[] innerActions;
   
   public ShouldRedirectFilter() {
      
      this.innerActions = null;
      
   }
   
   public ShouldRedirectFilter(String ... innerActions) {
      
      this.innerActions = innerActions;
   }
   
   public boolean shouldRedirect(String innerAction) {
      
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