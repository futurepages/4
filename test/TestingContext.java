package org.futurepages.test;

import org.futurepages.core.persistence.Dao;
import org.futurepages.menta.core.i18n.LocaleManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TestingContext {

	private static TestingContext INSTANCE;

	private static List<Integer> pilhaDeSuites = new ArrayList<>();

	static TestingContext getInstance(){
		return INSTANCE;
	}

	static void init() {
		if(INSTANCE ==null){
			Locale.setDefault(LocaleManager.getDefaultLocale());
			INSTANCE = new TestingContext();
		}
		pilhaDeSuites.add(1);
	}

	static void finish(){
		if(pilhaDeSuites.size()>0) {
			pilhaDeSuites.remove(pilhaDeSuites.size()-1);
		}
		if(pilhaDeSuites.size()==0){
			TestingContext.close();
			INSTANCE = null;
		}
	}

	static void close() {
		DriverFactory.quitDrivers();
		Dao.getInstance().close();
	}
}