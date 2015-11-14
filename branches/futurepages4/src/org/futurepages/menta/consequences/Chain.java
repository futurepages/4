package org.futurepages.menta.consequences;

import org.futurepages.menta.core.action.Action;
import org.futurepages.menta.core.consequence.Consequence;
import org.futurepages.menta.core.control.ActionConfig;
import org.futurepages.menta.core.control.Controller;
import org.futurepages.menta.core.filter.AfterConsequenceFilter;
import org.futurepages.menta.core.filter.Filter;
import org.futurepages.menta.core.input.Input;
import org.futurepages.menta.core.input.RequestInput;
import org.futurepages.menta.exceptions.ConsequenceException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * An action chaining consequence.
 * 
 * @author Sergio Oliveira
 */
public class Chain implements Consequence {

	private ActionConfig ac;
	
	private String actionName = null;
	private String namedInnerAction = null;
	private String innerAction = null;
	private String actionURI;

	private Map<String, Object> inputMap = new HashMap<String,Object>();


	public Chain(String actionURI) {
		extractUriData(actionURI);
	}

	public Chain(ActionConfig ac) {
		this.ac = ac;
	}

	/**
	 * Creates a chain consequence for the given ActionConfig
	 * @param ac
	 * @param namedInnerAction
	 */
	public Chain(ActionConfig ac, String namedInnerAction) {
		this(ac);
		this.namedInnerAction = namedInnerAction;
	}

	@Override
	public void execute(Action originalAction, HttpServletRequest req, HttpServletResponse res) throws ConsequenceException {
		Action newAction = ac.getAction();

		if (newAction == null) {
			throw new ConsequenceException("Could not load action for chain: '" + ac + "'");
		}

//		Because of the new InputWrapper filters, do not re-use the input but copy its values!
//		@TODO legacy, verify why that.
		Input input = chainedInput(req);

		Input old = originalAction.getInput();
		Iterator<String> iterOld = old.keys();

		while (iterOld.hasNext()) {
			String key = (String) iterOld.next();
			input.setValue(key, old.getValue(key));
		}
		for(String key : inputMap.keySet()){
			input.setValue(key, inputMap.get(key));
		}

		newAction.setInput(input);
		newAction.setOutput(originalAction.getOutput());
		newAction.setSession(originalAction.getSession());
		newAction.setApplication(originalAction.getApplication());
		newAction.setMessages(originalAction.getMessages());
		newAction.setLocale(originalAction.getLocale());
		newAction.setCookies(originalAction.getCookies());
		newAction.setCallback(originalAction.getCallback());

		Consequence c = null;

		List<Object> filters = new LinkedList<Object>();

		boolean conseqExecuted = false;
		boolean actionExecuted = false;
		StringBuilder returnedResult = new StringBuilder(32);

		try {
			String inner;
			if (this.innerAction != null) {
				inner = this.innerAction;
			} else {
				inner = ac.getNamedInnerAction();
			}
			c = Controller.getInstance().invokeAction(ac, newAction, inner, filters, returnedResult);
			actionExecuted = true;
			c.execute(newAction, req, res);
			conseqExecuted = true;
		} catch (ConsequenceException e) {
			throw e;
		} catch (Exception e) {
			throw new ConsequenceException(e);
		} finally {
			Iterator<Object> iter = filters.iterator();
			while (iter.hasNext()) {
				Filter f = (Filter) iter.next();
				if (f instanceof AfterConsequenceFilter) {
					AfterConsequenceFilter acf = (AfterConsequenceFilter) f;
					try {
						String s = returnedResult.toString();
						acf.afterConsequence(newAction, c, conseqExecuted, actionExecuted, s.length() > 0 ? s : null);
					} catch (Exception e) {
						throw new ConsequenceException(e);
					}
				}
			}
		}
	}

	public String getActionName() {
		return actionName;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(128);
		sb.append("Chain to ").append(ac);
		if (namedInnerAction != null) {
			sb.append(" (innerAction = ").append(namedInnerAction).append(")");
		}
		return sb.toString();
	}

	public void setAc(ActionConfig ac) {
		this.ac = ac;
	}

/**
 * Este método deve ser chamado só na instanciação. Os valores ficarão guardados pra
 * sempre que a action for chamada.
 */

	private void extractUriData(String actionURI) {
		actionURI = (actionURI.charAt(0)=='/')? actionURI.substring(1) : actionURI;

		if(actionURI.contains("/?")){
			actionURI = actionURI.replaceAll("/\\?", "?");
		}

		String[] parts   = actionURI.split("\\?");
		String sepRegex = "\\"+Controller.getInstance().getInnerActionSeparator();
		
	    String[] partsAction = parts[0].split(sepRegex);

		this.actionName = partsAction[0];

		if(partsAction.length > 1){
			this.innerAction = partsAction[1].split("/")[0];
		}

		if(parts.length>1){
			extractInputFromQuery(parts[1]);
		}
		this.actionURI = parts[0]; //necessário para o extractInputParamsFromURI
	}

	private void extractInputFromQuery(String queryParams) {
		String[] params = queryParams.split("&");
		for(String paramBruto : params){
			String[] param = paramBruto.split("=");
			inputMap.put(param[0], (param.length>1 ? param[1] : null));
		}
	}

	/**
	 * deve ser chamado somente quando vai ser feito o registro dos chains, após todos os módulos
	 * terem sido registrados, pois os módulos precisam ser carregados para verificar se a action é
	 * global.
	 */
	public void extractInputParamsFromURI() {
		String[] uriPathParts = actionURI.split("/");
		boolean global =  !(Controller.getInstance().isModule(uriPathParts[0]));
		int i = (global? 1 : 2);

		int keyIdx = 0;
		if(uriPathParts.length > i ){
			for(int k = i ; k < uriPathParts.length ; k++){
				inputMap.put(String.valueOf(keyIdx), uriPathParts[k]);
				keyIdx++;
			}
			if(global){
				this.actionName = uriPathParts[0];
			}
		}
	}
	
	private Input chainedInput(HttpServletRequest req) {
		//o input de acordo com a situação
		return new RequestInput(req);
	}

	public String getNamedInnerAction() {
		return this.namedInnerAction;
	}
}