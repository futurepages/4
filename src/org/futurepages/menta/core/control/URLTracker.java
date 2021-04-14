package org.futurepages.menta.core.control;

import org.futurepages.core.exception.AppLogger;
import org.futurepages.util.CalendarUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class URLTracker {

	public static final String URL_HISTORY_SESSION_KEY = "_fpg__track-url-history_";
	public static final int LOG_MAX_SIZE = 100;
	public static final int LOG_N_FIRST_ITEMS = 5;

	private static  URLTracker INSTANCE = new URLTracker();

	public static URLTracker getInstance(){
		return INSTANCE;
	}


	public synchronized void register(HttpSession session, String url, String referer) {
		try{
			List<String> urlHistory;
			if(session.getAttribute(URL_HISTORY_SESSION_KEY) != null){
				//noinspection unchecked
				urlHistory = (List<String>) session.getAttribute(URL_HISTORY_SESSION_KEY);
				urlHistory.add("["+ CalendarUtil.viewDateTime(CalendarUtil.now(),"dd/MM/yyyy HH:mm:ss") + "] " + url);
			}else{
				urlHistory = new ArrayList<>();
				urlHistory.add("[REFERER] " + (referer!=null? referer : "-"));
				urlHistory.add("["+ CalendarUtil.viewDateTime(CalendarUtil.now(),"dd/MM/yyyy HH:mm:ss") + "] (1st) " + url);
				session.setAttribute(URL_HISTORY_SESSION_KEY, urlHistory);
			}
			if(urlHistory.size() == LOG_MAX_SIZE){
				urlHistory.add(LOG_N_FIRST_ITEMS, "...");
				urlHistory.remove(LOG_N_FIRST_ITEMS+1);
			}else if(urlHistory.size() > LOG_MAX_SIZE){
				urlHistory.remove(LOG_N_FIRST_ITEMS+1);
			}
		}catch (Exception e){
			AppLogger.getInstance().execute(e);
		}
	}

	public List<String> getTrackUrlHistory(HttpServletRequest req){
		if(req.getSession() != null && req.getSession().getAttribute(URL_HISTORY_SESSION_KEY) != null){
			//noinspection unchecked
			return (List<String>) req.getSession().getAttribute(URL_HISTORY_SESSION_KEY);
		}
		return new ArrayList<>();
	}


	public String printTrackUrlHistory(HttpServletRequest req){
		List<String> listToPrint = new ArrayList<>(getTrackUrlHistory(req));
		Collections.reverse(listToPrint);
		return String.join("<br/>", listToPrint);
	}
}
