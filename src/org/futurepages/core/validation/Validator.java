package org.futurepages.core.validation;

import org.futurepages.core.exception.AppLogger;
import org.futurepages.core.services.EntityServices;
import org.futurepages.exceptions.UserException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class Validator<SERVICES extends EntityServices>  {

	/** mapa de validações (chave, mensagem)*/
	protected LinkedHashMap<String, String> validationMap = new LinkedHashMap<String, String>();

	/** validadores chamados por esse validador.*/
	private ArrayList<Validator> subValidators = new ArrayList<>();

	protected Boolean breakOnFirst;

	protected SERVICES services;

	private static <T extends Validator,SERVICES extends EntityServices> T validate(Class<T> t, SERVICES services, boolean breakOnFirst) {
		T validator;
		try {
			validator = t.newInstance();
			validator.services = services;
			validator.setBreakOnFirst(breakOnFirst);
			return validator;
		} catch (Exception ex) {
			AppLogger.getInstance().execute(ex);
		}
		return null;
	}

	public static <T extends Validator, SERVICES extends EntityServices> void validate(SERVICES services, Class<T> clss, Executor<T> executor){
		T validator = validate(clss,services,false);
		executor.execute(validator);
		validator.validate();
	}


	protected <T extends Validator> T validate(Class<T> t) {
		T validator = Validator.validate(t,null, breakOnFirst);
		subValidators.add(validator);
		return validator;
	}

	public void error(String key, String msg) {
		putError(key, msg);
	}

	public void error(String key, UserException ex) {
		putError(key, ex.getMessage());
	}

	public void error(String msg) {
		error(null, msg);
	}

	public void error(Exception ex) {
		error(null, ex.getMessage());
	}


	private void putError(String key, String message) {
		if (key == null) {
			key = String.valueOf(validationMap.size()+1);
		}
		validationMap.put(key, message);

		if (breakOnFirst) {
			throw new UserException(validationMap);
		}
	}

	public void setBreakOnFirst(boolean breakOnFirst) {
		this.breakOnFirst = breakOnFirst;
	}

	public void validate(){
		validationMap();
	}

	private HashMap<String, String> validationMap() {
		validationMapAux();

		if ((breakOnFirst != null) && (!validationMap.isEmpty())) {
			throw new UserException(validationMap);
		}
		return validationMap;
	}
	
	private HashMap<String, String> validationMapAux() {
		for (Validator v : subValidators) {
			HashMap<String, String> temp = v.validationMapAux();
			
			Iterator it = temp.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> entry = (Map.Entry<String, String>)it.next();
				validationMap.put(entry.getKey(), entry.getValue());
			}

		}
		
		return validationMap;
	}

	public interface Executor<V extends Validator> {

		public abstract void execute(V validator);
	}
}