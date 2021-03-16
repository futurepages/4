package org.futurepages.menta.consequences;

import org.futurepages.menta.core.action.Action;
import org.futurepages.menta.core.ajax.AjaxRenderer;
import org.futurepages.menta.core.consequence.Consequence;
import org.futurepages.menta.core.control.Controller;
import org.futurepages.menta.exceptions.ConsequenceException;
import org.futurepages.util.HttpUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * @author Rubem Azenha (rubem.azenha@gmail.com)
 *         <p>
 *         A Consequence for using Asynchronous JavaScript And XML, also know as
 *         Ajax. This consequence must receive an AjaxRender in its constructor.
 *         The AjaxRender is responsible for getting the action result and
 *         renderize it into a AjaxResponse implementation.
 *         </p>
 *         <p>
 *         Each implementation of the AjaxRender can expect diferents values in
 *         the action`s output and generates differents text results. The result
 *         could be a XML structure, a JSON object, a HTML or even a simple
 *         text.
 *         </p>
 *         <p>
 *         This consequence gets the rendered String and write it`s content to
 *         the client.
 *         </p>
 *         <p>
 *         Then you can use your favorite JavaScript Ajax library (or use your
 *         own library) to parse the generated result and interact it in your
 *         code.
 *         </p>
 */
public class AjaxConsequence implements Consequence {
	
	public static String KEY = "ajax_object";
   
   public static String DEFAULT_CHARSET = "UTF-8";

	private final String key;
    private final AjaxRenderer ajaxRenderer;
    
    private boolean pretty = false;
    
    public AjaxConsequence(AjaxRenderer renderer) {
    	this(KEY, renderer);
    }
  

    public AjaxConsequence(String key, AjaxRenderer renderer) {
    	this.key = key;
        this.ajaxRenderer = renderer;
    }
    
    public AjaxConsequence(AjaxRenderer renderer, boolean pretty) {
    	this(renderer);
    	this.pretty = pretty;
    }
    
    public AjaxConsequence(String key, AjaxRenderer renderer, boolean pretty) {
    	this(key, renderer);
    	this.pretty = pretty;
    }

    /**
     * Prints in the request's output the ajax response. The ajax response is
     * supplied by the AjaxRender.
     */
	@Override
    public void execute(Action a, HttpServletRequest req, HttpServletResponse res) throws ConsequenceException {

		Controller.getInstance().trackURL(req);

		Object obj = a.getOutput().getValue(key);
    	
    	if (obj == null) {
			obj = new HashMap();
		}

        try {
           
            StringBuilder sb = new StringBuilder(64);
            
            sb.append(ajaxRenderer.getContentType()).append("; charset=").append(ajaxRenderer.getCharset());
        	
            res.setContentType(sb.toString());
            
            String s = ajaxRenderer.encode(obj, a.getLocale(), pretty);
            
            HttpUtil.disableCache(res);
            
            PrintWriter printWriter = res.getWriter();
            printWriter.print(s);
            printWriter.flush();

        } catch (IOException e) {
            throw new ConsequenceException(
                    "Exception while writing the renderized document: "
                            + e.getMessage(), e);
        } catch (Exception e) {
            throw new ConsequenceException(
                    "Exception while renderizing with render "
                            + ajaxRenderer.getClass() + ": " + e.getMessage(), e);
        }

    }
}