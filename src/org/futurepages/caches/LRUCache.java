package org.futurepages.caches;

import org.futurepages.core.cache.AbstractCache;
import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache extends AbstractCache {
    
    public LRUCache(String name, int capacity) {
        super(name, capacity, LOAD);
    }

    public LRUCache(String name, int capacity, float load) {
        super(name, capacity, load);
    }
    
    protected Map<Object, Object> createMap(int initialCapacity, float load) {
        return new LinkedHashMap(initialCapacity, load, true) {
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > LRUCache.this.capacity;
            }
        };
    }
}
        