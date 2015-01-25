package modules.admin.model.core;

import java.util.HashMap;
import java.util.List;
import modules.admin.model.entities.Param;
import modules.admin.model.dao.ParamDao;


public class AdminParams<K, V> extends HashMap<String,Object> {

    private static final AdminParams instance = new AdminParams();

    private AdminParams() {
    }

    public static AdminParams getInstance() {
        return instance;
    }

    synchronized public void initCache() {

		List<Param> list = ParamDao.list();

        for (Param p : list) {
            this.put(p.getParamId(), p);
        }
    }

	public static Object get(String key){
		Param param = getInstance().getParam(key);
		if(param!=null){
			return param.getConvertedValue();
		}
		return null;
	}

	@Override
	public Object get(Object key){
		return getParam((String)key).getConvertedValue();
	}

	public static Integer getInt(Object key){
		Param param = getInstance().getParam((String)key);
		if(param!=null){
			return (Integer) param.getConvertedValue();
		}
		return null;
	}

	public static String getString(Object key){
		Param param = getInstance().getParam((String)key);
		if(param!=null){
			return (String) param.getConvertedValue();
		}
		return null;
	}

	public Param getParam(String paramId) {
        return (Param) super.get(paramId);
    }

    synchronized public void setOnParamId(String paramId, Param param) {
        put(paramId, param);
    }
}