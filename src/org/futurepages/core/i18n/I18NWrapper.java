package org.futurepages.core.i18n;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.futurepages.core.exception.DefaultExceptionLogger;

class I18NWrapper {
  
    private static long TIME = 30000L;
    
    private long ts = 0L;  
    private long lm = 0;
    private I18N i18n = null;
    private File file = null;
    private String resource = null;
  
    public I18NWrapper(File file) {
        this.file = file;
    }
    
    public I18NWrapper(String resource) {
    	this.resource = resource;
    }
    
    private void reloadFromFile() {
    	
    	FileInputStream fis = null;
    	
        try {
            if (!file.exists()) {
                this.i18n = null;
                return;
            }
            
            fis = new FileInputStream(file);
            Properties prop = new Properties();
            prop.load(fis);
            fis.close();
            this.lm = file.lastModified();
            this.i18n = new I18N(prop);
            
        } catch(IOException e) {
			DefaultExceptionLogger.getInstance().execute(e);
        } finally {
        	
        	if (fis != null) try { fis.close(); } catch(Exception e) { }
        }
    }   
    
    private void loadFromClasspath() {
    	
    	InputStream is = null;
    	
    	try {
    		
    		is = getClass().getResourceAsStream(resource);
    		
        	Properties prop = new Properties();
        	prop.load(is);

        	this.i18n = new I18N(prop);
    		
    	} catch(IOException e) {
    		
			DefaultExceptionLogger.getInstance().execute(e);
    		
    	} finally {
    		
    		if (is != null) try { is.close(); } catch(Exception e) { }
    		
    	}
    	
    }
  
    private boolean needsUpdate(boolean check) {
    	
    	if (resource != null) {
    		
    		return i18n == null;
    		
    	}
    	
        long ctm = System.currentTimeMillis();
        if (check) {
            if ((ctm - ts) > TIME) {
                ts = ctm;
            } else {
                return false;
            }
        }
        return i18n == null || file.lastModified() != lm;
    }
  
    public I18N getI18N() {
    	
        if (needsUpdate(true)) {
        	
            synchronized(file) {
                if (needsUpdate(false)) {
                	
                	if (resource != null) {
                		
                		loadFromClasspath();
                		
                	} else {
                	
                		reloadFromFile();
                		
                	}
                }
            }
        }
        return i18n;
    }
}
