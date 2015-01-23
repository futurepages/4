package org.futurepages.caches;

import org.futurepages.core.cache.AbstractCache;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TimeoutCache extends AbstractCache implements Runnable {
    
    private final long timeout;
    
    private Thread thread = null;
    private volatile boolean bThread = true;
    
    public TimeoutCache(String name, int capacity, long timeout) {
     
        super(name, capacity);
        
        this.timeout = timeout;
        
        this.thread = new Thread(this);
        this.thread.start();
    }
    
	@Override
    public void run() {
        
        while(bThread) {
            
            long now = System.currentTimeMillis();
            
            synchronized(this) {
                
                Iterator<Object> iter = map.values().iterator();
                
                while(iter.hasNext()) {
                    
                    Entry entry = (Entry) iter.next();
                    
                    if (entry.isExpired(now)) {
                        
                        iter.remove();
                        
                    }
                }
            }
            
            try {
                
                Thread.sleep(timeout);
                
            } catch(InterruptedException e) { }
        }
    }
    
    public void close() {
        
        bThread = false;
        
        thread.interrupt();
        
        thread = null;
    }
    
    protected Map<Object, Object> createMap(int initialCapacity, float load) {
     
        return new HashMap<Object, Object>(initialCapacity, load);
    }
    
	@Override
    public synchronized Object remove(Object key) {
        
        Entry entry = (Entry) super.remove(key);
        
        if (entry != null) {
            
            return entry.value;
            
        } else {
            
            return null;
        }
    }

    public synchronized Object get(Object key) {
        
        Entry entry = (Entry) super.get(key);
        
        if (entry != null) return entry.value;
        
        return null;
    }

	@Override
    public synchronized Object put(Object key, Object value) {
        
        if (map.size() == capacity && !map.containsKey(key)) {
            
            return null;
        }
        
        Entry entry = (Entry) map.put(key, new Entry(value));
        
        if (entry == null) return null;
        
        return entry.value;
    }
    
    private class Entry {
        
        long ts = System.currentTimeMillis();
        
        Object value = null;
        
        public Entry(Object value) {
            this.value = value;
        }
        
        public boolean isExpired(long now) {
            
             return now - ts >= timeout;
            
        }
    }
}