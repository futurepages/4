package org.futurepages.util.templatizer.util;

import java.util.Map;
import org.futurepages.core.config.Apps;
import org.futurepages.util.Is;
import org.futurepages.util.ReflectionUtil;

/**
 *
 * @author thiago
 */
public class GetFromMap {

	public interface RetrieveDataMap {
		public Object getValue(String key, Map<String, Object> mapValues);
	};
	
	public static class RecursiveRetrieveDataMap implements RetrieveDataMap {
		// @TODO: Não está sendo utilizado, fazer com que seja.
		//Entrada é ${VAR_PATH}, saída é o valor encontrado. Vazio é retornado se nada for encontrado.
		public String getParamValue(String matchedVar, Map<String,Object> mapValues){
			String key = matchedVar.substring(2, matchedVar.length()-1);
			Object value = getValue(key, mapValues);
			return (value==null? (Apps.get(key)!=null ? Apps.get(key) : "") : value.toString());
		}

		@Override
		//Entrada é VAR_PATH, saída é o valor encontrado. Vazio é retornado se nada for encontrado.
		public Object getValue(String key, Map<String,Object> mapValues) {
			return getValue(key, "", mapValues);
		}

		/**
		 * Busca recursivamente o atributo
		 * a.b.c.d por exemplo...
		 * procura para ver se tem no mapa a.b.c.d, se tiver retorna, senão
		 * procura a.b.c no mapa, se tiver, retorna obj.getD(), senão...
		 * procura a.b no mapa , se tiver, retorna obj.getC().getD(), senão ...
		 * procura a no mapa , se tiver, retorna obj.getB().getC().getD(), senão ...
		 * senão retorna Texto VAZIO.
		 */
		public static Object getValue(String key, String fieldPath,  Map<String,Object> mapValues) {
			Object value = null;
			if(mapValues.get(key) != null){
				if(!Is.empty(fieldPath)){
					value = ReflectionUtil.getField(mapValues.get(key),fieldPath);
				}else{
					value = mapValues.get(key);
				}
			}else{
				if (key.contains(".")) {
					int lastPoint = key.lastIndexOf(".");
					value = getValue(key.substring(0,lastPoint) , key.substring(lastPoint+1,key.length())+(!Is.empty(fieldPath)?"."+fieldPath:"")  , mapValues);
				}
			}
			return value;
		}
	};

	public static class NonRecuriveRetrieveDataMap implements RetrieveDataMap {

		@Override
		public Object getValue(String key, Map<String, Object> mapValues) {
			int nextIdx = key.indexOf('.');
			
			if (nextIdx > 0) {
				int prevIdx = 0;
				Object obj = mapValues.get(key.substring(prevIdx, nextIdx));
				
				if (obj != null) {
					while (true) {
						prevIdx = nextIdx + 1;
						nextIdx = key.indexOf('.', prevIdx);

						obj = ReflectionUtil.getField(obj, key.substring(prevIdx, nextIdx > 0 ? nextIdx : key.length()));

						if (obj == null || nextIdx <= -1) {
							break;
						}
					}
				}

				return obj;
			} else if (nextIdx <= -1 && key != null && !key.isEmpty()) {
				return mapValues.get(key);
			} else {
				return null;
			}
		}		
	}
	
	public static class FasterRetrieveDataMap implements RetrieveDataMap {
		private NonRecuriveRetrieveDataMap retriever = new NonRecuriveRetrieveDataMap();
		
		@Override
		public Object getValue(String key, Map<String, Object> mapValues) {
			return retriever.getValue(key, mapValues);
		}

		public Object getValue(String []keys, Map<String, Object> mapValues) {
			int len = keys.length;
			
			if (len > 0) {
				Object obj = mapValues.get(keys[0]);
				
				if (obj != null) {
					for (int i = 1; i < len; i++) {
						obj = ReflectionUtil.getField(obj, keys[i]);

						if (obj == null) {
							break;
						}
					}
				}

				return obj;
			} else {
				return null;
			}
		}
	}
}
