package org.futurepages.consequences;

import org.futurepages.exceptions.ConsequenceException;
import org.futurepages.core.action.Action;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.futurepages.core.consequence.Consequence;
import org.futurepages.core.output.Output;

/**
 * 
 * A web-based consequence that gets a ready-to-use input stream
 * from the output of the action, get the contents from that input
 * stream and flush them to the servlet output stream.
 * Very useful for loading images from a database, downloads and 
 * other resources that the programmer may want to protect from
 * direct access from the client.
 * This class is <i>thread-safe</i>.
 *
 * @author Rubem Azenha
 */
public class StreamConsequence implements Consequence {
    
    /** The default key ("<i>stream</i>") to look for in the action output. */
    public static final String STREAM_KEY = "stream";
    
    /** The default key ("<i>contentDisposition</i>") to look for in the action output. */
    public static final String CONTENT_DISPOSITION_KEY = "contentDisposition";
    
    /** The default key ("<i>contentLength</i>") to look for in the action output. */
    public static final String CONTENT_LENGTH_KEY = "contentLength";
    
    /** The default key ("<i>contentType</i>") to look for in the action output. */
    public static final String CONTENT_TYPE_KEY = "contentType";
    
    public static final String FILENAME_KEY = "filename";

	// The content-type of the message
	private String contentType;
    
    // The key with which to get the stream from the output...
    private String stream_key = STREAM_KEY;
    
    // The key with which to get the content disposition from the output...
    private String content_disposition_key = CONTENT_DISPOSITION_KEY;
    
    // The key with which to get the content length from the output...
    private String content_length_key = CONTENT_LENGTH_KEY;
    
    private String content_type_key = CONTENT_TYPE_KEY;
    
    private String filename_key = FILENAME_KEY;
    
    /**
     * Creates a new instance of the StreamConsequence class.
     * The content type and everything else will be dynamic, in
     * other words, defined by the action at runtime.
     */
    public StreamConsequence() {
    	
    }
    
	/**
	 * Creates a new instance of the StreamConsequence class,
	 * with the given content-type
     *
     * @param contentType The content type to set in the servlet response.
	 */
	public StreamConsequence(String contentType) {
	    this.contentType = contentType;
    }
    
	/**
	 * Creates a new instance of the StreamConsequence class,
	 * with the given content-type and key.
     *
     * @param contentType The content type to set in the servlet response.
     * @param stream_key The key with which to get the stream from the output.
	 */
	public StreamConsequence(String contentType, String stream_key) {
	    this(contentType);
        this.stream_key = stream_key;
    }    
    
	/**
	 * Creates a new instance of the StreamConsequence class,
	 * with the given content-type and key.
     *
     * @param contentType The content type to set in the servlet response.
     * @param stream_key The key with which to get the stream from the output.
     * @param content_disposition_key The key with which to get the content disposition from the output.
	 */
	public StreamConsequence(String contentType, String stream_key, String content_disposition_key) {
	    this(contentType, stream_key);
        this.content_disposition_key = content_disposition_key;
    }        
    
	/**
	 * Creates a new instance of the StreamConsequence class,
	 * with the given content-type and key.
     *
     * @param contentType The content type to set in the servlet response.
     * @param stream_key The key with which to get the stream from the output.
     * @param content_disposition_key The key with which to get the content disposition from the output.
     * @param content_length_key The key with which to get the content length from the output.
	 */
	public StreamConsequence(String contentType, String stream_key, String content_disposition_key, String content_length_key) {
	    this(contentType, stream_key, content_disposition_key);
        this.content_length_key = content_length_key;
    } 
	
	public void setFileNameKey(String filename_key) {
		
		this.filename_key = filename_key;
	}
	
	public void setContentTypeKey(String key) {
		this.content_type_key = key;
	}
    
	/**
	 * Executes the action, reading from the input stream the data that
	 * has to be flushed into the servlet output stream.
	 */
	public void execute(Action a, HttpServletRequest req, HttpServletResponse res) throws ConsequenceException {
		Output output = a.getOutput();
        defineHeader(res, output);
		OutputStream outputStream = null;
		try {
			outputStream = res.getOutputStream();
		} catch (IOException e) {
			throw new ConsequenceException(e);
		}
        
        Object obj = output.getValue(stream_key);
        try {
            if (obj instanceof InputStream) {
            	
            	// change done by: Luiz Antonio de Abreu Pereira
            	
            	InputStream in = (InputStream) obj;
            	 byte[] buffer = new byte[4092];
            	 int size = 0;
            	 while((size = in.read(buffer, 0, buffer.length)) != -1) {
            		 
            	     outputStream.write(buffer, 0, size);
            	 }
            	 
                outputStream.flush();
                
            } else if (obj instanceof byte[]) {
                byte [] b = (byte []) obj;
                outputStream.write(b);
                outputStream.flush();
            } else {
                throw new ConsequenceException("Cannot find stream: " + stream_key);
            }
        } catch(Exception e) {
            throw new ConsequenceException(e);
        }
    }
    
	protected void defineHeader(HttpServletResponse res, Output output) throws ConsequenceException {
		
		if (contentType == null) {
			
			Object o = output.getValue(content_type_key);
			
			if (o == null || !(o instanceof String)) {
				throw new ConsequenceException("Content-type not defined!");
			}
			
			res.setContentType(o.toString());
			
		} else {
		
			res.setContentType(contentType);
			
		}
        
        Object contentLength = output.getValue(content_length_key);
        
        if (contentLength != null && contentLength instanceof Number) {
            res.setContentLength(((Number) contentLength).intValue());
        }
        
		Object contentDisposition = output.getValue(content_disposition_key);
		
		Object filename = output.getValue(filename_key);
		
		if (filename != null) {
			
			res.setHeader("Content-Disposition", "inline;filename=" + filename);
			
		} else if (contentDisposition == null) {
			
            res.setHeader("Content-Disposition", "inline");
            
        } else {
        	
            res.setHeader("Content-Disposition", contentDisposition.toString());
        }
	}    
}