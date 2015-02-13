package org.futurepages.core.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.futurepages.core.exception.DefaultExceptionLogger;
import org.futurepages.exceptions.UserException;

public abstract class Validator {

	/** mapa de validações (chave, mensagem)*/
	protected LinkedHashMap<String, String> validationMap;

	/** validadores chamados por esse validador.*/
	private ArrayList<Validator> subValidators;

	protected Boolean breakOnFirst;

	public static <T extends Validator> T validate(Class<T> t, boolean breakOnFirst) {
		T validator;
		try {
			validator = t.newInstance();
			validator.setBreakOnFirst(breakOnFirst);
			return validator;
		} catch (Exception ex) {
			DefaultExceptionLogger.getInstance().execute(ex);
		}
		return null;
	}

	public Validator() {
		validationMap = new LinkedHashMap<String, String>();
		subValidators = new ArrayList<Validator>();
	}

	protected <T extends Validator> T validate(Class<T> t) {
		T validator = Validator.validate(t, breakOnFirst);
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
			UserException exce = new UserException(validationMap);
			throw exce;
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
}