package org.futurepages.caches;

import org.futurepages.core.cache.Cache;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import org.futurepages.core.exception.DefaultExceptionLogger;

import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.MessageListener;
import org.jgroups.blocks.PullPushAdapter;
import org.jgroups.util.Util;

public class DistributedCache implements Cache, MessageListener {

    public static final String JGROUPS_PROTOCOL_STACK = "UDP:" +
	                                                    "PING:" +
	                                                    "FD(timeout=5000):" +
	                                                    "STABLE:" +
	                                                    "VERIFY_SUSPECT(timeout=1500):" +
	                                                    "MERGE2:" +
	                                                    "NAKACK:" +
	                                                    "UNICAST(timeout=5000):" +
	                                                    "FRAG:" +
	                                                    "FLUSH:" +
	                                                    "GMS:" +
                                                        "VIEW_ENFORCER:" +
                                                        "STATE_TRANSFER:" +
	                                                    "QUEUE";

    private Cache cache;
    private JChannel channel;
    private PullPushAdapter adapter;
    private String groupname;

    public DistributedCache(String name, String groupname, int capacity, Class cacheImpl, String jgroups_protocol_stack) {
        try {
            Constructor c = cacheImpl.getConstructor(new Class[] { String.class, int.class });
            this.cache = (Cache) c.newInstance(new Object[] { name, new Integer(capacity) });
            initChannel(groupname, jgroups_protocol_stack);
        } catch(Exception e) {
            throw new RuntimeException("Error creating synchronized cache!", e);
        }
    }

    public DistributedCache(String name, String groupname, int capacity) {
       this(name, groupname, capacity, LRUCache.class);
    }

    public DistributedCache(String name, String groupname, int capacity, Class cacheImpl) {
        this(name, groupname, capacity, cacheImpl, JGROUPS_PROTOCOL_STACK);
    }

    public DistributedCache(String name, String groupname, int capacity, float load, Class cacheImpl, String jgroups_protocol_stack) {
        try {
            Constructor c = cacheImpl.getConstructor(new Class[] { String.class, int.class, float.class });
            this.cache = (Cache) c.newInstance(new Object[] { name, new Integer(capacity), new Float(load) });
            initChannel(groupname, jgroups_protocol_stack);
        } catch(Exception e) {
            throw new RuntimeException("Error creating synchronized cache!", e);
        }
    }

    public DistributedCache(String name, String groupname, int capacity, float load, Class cacheImpl) {
        this(name, groupname, capacity, load, cacheImpl, JGROUPS_PROTOCOL_STACK);
    }

    private void initChannel(String groupname, String stack) throws Exception {
        this.groupname = groupname;
        this.channel = new JChannel(stack);
        channel.setOpt(JChannel.GET_STATE_EVENTS, Boolean.TRUE);
        channel.connect(groupname);
        this.adapter = new PullPushAdapter(channel, this);
        channel.getState(null, 0);
    }

    public synchronized Object get(Object key) {
        return cache.get(key);
    }

    public synchronized Object put(Object key, Object value) {
        return put(key, value, true);
    }

    protected synchronized Object put(Object key, Object value, boolean send) {
        Object obj = cache.put(key, value);
        if (send) {
            try {
                DistributedCacheMessage msg = new DistributedCacheMessage(DistributedCacheMessage.PUT, (java.io.Serializable) key, (java.io.Serializable) value);
                channel.send(null, null, msg);
            } catch(Exception e) {
                DefaultExceptionLogger.getInstance().execute(e);
            }
        }
        return obj;
    }

    public synchronized Object remove(Object key) {
        return remove(key, true);
    }

    protected synchronized Object remove(Object key, boolean send) {
        Object obj = cache.remove(key);
        if (send) {
            try {
                DistributedCacheMessage msg = new DistributedCacheMessage(DistributedCacheMessage.REMOVE, (java.io.Serializable) key);
                channel.send(null, null, msg);
            } catch(Exception e) {
                DefaultExceptionLogger.getInstance().execute(e);
            }
        }
        return obj;
    }

    public synchronized void clear() {
        clear(true);
    }

    protected synchronized void clear(boolean send) {
        cache.clear();
        if (send) {
            try {
                DistributedCacheMessage msg = new DistributedCacheMessage(DistributedCacheMessage.CLEAR);
                channel.send(null, null, msg);
            } catch(Exception e) {
                DefaultExceptionLogger.getInstance().execute(e);
            }
        }
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(1000);
        sb.append("ReplicatedCache: GroupName=").append(groupname).append("\n");
        sb.append(cache.toString());
        return sb.toString();
    }

    public synchronized void receive(Message msg) {
        try {
            Object obj = Util.objectFromByteBuffer(msg.getBuffer());
            if (obj instanceof DistributedCacheMessage) {
                DistributedCacheMessage message = (DistributedCacheMessage) obj;
                if (message.getType() == DistributedCacheMessage.PUT) {
                    put(message.getKey(), message.getValue(), false);
                } else if (message.getType() == DistributedCacheMessage.REMOVE) {
                    remove(message.getKey(), false);
                } else if (message.getType() == DistributedCacheMessage.CLEAR) {
                    clear(false);
                }
            }
        } catch(Exception e) {
            DefaultExceptionLogger.getInstance().execute(e);
        }
    }

    public synchronized void setState(byte [] state) {
        try {
            this.cache = (Cache) Util.objectFromByteBuffer(state);
        } catch(Exception e) {
            System.err.println("Error setting jgroups state!");
            DefaultExceptionLogger.getInstance().execute(e);
        }
    }

    public synchronized byte [] getState() {
        try {
            return Util.objectToByteBuffer(cache);
        } catch(Exception e) {
            System.err.println("Error getting jgroups state!");
            DefaultExceptionLogger.getInstance().execute(e);
            return null;
        }
    }
}

class DistributedCacheMessage implements Serializable {

    public static final int PUT = 1;
    public static final int REMOVE = 2;
    public static final int CLEAR = 3;

    private int type;
    private Serializable key = null;
    private Serializable value = null;

    public DistributedCacheMessage(int type) {
        this.type = type;
    }

    public DistributedCacheMessage(int type, Serializable key, Serializable value) {
        this.type = type;
        this.key = key;
        this.value = value;
    }

    public DistributedCacheMessage(int type, Serializable key) {
        this.type = type;
        this.key = key;
    }

    public int getType() { return type; }
    public Serializable getKey() { return key; }
    public Serializable getValue() { return value; }
}
