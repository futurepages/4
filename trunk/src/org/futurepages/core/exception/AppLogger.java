package org.futurepages.core.exception;

import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServletRequest;
import org.futurepages.apps.simple.SimpleUI;
import org.futurepages.core.locale.LocaleManager;
import org.futurepages.core.locale.Txt;
import org.futurepages.core.locale.TxtNotFoundException;
import org.futurepages.util.EncodingUtil;
import org.futurepages.util.The;
import org.futurepages.util.brazil.DateUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
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

	public String execute(Throwable throwable, VaadinRequest vaadinReq) {
		HttpServletRequest req = null;
		if(vaadinReq!=null){
			req = ((VaadinServletRequest)vaadinReq).getHttpServletRequest();
		}
		ExceptionLogType logType;
		if(throwable instanceof TxtNotFoundException){
			logType = ExceptionLogType.TXT_NOT_FOUND;
		}else{
			logType = ExceptionLogType.INTERNAL_FAIL;
		}

		String failNumber = System.currentTimeMillis()+"-"+Thread.currentThread().getId();

		String exceptionId =  The.concat("[",logType,"] ",failNumber);

		DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.MEDIUM, LocaleManager.getDefaultLocale());
        log(exceptionId, "  (", formatter.format(new Date()), ") >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n");

		if(logType==ExceptionLogType.TXT_NOT_FOUND){
			log(throwable.getMessage());
			for(StackTraceElement el : throwable.getStackTrace()){
				if(!el.getClassName().equals(Txt.class.getName())){
					log("\tat "+el);
					break;
				}
			}
		}else{
			throwable.printStackTrace();
		}
		if(req!=null){
			log("\n>[url    ]  ", req.getRequestURL().toString(), (req.getQueryString()!=null?"?"+req.getQueryString():""));
			log(">[state  ]  ", SimpleUI.getCurrent() != null && SimpleUI.getCurrent().getNavigator() != null ? SimpleUI.getCurrent().getNavigator().getState():"<< null >>");
			log(">[referer]  ", req.getHeader("referer"));
			log(">[browser]  ", req.getHeader("user-agent"));
			log(">[proxy  ]  ", req.getHeader("Proxy-Authorization"));

			if(SimpleUI.getCurrent().getLoggedUser()!=null){
				log(">[user   ]  ", SimpleUI.getCurrent().getLoggedUser().getLogin());
			}
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
					"creation: ", DateUtil.viewDateTime(new Date(req.getSession().getCreationTime())), "; ",
					"last access: ", DateUtil.viewDateTime(new Date(req.getSession().getLastAccessedTime())), "; ",
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
		log("\n",exceptionId," <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n");
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