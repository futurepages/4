package org.futurepages.core.exception;

//import org.futurepages.core.control.UI;
//import org.futurepages.core.locale.Txt;
//import org.futurepages.core.locale.TxtNotFoundException;
import org.futurepages.util.DateUtil;
import org.futurepages.util.EncodingUtil;
import org.futurepages.util.The;
import org.futurepages.util.brazil.BrazilDateUtil;

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
		return execute(throwable, null);
	}

	public String execute(Throwable throwable, HttpServletRequest vaadinReq) {
		HttpServletRequest req = null;
		if(vaadinReq!=null){
			req = (HttpServletRequest) vaadinReq;
		}
		ExceptionLogType logType;
//TODO: txt not installed yet.
//		if(throwable instanceof TxtNotFoundException){
//			logType = ExceptionLogType.TXT_NOT_FOUND;
//		}else{
			logType = ExceptionLogType.INTERNAL_FAIL;
//		}

		String failNumber = System.currentTimeMillis()+"-"+Thread.currentThread().getId();

		String exceptionId =  The.concat("[",logType,"] ",failNumber);

//		if(logType!=ExceptionLogType.TXT_NOT_FOUND){
	        log(exceptionId, "  (", DateUtil.getInstance().viewDateTime(new Date()), ") >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n");
			throwable.printStackTrace();
//		}

//		if(logType==ExceptionLogType.TXT_NOT_FOUND){
//			log("\n[",logType,"] "," (", DateUtil.getInstance().viewDateTime(new Date()), ")");
//			log("\t",throwable.getMessage());
//			for(StackTraceElement el : throwable.getStackTrace()){
//				if(!el.getClassName().equals(Txt.class.getName())){
//					log("\t\tat "+el+"\n");
//					break;
//				}
//			}
//		}

		if(req!=null){
			log("\n>[url    ]  ", req.getRequestURL().toString(), (req.getQueryString()!=null?"?"+req.getQueryString():""));
			log(">[referer]  ", req.getHeader("referer"));
			log(">[browser]  ", req.getHeader("user-agent"));
			log(">[proxy  ]  ", req.getHeader("Proxy-Authorization"));

// TODO: new futurepages need UI implemented.
//			if(UI.getCurrent().getLoggedUser()!=null){
//				log(">[user   ]  ", UI.getCurrent().getLoggedUser().getLogin());
//			}
			log(">[method ]  ", req.getMethod());

			logInline(">[request]  ");
			for (Object key : req.getParameterMap().keySet()) {
				log(key.toString(), ": ",
						The.implodedArray(req.getParameterValues(key.toString()), ",", "'"),
						";"
				);
			}
			System.err.println();
			log(">[session]  id: ", req.getSession().getId(), "; ",
					"creation: ", BrazilDateUtil.viewDateTime(new Date(req.getSession().getCreationTime())), "; ",
					"last access: ", BrazilDateUtil.viewDateTime(new Date(req.getSession().getLastAccessedTime())), "; ",
					"max inative interval: ", String.valueOf(req.getSession().getMaxInactiveInterval() / 60), " minutes;"
			);
			logInline(">[session] ");
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
				logInline(name, ": '", toStringValue, "';");
			}
			System.err.println();

			if (req.getCookies() != null) {
				logInline(">[cookies]  (", req.getCookies().length, ") ");
				for (Cookie cookie : req.getCookies()) {
					logInline(cookie.getName(), ": '", EncodingUtil.decodeUrl(cookie.getValue()), "'; ");
				}
				System.err.println();
			}
		}
		if(logType!=ExceptionLogType.TXT_NOT_FOUND){
			log("\n",exceptionId," <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n");
		}
		return failNumber;
	}


	private void log(Object... strs){
		System.err.println(The.concat(strs));
	}

	private void logInline(Object... strs){
		System.err.print(The.concat(strs));
	}
	
	private enum ExceptionLogType {
		INTERNAL_FAIL, TXT_NOT_FOUND
	}
}