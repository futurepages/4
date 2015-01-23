package org.futurepages.core.i18n;

import java.io.File;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

import org.futurepages.core.control.AbstractApplicationManager;

/**
 * @author Sergio Oliveira
 */
public class I18NMap {
  
    private static Map<String, String> cache = new Hashtable<String, String>();
    private static Map<String, I18NWrapper> map = new Hashtable<String, I18NWrapper>();
    private static Map<String, I18NWrapper> mapCP = new Hashtable<String, I18NWrapper>();
    private static Map<File, I18NWrapper> mapFile = new Hashtable<File, I18NWrapper>();

    protected static String getPath(String file) {
        String path = cache.get(file);
        if (path == null) {
            synchronized(cache) {
                path = cache.get(file);
                if (path == null) {
                    String temp = new String(file);
                    int index = temp.indexOf(".jsp");
                    if (index > 0) {
                        temp = temp.substring(0, index);
                        StringBuffer sb = new StringBuffer(temp);
                        sb.append(".i18n");
                        temp = sb.toString();
                    }
					
					if (!temp.endsWith(".i18n")) {
						StringBuffer sb = new StringBuffer(temp);
						sb.append(".i18n");
						temp = sb.toString();
					}
          
                    StringBuffer sb = new StringBuffer();
                    sb.append(AbstractApplicationManager.getRealPath());
                    sb.append(temp);
          
                    path = sb.toString();
                
                    cache.put(file, path);
                }
            }
        }
        return path;
    }
  
    private static I18NWrapper getI18NWrapper(String file) {
        String path = getPath(file);
        I18NWrapper wrapper = map.get(path);
        if (wrapper == null) {
            synchronized(path) {
                wrapper = map.get(path);
                if (wrapper == null) {
                    File f = new File(path);
                    wrapper = new I18NWrapper(f);
                    map.put(path, wrapper);
                }
            }
        }
        return wrapper;
    }
    

	private static I18NWrapper getI18NWrapperCP(File file) {
        I18NWrapper wrapper = mapFile.get(file);
        
        if (wrapper == null) {
        	
            synchronized(mapFile) {
            	
                wrapper = mapFile.get(file);
                
                if (wrapper == null) {
                	
                    wrapper = new I18NWrapper(file);
                    
                    mapFile.put(file, wrapper);
                }
            }
        }
        return wrapper;
	}
    
    private static I18NWrapper getI18NWrapperCP(String resource) {
    	
        I18NWrapper wrapper = mapCP.get(resource);
        
        if (wrapper == null) {
        	
            synchronized(mapCP) {
            	
                wrapper = mapCP.get(resource);
                
                if (wrapper == null) {
                	
                    wrapper = new I18NWrapper(resource);
                    
                    mapCP.put(resource, wrapper);
                }
            }
        }
        return wrapper;
    }
    
    
    public static I18N getI18N(String file) {
        I18NWrapper wrapper = getI18NWrapper(file);
        return wrapper.getI18N();
    }
    
    public static I18N getI18N(String file, Locale loc) {
    	
    	StringBuilder sb = new StringBuilder(file);
    	
    	sb.append("_").append(loc).append(".i18n");
    	
    	return getI18N(sb.toString());
    }
    
    public static I18N getI18NFromClasspath(String file) {
    	I18NWrapper wrapper = getI18NWrapperCP(file);
    	return wrapper.getI18N();
    }
    
    public static I18N getI18NFromClasspath(String file, Locale loc) {
    	
    	StringBuilder sb = new StringBuilder(file);
    	
    	sb.append("_").append(loc).append(".i18n");
    	
    	return getI18NFromClasspath(sb.toString());
    }


	public static I18N getI18N(File file) {
    	I18NWrapper wrapper = getI18NWrapperCP(file);
    	return wrapper.getI18N();
	}

}