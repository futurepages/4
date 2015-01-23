package org.futurepages.core.ajax.tools;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.futurepages.consequences.AjaxConsequence;
import org.futurepages.core.ajax.AjaxRenderer;
import org.futurepages.util.InjectionUtils;

public class BeanCollectionAjaxRenderer implements AjaxRenderer {
	
    //public static final String DEFAULT_COLLECTION_ATTR = "beanCollection";
    
    public static final String DEFAULT_ROOT_TAG = "father";
    
    public static final String DEFAULT_ENTRY_TAG = "child";
	
	private String root = DEFAULT_ROOT_TAG;
    private String child = DEFAULT_ENTRY_TAG;
    
	private String[] subTagsAttrs; // bean attributes for sub tags...
    private String[] attrsAttrs;   // bean attributes for tag attributes...
    
    private void printCR(StringBuilder sb, boolean pretty) {
    	
    	if (!pretty) return;
    	
    	sb.append('\n');
    }
    
    private void printTab(StringBuilder sb, boolean pretty, int x) {
    	
    	if (!pretty) return;
    	
    	for(int i=0;i<x;i++) sb.append("   ");
    }
	
    public String encode(Object obj, Locale loc, boolean pretty) throws Exception {
    	
    	if (!(obj instanceof Collection)) throw new IllegalArgumentException("Object is not a Collection: " + obj);
    	
    	Collection coll = (Collection) obj;
    	
        StringBuilder sb = new StringBuilder();
        
        sb.append('<').append(root).append('>');
        
        printCR(sb, pretty);
        
        for(Iterator iter = coll.iterator(); iter.hasNext();) {
        	
        	Object bean = iter.next();
        	
            Class<? extends Object> klass = bean.getClass();
            
            printTab(sb, pretty, 1);
            
            sb.append('<').append(child);
        	
            if (attrsAttrs != null) {
            	
            	for (int i = 0; i < attrsAttrs.length; i++) {
            		
            		String attrName = attrsAttrs[i];
            		
                    Method method = InjectionUtils.findMethodToGet(klass, attrName);
                    
                    if (method == null) throw new Exception("Cannot find method to get: " + klass + " / " + attrName);
                    	
                    sb.append(' ').append(attrName).append("=\"").
                    
                    append(method.invoke(bean, (Object[]) null)).append("\"");
            	}
            	
            } else {
            	
            	sb.append(' ');
            }
            
            sb.append('>');
            
            printCR(sb, pretty);
            
            if (subTagsAttrs != null) {
            	
            	for (int i = 0; i < subTagsAttrs.length; i++) {
            		
            		String attrName = subTagsAttrs[i];
            		
            		Method method = InjectionUtils.findMethodToGet(klass, attrName);
            		
            		if (method == null) throw new Exception("Cannot find method to get: " + klass + " / " + attrName);
            		
            		printTab(sb, pretty, 2);
            		
            		sb.append('<').append(attrName).append('>').
            		
            		append(method.invoke(bean, (Object[]) null)).append("</").append(attrName).append('>');
            		
            		printCR(sb, pretty);
            	}
            }
            
            printTab(sb, pretty, 1);
            
            sb.append("</").append(child).append('>');
            
            printCR(sb, pretty);
        }
        
        sb.append("</").append(root).append('>');
        
        printCR(sb, pretty);
        
    	return sb.toString();
	}

	public BeanCollectionAjaxRenderer(String rootTag, String childTag,  String[] subTagsAttrs, String[] attrsAttrs) {
		
		this.root = rootTag;
		
		this.child = childTag;

		this.attrsAttrs = attrsAttrs;
		
		this.subTagsAttrs = subTagsAttrs;
	}


	public BeanCollectionAjaxRenderer(String[] subTagsAttrs, String[] attrsAttrs) {
		
		this.attrsAttrs = attrsAttrs;
		
		this.subTagsAttrs = subTagsAttrs;
	}


    public String getContentType() {
        return TEXT_XML;
    }
    
    public String getCharset() {
       return AjaxConsequence.DEFAULT_CHARSET;
    }
    
    private static class User {
    	
    	private final String name, username, email;
    	
    	public User(String name, String username, String email) {
    		
    		this.name = name;
    		
    		this.username = username;
    		
    		this.email = email;
    	}
    	
    	public String getName() { return name; }
    	
    	public String getUsername() { return username; }
    	
    	public String getEmail() { return email; }
    	
    }
    
    public static void main(String[] args) throws Exception {
    	
    	List<User> list = new LinkedList<User>();
    	
    	list.add(new User("sergio", "soliveira", "sergio@sergio.com.br"));
    	list.add(new User("patty", "pmesseder", "patty@patty.com.br"));
    	
    	AjaxRenderer render = new BeanCollectionAjaxRenderer(/*"father", "child",*/ new String[] { "name", "username", "email" }, new String[] { "email", "username", "name"});
    	
    	System.out.println(render.encode(list, new Locale("en"), true));
    	
    }
}
