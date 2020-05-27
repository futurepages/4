package org.futurepages.core.exception;

import org.futurepages.core.auth.DefaultUser;
import org.futurepages.core.config.Apps;
import org.futurepages.menta.actions.HiddenRequestAction;
import org.futurepages.menta.consequences.Forward;
import org.futurepages.menta.core.action.AbstractAction;
import org.futurepages.menta.core.action.Action;
import org.futurepages.menta.core.control.Controller;
import org.futurepages.menta.exceptions.PageNotFoundException;
import org.futurepages.util.DateUtil;
import org.futurepages.util.EncodingUtil;
import org.futurepages.util.Security;
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

	public static Map mappedInputs(String... inputs) {
		Map<Object, Object> mapInputs = new HashMap<>();
		if(inputs!=null && inputs.length>0){
			int i = 1;
			for(Object input : inputs){
				mapInputs.put("input#"+i,input!=null?input.toString():"(null)");
				i++;
			}
		}
		return mapInputs;
	}

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

	public String exception(Throwable throwable, String... inputs) {
		return execute(throwable, EXCEPTION, null, mappedInputs(inputs));
	}

	public String silent(Throwable throwable, String... inputs) {
		return execute(throwable, SILENT_EXCEPTION, null, mappedInputs(inputs));
	}

	public String execute(Throwable throwable, Map mapInputs) {
		return execute(throwable,SILENT_EXCEPTION, null, mapInputs);
	}

	public String execute(Throwable throwable, String... inputs) {
		return execute(throwable, SILENT_EXCEPTION, null, mappedInputs(inputs));
	}

	public String execute(Throwable throwable, ExceptionLogType logType, HttpServletRequest req, Map mapInputs) {

		logType = (throwable instanceof PageNotFoundException)  ? ExceptionLogType.NOT_FOUND : logType;

		String failNumber = System.currentTimeMillis()+"-"+Thread.currentThread().getId();

		StringBuilder logSB = new StringBuilder();
		String exceptionId =  The.concat("\n[",logType,"] ",failNumber);

        logSB.append(logln(exceptionId, "  (", DateUtil.getInstance().viewDateTime(new Date()), ") >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"));

		StringWriter errors = new StringWriter();
        String stackHash = "";
        boolean simple404 = logType==ExceptionLogType.NOT_FOUND && (req!=null &&  (req.getHeader("referer")==null || req.getHeader("referer").equals(req.getRequestURL().toString())));
        if(simple404){
	        logSB.append(logln(throwable.getMessage()));
        } else{
			throwable.printStackTrace(new PrintWriter(errors));
			logSB.append(logln(errors.toString()));
	        stackHash = Security.md5(errors.toString());
        }

        if(req==null && Controller.getInstance()!=null && Controller.getInstance().getChain()!=null && Controller.getInstance().getChain().getAction()!=null){
            req = Controller.getInstance().getChain().getAction().getRequest();
		}

		if(req!=null){
        	Action action  = req.getAttribute(Forward.ACTION_REQUEST) instanceof Action? (Action) req.getAttribute(Forward.ACTION_REQUEST) : null;
			if(!simple404){
				try{
					logSB.append(logln(    ">[url    ]  ", req.getRequestURL()!=null? (req.getRequestURL().toString() + (req.getQueryString()!=null?"?"+req.getQueryString():"")):"?"));
				}catch (Exception ignored){}
				logSB.append(logln(    ">[referer]  ", req.getHeader("referer")));
				try{
					logSB.append(logln(    ">[from   ]  ", AbstractAction.getIpsFromRequest(req)));
				}catch (Exception ignored){}
				logSB.append(logln(    ">[browser]  ", req.getHeader("user-agent")));
				logSB.append(logln(    ">[proxy  ]  ", req.getHeader("Proxy-Authorization")));
				try{
				DefaultUser user = AbstractAction.loggedUser(req);
				if (user!=null) {
					logSB.append(logln(">[user   ]  ", user.getLogin()," | ", user.getEmail() ," | ",user.getFullName()));
				}
				}catch (Exception ignored){}
				logSB.append(logln(   ">[method ]  ", req.getMethod()));
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

				logSB.append(logln(">[session-id] ", (req.getSession()!=null?req.getSession().getId():""), "; ",
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
		} else{
			logSB.append(logln(    ">[request]  null"));
		}
		if(mapInputs!=null){
			for(Object key : mapInputs.keySet()){
				logSB.append(logln(  ">[",key,"]  ", mapInputs.get(key)));
			}
		}
		if(!simple404){
			logSB.append(logln(exceptionId," <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n"));
		}
		if(exceptionExecutor!=null && !simple404 && Controller.isInitialized()){
			exceptionExecutor.execute(failNumber, throwable.getMessage()!=null?throwable.getMessage() :throwable.getClass().getSimpleName(), stackHash, logSB.toString());
		}else{
			System.out.println(logSB.toString());
		}
		return failNumber;
	}

	private String logln(Object... strs){
		return The.concat(strs)+"\n";
	}

	private String log(Object... strs){
		return The.concat(strs);
	}

	public interface ExceptionExecutor {
    	void init();
    	void execute(String failNum, String logTitle, String stackHash, String logStack);
	}
}

