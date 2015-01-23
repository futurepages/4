package org.futurepages.consequences;

import org.futurepages.exceptions.ConsequenceException;
import org.futurepages.core.action.Action;
import java.io.IOException;
import java.util.Iterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.futurepages.core.consequence.Consequence;
import org.futurepages.core.control.Controller;
import org.futurepages.core.output.Output;

/**
 * A forward web consequence.
 * 
 * @author Sergio Oliveira
 */
public class Forward implements Consequence {
    
    /**
     * The action is set in the request with this name
     */
    public static String ACTION_REQUEST = "action";

    /**
     * The session context is set in the request with this name
     */    
    public static String SESSION_REQUEST = "session";

    /**
     * The application context is set in the request with this name
     */    
    public static String APPLICATION_REQUEST = "application";
    
    /**
     * The action input is set in the request with this name
     */        
    public static String INPUT_REQUEST = "input";
    
    private String url;
	
	/**
	 * Creates a web consequence for the given url.
	 * 
	 * @param url The url for the forward
	 */
	public Forward(String url) {
		this.url = putSlash(url);
	}
    
    private String putSlash(String url) {
        if (url != null && !url.startsWith("/")) {
            return "/" + url;
        }
        return url;
    }
    
	@Override
    public void execute(Action a, HttpServletRequest req, HttpServletResponse res) throws ConsequenceException {
        try {
            // put output values in the request...
            
            if (a != null) {
            
                Output output = a.getOutput();
                Iterator<String> iter = output.keys();
                while(iter.hasNext()) {
                    String key = (String) iter.next();
                    Object value = output.getValue(key);
                    req.setAttribute(key, value);
                }
                
                // put the application in the request...
                req.setAttribute(APPLICATION_REQUEST, a.getApplication());
                
                // put the session in the request...
                req.setAttribute(SESSION_REQUEST, a.getSession());
                
                // put the input in the request...
                req.setAttribute(INPUT_REQUEST, a.getInput());
                
                // put the action in the request...
                req.setAttribute(ACTION_REQUEST, a);
            
            }
            forward(this.getUrl(), req, res);
            
        } catch(Exception e) {
            throw new ConsequenceException(e);
        }
    }
    
    /**
     * Forward your web application to an URL.
     *
     * @param url The url for the forward.
     * @param req The http servlet request.
     * @param res the http servlet response.
     */
	public static void forward(String url, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
			ServletContext app = Controller.getInstance().getApplication();
			RequestDispatcher rd = app.getRequestDispatcher(url);
            //res.setBufferSize(41943040); //descomentar para usar JSP enquanto ajeitam o bug do tomcat 8.0.17 .... 40MB //TODO NAO SUBMETER!!!
			rd.forward(req, res);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(100);
        sb.append("Forward to ").append(url);
        return sb.toString();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = putSlash(url);
    }
}