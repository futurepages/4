package org.futurepages.filters;

import java.io.Serializable;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.futurepages.core.filter.Filter;
import org.futurepages.core.input.Input;
import org.futurepages.core.control.InvocationChain;

import org.futurepages.core.persistence.Dao;
import org.futurepages.core.persistence.GenericDao;
import org.futurepages.util.ReflectionUtil;
import org.futurepages.util.The;

/**
 * PIFilter - Persistent Injection Filter
 * Recupera no banco um objeto do tipo 'classToInject' com o id passado no input com a chave 'keyToInject'
 * em seguida injeta no objeto que está referenciado no input com a chave 'targetKey'
 * @author leandro
 * Deprecated Classe. Use {@link PersistenceInjectionFilter}
 */
public class PIFilter implements Filter {

	private Class classToInject;   //classe do objeto a ser injetado
	private String targetKey;   //chave do input do objeto alvo - que sofrerá a injeção
	private String keyToInject;  //chave do input que será injetado no alvo
	private GenericDao dao;

	public PIFilter(String targetKey, Class classToInject) {
		this.targetKey = targetKey;
		this.classToInject = classToInject;
		this.keyToInject = The.uncapitalizedWord(classToInject.getSimpleName());
		this.dao = Dao.getInstance();
	}

	public PIFilter(String targetKey, String keyToInject, Class classToInject) {
		this.targetKey = targetKey;
		this.classToInject = classToInject;
		this.keyToInject = keyToInject;
		this.dao = Dao.getInstance();
	}

	public PIFilter(String targetKey, String keyToInject, Class classToInject, String schemaId) {
		this(targetKey, keyToInject, classToInject);
		this.dao = Dao.getInstance(schemaId);
	}

	public PIFilter(String targetKey, Class classToInject, String schemaId) {
		this(targetKey, classToInject);
		this.dao = Dao.getInstance(schemaId);
	}

	@Override
	public String filter(InvocationChain chain) throws Exception {
		Input input = chain.getAction().getInput();

		Serializable objectToInject = null;
		Object keyToInjectFound = input.getValue(keyToInject);
		boolean keyFound = keyToInjectFound != null;
		if (keyFound) {
			Class pkType = this.dao.getIdType(classToInject);
			if (pkType == StringType.class) {
				objectToInject = this.dao.get(classToInject, input.getStringValue(keyToInject));
			} else if (pkType == LongType.class) {
				final long idLong = input.getLongValue(keyToInject);
				objectToInject = this.dao.get(classToInject, idLong);
			} else if (pkType == IntegerType.class) {
				objectToInject = this.dao.get(classToInject, input.getIntValue(keyToInject));
			}
		}
		inject(input, objectToInject);
		return chain.invoke();
	}

	/**
	 * Se o alvo é targetKey é não nulo então:
	 * <li> se o objeto já existe no input, injeta-se o valor encontrato em tal objeto:setField( obj, targetObjetc)
	 * <li> se o objeto ainda não existe no input: input.setValue(targetKey, obj)
	 * 	
	 * @param input
	 * @param objToInject
	 */
	private void inject(Input input, Serializable objToInject) {
		String[] explodedTarget = The.explodedToArray(targetKey, ".");
		Object targetObject = input.getValue(explodedTarget[0]);
		if (targetObject != null) {
			setField(objToInject, explodedTarget, targetObject);
		} else {
			input.setValue(targetKey, objToInject);
		}
	}

	private void setField(Serializable obj, String[] explodedTarget, Object targetObject) {
		if (explodedTarget.length > 1) {
			for (int i = 1; i < explodedTarget.length; i++) {
				targetObject = ReflectionUtil.getField(targetObject, explodedTarget[i]);
				if(targetObject==null){
					return;
				}
			}
		}
		ReflectionUtil.setField(targetObject, keyToInject, obj);
	}

	@Override
	public void destroy() {
	}
}
