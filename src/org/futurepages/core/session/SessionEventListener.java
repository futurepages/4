package org.futurepages.core.session;

import javax.servlet.http.HttpSession;

public interface SessionEventListener {

    public void onCreate(HttpSession session);

	public void onDestroy(HttpSession session);
    
}