package org.futurepages.menta.filters;


import org.futurepages.core.exception.ExceptionLogger;
import org.futurepages.exceptions.UserException;
import org.futurepages.menta.actions.HiddenRequestAction;
import org.futurepages.menta.annotations.NotListDependencies;
import org.futurepages.menta.core.action.AbstractAction;
import org.futurepages.menta.core.action.Action;
import org.futurepages.menta.core.action.AsynchronousManager;
import org.futurepages.menta.core.action.Manipulable;
import org.futurepages.menta.core.control.Controller;
import org.futurepages.menta.core.control.InvocationChain;
import org.futurepages.menta.core.filter.Filter;
import org.futurepages.menta.exceptions.FuturepagesServletException;
import org.futurepages.menta.exceptions.PageNotFoundException;
import org.futurepages.menta.exceptions.ServletErrorException;
import org.futurepages.util.EncodingUtil;
import org.futurepages.util.The;
import org.futurepages.util.brazil.BrazilDateUtil;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Enumeration;

public class ExceptionFilter implements Filter {

	
	@Override
	public String filter(InvocationChain chain) throws Exception {
		try {
			return chain.invoke();
		} catch (Throwable throwable) {
			return treatedException(chain, throwable);
		}
	}

	@Override
	public void destroy() {
	}


	/**
	 * 
	 * @return DYN_EXCEPTION ou EXCEPTION para exceptions esperadas, ERROR para exceptions esperadas.
	 */
	public static String treatedException(InvocationChain chain, Throwable ex) throws ServletException {
		Throwable cause = ex;
		if(ex.getCause() != null){
			cause = ex.getCause();
		}
		boolean isErrorException = (cause instanceof UserException);
		if(isErrorException){ //Erros causados por Exceptions Esperadas (ErrorExceptions)
			Action action = chain.getAction();
			UserException errorException = (UserException) cause;
			boolean listDependencies = true;
			if (cause.getClass().isAnnotationPresent(NotListDependencies.class)
			 || chain.getMethod().isAnnotationPresent(NotListDependencies.class)) {
				listDependencies = false;
			}
			return ((AbstractAction) action).putError(listDependencies, errorException);
		}else{
			if(cause instanceof ServletException){
				throw ((ServletException)cause);  //Erro 500.
			}
			//Exceptions causadas por erros inesperados (muito provavelmente retornará EXCEPTION ou DYN_EXCEPTION)
			return LegacyExceptionLogger.getInstance().execute(cause, chain, chain.getAction().getRequest() , false);
		}
	}
}

class LegacyExceptionLogger implements ExceptionLogger, Manipulable {

    private static final LegacyExceptionLogger INSTANCE = new LegacyExceptionLogger();

    public static LegacyExceptionLogger getInstance() {
        return INSTANCE;
    }

	private LegacyExceptionLogger() {}

	public String execute(Throwable throwable) {
		return execute(throwable, ExceptionLogType.SILENT_EXCEPTION.name(),null);
	}

	public String execute(Throwable throwable, String errorType, HttpServletRequest req) {

		boolean pageNotFoundEx = (throwable instanceof PageNotFoundException);

		String numeroProtocolo = System.currentTimeMillis()+"-"+Thread.currentThread().getId();

		InvocationChain chain = Controller.getInstance()!=null ? Controller.getInstance().getChain() : null;
		String exceptionId =  The.concat("[",errorType.toUpperCase(),"] ",numeroProtocolo);
        log(exceptionId , "  ("  , BrazilDateUtil.viewDateTime(new Date()) , ") >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		if(chain!=null){
			if(req == null){
				req =  chain.getAction().getRequest();
			}
		}

		if(!pageNotFoundEx){
			throwable.printStackTrace();
		} else {
			log("\n[ PAGE NOT FOUND - PAGE NOT FOUND - unnecessary stack trace. ]\n");
		}


		if(req!=null){
			log(">[url    ]  ", req.getRequestURL().toString(), (req.getQueryString()!=null?"?"+req.getQueryString():""));
			log(">[referer]  ", req.getHeader("referer"));
			log(">[from   ]  ", AbstractAction.getIpsFromRequest(req));
			log(">[browser]  ", req.getHeader("user-agent"));
			log(">[proxy  ]  ", req.getHeader("Proxy-Authorization"));
			if(AbstractAction.isLogged(req)){
			log(">[user   ]  ", AbstractAction.loggedUser(req).getLogin());
			}
			log(">[method ]  ", req.getMethod());

			if (!pageNotFoundEx) {
				System.out.print(">[request]  ");
				if (chain != null) {
					if (!(HiddenRequestAction.class.isAssignableFrom(chain.getAction().getClass()))) {
						for (Object key : req.getParameterMap().keySet()) {
							System.out.print(The.concat(key.toString(), ": ",
											The.implodedArray(req.getParameterValues(key.toString()), ",", "'"),
											";"
									)
							);
						}
					} else { //é  LoginAction ou similar que não pode exibir alguma senha no request.
						System.out.println("<< hidden because it's a Hidden Request Action (maybe some kind of login) >>");
					}
				} else { //chain == null
					for (Object key : req.getParameterMap().keySet()) {
						System.out.print(The.concat(key.toString(), ": ",
								The.implodedArray(req.getParameterValues(key.toString()), ",", "'"),
								";"
						)
						);
					}
				}
				System.out.println();

				log(">[session]  id: ", req.getSession().getId(), "; ",
						"creation: ", BrazilDateUtil.viewDateTime(new Date(req.getSession().getCreationTime())), "; ",
						"last access: ", BrazilDateUtil.viewDateTime(new Date(req.getSession().getLastAccessedTime())), "; ",
						"max inative interval: ", String.valueOf(req.getSession().getMaxInactiveInterval() / 60), " minutes;"
				); //TODO - informacoes de tempo da sessao
				System.out.print(">[session]  ");
				Enumeration ralist = req.getSession().getAttributeNames();
				while (ralist.hasMoreElements()) {
					String name = (String) ralist.nextElement();
					String toStringValue = req.getSession().getAttribute(name).toString();
					if(toStringValue.length()>200){
						toStringValue = toStringValue.substring(0,197)+" (...)";
					}
					if(toStringValue.contains("\n")){
						toStringValue = toStringValue.replaceAll("\\s+"," ");
					}
					System.out.print(The.concat(name, ": '", toStringValue, "';"));
				}
				System.out.println();

				if (req.getCookies() != null) {
					System.out.print(">[cookies]  (" + req.getCookies().length + ") ");
					for (Cookie cookie : req.getCookies()) {
						System.out.print(The.concat(cookie.getName(), ": '", EncodingUtil.decodeUrl(cookie.getValue()), "'; "));
					}
					System.out.println();
				}
			}
		}

		log("\n",exceptionId," <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n");
		return numeroProtocolo;
	}

	public String execute(Throwable throwable, InvocationChain chain, HttpServletRequest req, boolean status500) throws ServletException {

		String actionType = null;
		if(chain!=null){
			if(AsynchronousManager.isAsynchronousAction(chain)){
				actionType = DYN_EXCEPTION;
			}else{
				actionType = EXCEPTION;
			}
		}


		if(status500){
			Throwable ex;
			if(throwable.getCause() != null){
				ex = throwable.getCause();
			}else{
				ex = throwable;
			}
			if(ex instanceof ServletErrorException){
				throw (ServletErrorException) ex;
			}
			String protocolNumber = execute(ex,ExceptionLogType.SERVLET_500.name(),req);
			throw new FuturepagesServletException(protocolNumber, actionType, ex);
		}else if(chain!=null){
			String protocolNumber = execute(throwable, actionType, chain.getAction().getRequest());
			chain.getAction().getOutput().setValue(EXCEPTION, new Exception(protocolNumber,throwable));
		}
		return actionType;
	}

	private void log(String... strs){
		System.out.println(The.concat(strs));
	}

	private enum ExceptionLogType {
		EXCEPTION,
		SERVLET_500,
		DYN_EXCEPTION,
		SILENT_EXCEPTION
	}
}