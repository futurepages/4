package org.futurepages.core.exception;

import org.futurepages.exceptions.PageNotFoundException;
import org.futurepages.util.BrazilianDateUtil;
import org.futurepages.util.EncodingUtil;
import org.futurepages.util.StringUtils;
import org.futurepages.util.The;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Enumeration;


public class DefaultExceptionLogger implements ExceptionLogger{

    private static final DefaultExceptionLogger INSTANCE = new DefaultExceptionLogger();

    public static DefaultExceptionLogger getInstance() {
        return INSTANCE;
    }

	private DefaultExceptionLogger() {}

	public String execute(Throwable throwable) {
		return execute(throwable, ExceptionLogType.SILENT_EXCEPTION.name(),null);
	}

	public String execute(Throwable throwable, String errorType, HttpServletRequest req) {

		boolean pageNotFoundEx = (throwable instanceof PageNotFoundException);

		String numeroProtocolo = System.currentTimeMillis()+"-"+Thread.currentThread().getId();

		String exceptionId =  StringUtils.concat("[",errorType.toUpperCase(),"] ",numeroProtocolo);
        log(exceptionId , "  ("  , BrazilianDateUtil.viewDateTime(new Date()) , ") >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");


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
						"creation: ", BrazilianDateUtil.viewDateTime(new Date(req.getSession().getCreationTime())), "; ",
						"last access: ", BrazilianDateUtil.viewDateTime(new Date(req.getSession().getLastAccessedTime())), "; ",
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
		System.out.println(StringUtils.concat(strs));
	}
	
	private enum ExceptionLogType {
		EXCEPTION,
		SERVLET_500,
		SILENT_EXCEPTION
	}
}