package org.futurepages.core.exception;

import org.futurepages.exceptions.PageNotFoundException;
import org.futurepages.util.EncodingUtil;
import org.futurepages.util.The;
import org.futurepages.util.brazil.DateUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Enumeration;


public class AppLogger implements ExceptionLogger{

    private static final AppLogger INSTANCE = new AppLogger();

    public static AppLogger getInstance() {
        return INSTANCE;
    }

	private AppLogger() {}

	public String execute(Throwable throwable) {
		return execute(throwable, ExceptionLogType.INTERNAL_FAIL.name(),null);
	}

	public String execute(String msg) {
		return execute(new Exception(msg), ExceptionLogType.INTERNAL_FAIL.name(),null);
	}

	public String execute(Throwable throwable, String errorType, HttpServletRequest req) {

		boolean pageNotFoundEx = (throwable instanceof PageNotFoundException);

		String numeroProtocolo = System.currentTimeMillis()+"-"+Thread.currentThread().getId();

		String exceptionId =  The.concat("[",errorType.toUpperCase(),"] ",numeroProtocolo);
		//TODO remove brazilian date and put it acoording to the app locale. App.get("LOCALE")
        log(exceptionId , "  ("  , DateUtil.viewDateTime(new Date()) , ") >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");


		if(!pageNotFoundEx){
			throwable.printStackTrace();
		} else {
			log("\n[ PAGE NOT FOUND - PAGE NOT FOUND - unnecessary stack trace. ]\n");
		}


		if(req!=null){
			log(">[url    ]  ", req.getRequestURL().toString(), (req.getQueryString()!=null?"?"+req.getQueryString():""));
			log(">[referer]  ", req.getHeader("referer"));
			log(">[browser]  ", req.getHeader("user-agent"));
			log(">[proxy  ]  ", req.getHeader("Proxy-Authorization"));
//			if(AbstractAction.isLogged(req)){
//			log(">[user   ]  ", AbstractAction.loggedUser(req).getLogin());
//			}
			log(">[method ]  ", req.getMethod());

			if (!pageNotFoundEx) {
				System.out.print(">[request]  ");

					for (Object key : req.getParameterMap().keySet()) {
						System.out.print(The.concat(key.toString(), ": ",
								The.implodedArray(req.getParameterValues(key.toString()), ",", "'"),
								";"
						)
						);
					}

				System.out.println();

				log(">[session]  id: ", req.getSession().getId(), "; ",
						"creation: ", DateUtil.viewDateTime(new Date(req.getSession().getCreationTime())), "; ",
						"last access: ", DateUtil.viewDateTime(new Date(req.getSession().getLastAccessedTime())), "; ",
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



	private void log(String... strs){
		System.err.println(The.concat(strs));
	}
	
	private enum ExceptionLogType {
		INTERNAL_FAIL
	}
}