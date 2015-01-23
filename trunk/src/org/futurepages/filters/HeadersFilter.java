package org.futurepages.filters;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.futurepages.core.action.Action;
import org.futurepages.core.filter.Filter;
import org.futurepages.core.input.Input;
import org.futurepages.core.input.InputWrapper;
import org.futurepages.core.control.InvocationChain;

public class HeadersFilter extends InputWrapper implements Filter {
   
   private final String name;
   
   private ThreadLocal<Action> action = new ThreadLocal<Action>();
   
   public HeadersFilter(String name) {
      
      this.name = name;
      
   }
   
   public String filter(InvocationChain chain) throws Exception {
	   
	   Action action = chain.getAction();
	
	   super.setInput(action.getInput());
	
	   action.setInput(this);
	   
	   this.action.set(action);
	   
	   return chain.invoke();
   }
   
	@Override
	public Object getValue(String name) {
		
		if (name.equals(this.name)) {
			
			Object value = super.getValue(name);
			
			if (value != null) return value;
			
			Action action = this.action.get();
			
			if (action == null) throw new IllegalStateException("Action cannot be null here!");
			
			Input input = action.getInput();
			
			Map<String, String> headers = new HashMap<String, String>();
			
			Iterator<String> iter = input.getHeaderKeys();
			
			while(iter.hasNext()) {
				
				String key = iter.next();
				
				headers.put(key, input.getHeader(key));
			}
			
			setValue(name, headers);
			
			return headers;
			
		} else {
			
			return super.getValue(name);
		}
	}
   
   public void destroy() { 
      
   }
   
}