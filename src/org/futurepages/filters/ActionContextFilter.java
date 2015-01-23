package org.futurepages.filters;

import java.util.HashMap;
import java.util.Map;

import org.futurepages.core.action.Action;
import org.futurepages.core.filter.Filter;
import org.futurepages.core.input.InputWrapper;
import org.futurepages.core.control.InvocationChain;

public class ActionContextFilter extends InputWrapper implements Filter {
   
   private final String name;
   
   public ActionContextFilter(String name) {
      
      this.name = name;
      
   }
   
   public String filter(InvocationChain chain) throws Exception {
	   
	   Action action = chain.getAction();
	
	   super.setInput(action.getInput());
	
	   action.setInput(this);
	   
	   return chain.invoke();
   }
   
	@Override
	public Object getValue(String name) {
		
		if (name.equals(this.name)) {
			
			Object value = super.getValue(name);
			
			if (value != null) return value;
			
			Map<String, Object> pojoContext = new HashMap<String, Object>();
			
			pojoContext.put("application", super.getValue("application"));
			
			pojoContext.put("session", super.getValue("session"));
			
			pojoContext.put("output", super.getValue("output"));
			
			pojoContext.put("input", this);
			
			pojoContext.put("isPost", super.getValue("isPost"));
			
			pojoContext.put("cookies", super.getValue("cookies"));
			
			pojoContext.put("locale", super.getValue("locale"));
			
			pojoContext.put("headers", super.getValue("headers"));
			
			pojoContext.put("messages", super.getValue("messages"));
			
			pojoContext.put("errors", super.getValue("errors"));
			
			pojoContext.put("fieldErrors", super.getValue("fieldErrors"));
			
			setValue(name, pojoContext);
			
			return pojoContext;
			
		} else {
			
			return super.getValue(name);
		}
	}
   
   public void destroy() { 
      
   }
   
}