package org.futurepages.util.templatizer.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author thiago
 */
// @TODO: add the Apps' map reading
@SuppressWarnings({ "serial", "unchecked" })
public class ContextTemplateTag implements Map<String, Object> {
	
	private static interface HashMapAcces {
		public Object get(List<HashMap<String, Object>> maps, String key);
	};
	
	private static class NormalHashMapAccess implements HashMapAcces {

		@Override
		public Object get(List<HashMap<String, Object>> maps, String key) {

			for (int i = maps.size() - 1; i >= 0; i--) {
				HashMap<String, Object> map = maps.get(i);
				Object val = map.get(key);

				if (val != null) {
					return val;
				} else if (map.containsKey(key)) {
					return val;
				}
			}

			return null;
		}
	}
	
	private static class ReflectionHashMapAccess implements HashMapAcces {
		private Method getEntry;
		
		public ReflectionHashMapAccess() throws NoSuchMethodException, SecurityException {
			getEntry = HashMap.class.getDeclaredMethod("getEntry", Object.class);
			getEntry.setAccessible(true);
		}

		@Override
		public Object get(List<HashMap<String, Object>> maps, String key) {
			for (int i = maps.size() - 1; i >= 0; i--) {
				try {
					HashMap<String, Object> map = maps.get(i);

					Map.Entry<String, Object> e = (Map.Entry<String, Object>)getEntry.invoke(map, key);

					if (e != null) {
						return e.getValue();
					}
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			}

			return null;
		}
	}
	
	static {
		try {
			hashMapAccess = new ReflectionHashMapAccess();
		} catch (Exception ex) {
			hashMapAccess = new NormalHashMapAccess();
		}
	}
	
	private static HashMapAcces hashMapAccess;
	private static int numRuntimeErrors = 0;
	private static final int maxRuntimeErrors = 5;
	private static final GetFromMap.RetrieveDataMap defaultRetrieverDataMap = new GetFromMap.NonRecuriveRetrieveDataMap();
	
	private static Object get(List<HashMap<String, Object>> maps, String key) {
		try {
			return hashMapAccess.get(maps, key);
		} catch (Exception ex) {
			HashMapAcces nha = new NormalHashMapAccess();

			if (numRuntimeErrors++ >= maxRuntimeErrors) {
				hashMapAccess = nha;
			}
			
			return nha.get(maps, key);
		}
	}
	
	private static int size(List<HashMap<String, Object>> maps) {
		return keySet(maps).size();
	}
	
	private static Set<String> keySet(List<HashMap<String, Object>> maps) {
		HashSet<String> keys = new HashSet<String>();

		for (HashMap<String, Object> map : maps) {
			keys.addAll(map.keySet());
		}

		return keys;
	}
	
	/*------------------------------------------------------------------------*/
	
	private MyStack<HashMap<String, Object>> contextStack;
	private int size = 0;
	private boolean mapChanged = false;
	private GetFromMap.RetrieveDataMap retrieverDataMap;
	//private MyStack<AbstractTemplateBlock> genealogy;
	
	public ContextTemplateTag() {
		contextStack = new MyStack<HashMap<String, Object>>();
		retrieverDataMap = defaultRetrieverDataMap;
		//genealogy = new MyStack<AbstractTemplateBlock>();
	}

	public ContextTemplateTag(GetFromMap.RetrieveDataMap retrieverDataMap) {
		contextStack = new MyStack<HashMap<String, Object>>();
		this.retrieverDataMap = retrieverDataMap;
		//genealogy = new MyStack<AbstractTemplateBlock>();
	}
	
	public ContextTemplateTag(Map<String, Object> params) {
		this();

		if (params instanceof HashMap) {
			contextStack.push((HashMap<String, Object>)params);
		} else {
			contextStack.push(new HashMap<String, Object>()).peek().putAll(params);
		}

		size = params.size();
		mapChanged = false;
	}

	public ContextTemplateTag(Map<String, Object> params, GetFromMap.RetrieveDataMap retrieverDataMap) {
		this(retrieverDataMap);

		if (params instanceof HashMap) {
			contextStack.push((HashMap<String, Object>)params);
		} else {
			contextStack.push(new HashMap<String, Object>()).peek().putAll(params);
		}

		size = params.size();
		mapChanged = false;
	}
	
	public ContextTemplateTag createNewContext(Map<String, Object> params) {
		if (params instanceof HashMap) {
			contextStack.push((HashMap<String, Object>)params);
		} else {
			contextStack.push(new HashMap<String, Object>()).peek().putAll(params);
		}

		size = size(contextStack.getList());
		mapChanged = false;
		
		return this;
	}

	public ContextTemplateTag createNewContext() {
		contextStack.push(new HashMap<String, Object>());
		
		return this;
	}
	
	public HashMap<String, Object> popContext() {
		HashMap<String, Object> top = contextStack.pop();
		mapChanged = true;

		return top;
	}

	@Override
	public int size() {
		if (mapChanged) {
			size = size(contextStack.getList());
			mapChanged = false;
		}

		return size;
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public boolean containsKey(Object key) {
		List<HashMap<String, Object>> maps = contextStack.getList();

		for (int i = maps.size() - 1; i >= 0; i--) {
			HashMap<String, Object> map = maps.get(i);
			if (map.containsKey(key)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		List<HashMap<String, Object>> maps = contextStack.getList();

		for (int i = maps.size() - 1; i >= 0; i--) {
			HashMap<String, Object> map = maps.get(i);
			if (map.containsValue(value)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public Object get(Object key) {
		return get(contextStack.getList(), (String)key);
	}

	@Override
	public Object put(String key, Object value) {
		Object obj = contextStack.peek().put(key, value);

		if (obj == null) {
			mapChanged = true;
		}

		return obj;
	}

	/*
	 * @TODO: O que fazer? Remover somente do contexto mais recente ou de todos os contextos?
	 */
	@Deprecated
	@Override
	public Object remove(Object key) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> m) {
		contextStack.peek().putAll(m);
		mapChanged = true;
	}

	@Override
	public void clear() {
		contextStack.getList().clear();
		size = 0;
		mapChanged = false;
	}

	@Override
	public Set<String> keySet() {
		return keySet(contextStack.getList());
	}

	@Override
	public Collection<Object> values() {
		Set<String> keys = keySet(contextStack.getList());
		ArrayList<Object> objs = new ArrayList<Object>();

		for (String key : keys) {
			objs.add(get(contextStack.getList(), key));
		}

		return objs;
	}

	@Override
	public Set<Map.Entry<String, Object>> entrySet() {
		List<HashMap<String, Object>> maps = contextStack.getList();
		HashMap<String, Object> plainMap = new HashMap<String, Object>();
		
		for (HashMap<String, Object> map : maps) {
			plainMap.putAll(map);
		}

		return plainMap.entrySet();
	}
	
	public Object getValue(String key) {
		return retrieverDataMap.getValue(key, this);
	}
}
