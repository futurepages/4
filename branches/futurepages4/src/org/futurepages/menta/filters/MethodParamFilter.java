package org.futurepages.menta.filters;

import org.futurepages.menta.core.action.Action;
import org.futurepages.menta.core.control.InvocationChain;
import org.futurepages.menta.core.filter.Filter;
import org.futurepages.menta.core.input.Input;

import java.util.ArrayList;
import java.util.List;

/**
 * This filter will place a java.util.List with the action input keys we
 * want to use as the parameters of a Pojo Action method.
 * 
 * It simply takes the list of params passed, create a list with them, and place this
 * list in the action input with the PARAM_KEY key value, so that it can be later
 * accessed by the InvocationChain.
 * 
 * @author Sergio Oliveira Jr
 */
public class MethodParamFilter implements Filter {
	
	public static String PARAM_KEY = "_params";
		
    
    private final List<String> list;
    
	public MethodParamFilter(String... params) {
		
		super();
		
		list = new ArrayList<String>(params.length);
		
		for(String p: params) {
			
			list.add(p);
		}
	}
    
	public String filter(InvocationChain chain) throws Exception {
		
		Action a = chain.getAction();
		
		Input input = a.getInput();
		
		input.setValue(PARAM_KEY, list);
		
		return chain.invoke();
        
	}
	
	public void destroy() { 
    
    }
	
	@Override
    public String toString() {
        
        StringBuffer sb = new StringBuffer(128);
    
        sb.append("MethodParamFilter: ");
        
        for(int i=0;i<list.size();i++) {
        	
        	if (i > 0) sb.append(", ");
        	
        	sb.append(list.get(i));
        }
            
        return sb.toString();
        
    }
	
}