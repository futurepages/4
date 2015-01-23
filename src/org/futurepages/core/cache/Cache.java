package org.futurepages.core.cache;


public interface Cache {

    public Object get(Object key);
    public Object put(Object key, Object value);
    public Object remove(Object key);
    public void clear();

}

        