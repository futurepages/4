package org.futurepages.jersey.actions;

import org.futurepages.jersey.core.responses.Error;
import org.futurepages.jersey.core.responses.Success;
import org.futurepages.menta.actions.NullAction;
import org.futurepages.menta.consequences.Forward;
import org.futurepages.menta.core.action.Action;
import org.futurepages.menta.core.input.Input;
import org.futurepages.menta.core.output.Output;
import org.glassfish.jersey.server.mvc.Viewable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;

import static org.futurepages.util.The.concat;

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
	}

	public static JerseyAction invoked(){
		return arTL.get();
	}

	public void output(String key, Object value){
		action.output(key,value);
	}

	public Input getInput(){
		return action.getInput();
	}

	public Output getOutput(){
		return action.getOutput();
	}

	public Response jsp(String path) {
		Forward.outputValues(action,action.getRequest());
		return  Response.ok(new Viewable(path,null)).build();
	}

	public Response redir(String path) {
		Forward.outputValues(action,action.getRequest());
		try {
			return Response.temporaryRedirect(new URI(fixedURLByType("/app"+path,req))).build();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	public Success success(Object obj){
		return new Success(obj);
	}

	public Error error(String... msgs){
		return new Error(msgs);
	}


	public Response redir_out(String path) {
		Forward.outputValues(action,action.getRequest());
		try {
			return Response.temporaryRedirect(new URI(fixedURLByType(path,req))).build();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("Duplicates")
	private String fixedURLByType(String theURL, HttpServletRequest req) {
		if (theURL.indexOf("://") > 0) {
			return theURL;
		} else if (theURL.startsWith("//")) {
			return theURL.substring(1, theURL.length());
		} else {
			return concat(req.getContextPath(), (!theURL.startsWith("/") ? "/" : ""), theURL);
		}
	}
}