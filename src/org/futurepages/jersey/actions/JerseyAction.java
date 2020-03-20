package org.futurepages.jersey.actions;

import org.futurepages.menta.actions.NullAction;
import org.futurepages.menta.core.action.Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

public class JerseyAction {

	@Context
	HttpServletRequest req;

	@Context
	HttpServletResponse res;

	protected Action action;

	private static ThreadLocal<JerseyAction> arTL = new ThreadLocal<>();

	@Context
	public void setAction(HttpServletRequest req, HttpServletResponse res) {
		this.req = req;
		this.res = res;
		action = new NullAction(req, res);
		arTL.set(this);
		// para simular erro na estrutura do framework antes da 'action'
		//if(true){
		//	throw new RuntimeException("quebrou");
		//}
	}

	public static JerseyAction invoked(){
		return arTL.get();
	}
}
