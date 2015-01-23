package org.futurepages.core.cache;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

public abstract class AbstractCache implements Cache, Serializable {
    
    protected static final float LOAD = 0.75F;
    
    protected String name;
    protected Map<Object, Object> map;
    protected int capacity;
    protected transient volatile int misses = 0;
    protected transient volatile int hits = 0;
    protected transient Date startDate = new Date();

    public AbstractCache(String name, int capacity) {
        this(name, capacity, LOAD);
    }

    public AbstractCache(String name, int capacity, float load) {
        this.capacity = capacity;
        int initialCapacity = (int) Math.ceil(capacity / load) + 1;
        map = createMap(initialCapacity, load);
    }
    
    protected abstract Map<Object, Object> createMap(int initialCapacity, float load);
    
    public synchronized Object remove(Object key) {
        return map.remove(key);
    }

    public synchronized Object get(Object key) {
        Object obj = map.get(key);
        if (obj == null) misses++;
        else hits++;
        return obj;
    }

    public synchronized Object put(Object key, Object value) {
        return map.put(key, value); 
    }
    
    public synchronized void clear() {
        map.clear();
    }

    public synchronized int getSize() {
        return map.size();
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer(250);
        sb.append("NAME=").append(name).append("\n");
        sb.append("STARTDATE=").append(startDate).append("\n");
        sb.append("CAPACITY=").append(capacity).append("\n");
        sb.append("SIZE=").append(getSize()).append("\n");
        int misses = this.misses;
        int hits = this.hits;
        int total = misses + hits;
        sb.append("MISSES=").append(misses).append("\n");
        sb.append("HITS=").append(hits).append("\n");
        sb.append("TOTAL=").append(total).append("\n");
        
        if (getSize() < 25) { // for debugging...
            sb.append("ELEMENTS: ");
            synchronized(this) {
                Iterator<Object> iter = map.values().iterator();
                while(iter.hasNext()) {
                    Object value = iter.next();
                    sb.append("[").append(value).append("]");
                }
            }
        }
        sb.append("\n");
        return sb.toString();
    }
}
        