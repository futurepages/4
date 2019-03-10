package org.futurepages.core.exception;

import org.futurepages.core.config.Apps;
import org.futurepages.menta.actions.HiddenRequestAction;
import org.futurepages.menta.consequences.Forward;
import org.futurepages.menta.core.action.AbstractAction;
import org.futurepages.menta.core.action.Action;
import org.futurepages.menta.core.control.Controller;
import org.futurepages.menta.exceptions.PageNotFoundException;
import org.futurepages.util.DateUtil;
import org.futurepages.util.EncodingUtil;
import org.futurepages.util.The;
import org.futurepages.util.brazil.BrazilDateUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import static org.futurepages.core.exception.ExceptionLogType.*;


public class AppLogger implements ExceptionLogger{

	private ExceptionExecutor exceptionExecutor;

	public void init() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
		if(Apps.get("LOG_EXCEPTIONS_EXECUTOR")!=null){
			exceptionExecutor = (ExceptionExecutor) Class.forName(Apps.get("LOG_EXCEPTIONS_EXECUTOR")).newInstance();
			exceptionExecutor.init();
			logln(">> LOG EXECUTOR INITIALIZED: "+Apps.get("LOG_EXCEPTIONS_EXECUTOR"));
		}
	}

    private static final AppLogger INSTANCE = new AppLogger();

    public static AppLogger getInstance() {
        return INSTANCE;
    }

	private AppLogger() {}

	public String execute(Throwable throwable, HttpServletRequest req) {
		return execute(throwable, ExceptionLogType.SILENT_EXCEPTION, req, null);
	}

	public String execute(Throwable throwable, Map mapInputs) {
		return execute(throwable,SILENT_EXCEPTION, null, mapInputs);
	}

	public String execute(Throwable throwable, String... inputs) {
        Map<Object, Object> mapInputs = null;
    	if(inputs!=null && inputs.length>0){
	        mapInputs = new HashMap<>();
	        int i = 1;
	        for(Object input : inputs){
				mapInputs.put("input#"+i,input!=null?input.toString():"null");
				i++;
		    }
	    }
		return execute(throwable, SILENT_EXCEPTION, null, mapInputs);
	}

	public String execute(Throwable throwable, ExceptionLogType logType, HttpServletRequest req, Map mapInputs) {

		logType = (throwable instanceof PageNotFoundException)  ? ExceptionLogType.NOT_FOUND : logType;

		String failNumber = System.currentTimeMillis()+"-"+Thread.currentThread().getId();

		StringBuilder logSB = new StringBuilder();
		String exceptionId =  The.concat("\n[",logType,"] ",failNumber);

        logSB.append(logln(exceptionId, "  (", DateUtil.getInstance().viewDateTime(new Date()), ") >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"));

        if(logType==ExceptionLogType.NOT_FOUND){
	        logSB.append(logln("\n[ PAGE NOT FOUND - PAGE NOT FOUND - unnecessary stack trace. ]\n"));
        } else{
			StringWriter errors = new StringWriter();
			throwable.printStackTrace(new PrintWriter(errors));
			logSB.append(logln(errors.toString()));
        }

		if(req!=null){
        	Action action  = req.getAttribute(Forward.ACTION_REQUEST) instanceof Action? (Action) req.getAttribute(Forward.ACTION_REQUEST) : null;
			logSB.append(logln(    ">[url    ]  ", req.getRequestURL().toString(), (req.getQueryString()!=null?"?"+req.getQueryString():"")));
			logSB.append(logln(    ">[referer]  ", req.getHeader("referer")));
			logSB.append(logln(    ">[from   ]  ", AbstractAction.getIpsFromRequest(req)));
			logSB.append(logln(    ">[browser]  ", req.getHeader("user-agent")));
			logSB.append(logln(    ">[proxy  ]  ", req.getHeader("Proxy-Authorization")));
			if (AbstractAction.isLogged(req)) {
				logSB.append(logln(">[user   ]  ", AbstractAction.loggedUser(req).getLogin()));
			}
			logSB.append(logln(   ">[method ]  ", req.getMethod()));

			if(logType!=ExceptionLogType.NOT_FOUND){
				logSB.append(
						log(  ">[request]  ")
				);

				if(action!=null &&  HiddenRequestAction.class.isAssignableFrom(action.getClass())){
					logSB.append(logln("Hidden because it's a Hidden Request Action (maybe some kind of login)."));
				}else{
					for (Object key : req.getParameterMap().keySet()) {
						logSB.append(
								log(key.toString(), ": ",
								The.implodedArray(req.getParameterValues(key.toString()), ",", "'"),
								";"
						));
					}
				}
				logSB.append(logln(""));

				logSB.append(logln(">[session-id] ", req.getSession().getId(), "; ",
						"creation: ", BrazilDateUtil.viewDateTime(new Date(req.getSession().getCreationTime())), "; ",
						"last access: ", BrazilDateUtil.viewDateTime(new Date(req.getSession().getLastAccessedTime())), "; ",
						"max inative interval: ", String.valueOf(req.getSession().getMaxInactiveInterval() / 60), " minutes;"
				));
				logSB.append(log(">[session] "));
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
					logSB.append(log(name, ": '", toStringValue, "';"));
				}
				logSB.append(logln(""));

				if (req.getCookies() != null) {
					logSB.append(log(">[cookies]  (", req.getCookies().length, ") "));
					for (Cookie cookie : req.getCookies()) {
						logSB.append(log(cookie.getName(), ": '", EncodingUtil.decodeUrl(cookie.getValue()), "'; "));
					}
					logSB.append(logln(""));
				}
			}
		}
		if(mapInputs!=null){
			for(Object key : mapInputs.keySet()){
				logSB.append(logln(  ">[",key,"]  ", mapInputs.get(key)));
			}
		}
		logSB.append(logln(exceptionId," <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n"));
		if(exceptionExecutor!=null && logType!=ExceptionLogType.NOT_FOUND && Controller.isInitialized()){
			exceptionExecutor.execute(failNumber, throwable.getMessage()!=null?throwable.getMessage() :"...", logSB.toString());
		}
		return failNumber;
	}

	private String logln(Object... strs){
    	String log = The.concat(strs)+"\n";
		System.out.println(log);
		return log;
	}

	private String log(Object... strs){
    	String log = The.concat(strs);
		System.out.print(log);
		return log;
	}

	public interface ExceptionExecutor {
    	void init();
    	void execute(String failNum, String logTitle, String logStack);
	}
}

