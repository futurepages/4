package org.futurepages.core.locale;

import com.vaadin.ui.UI;
import org.futurepages.core.config.Apps;
import org.futurepages.exceptions.AppsPropertiesException;

import java.util.Collection;
import java.util.Locale;

public class LocaleManager {

	private static LocaleManager INSTANCE;

	private LocaleManager(){}

	public static Locale getDefaultLocale() {
		return getInstance().getDesiredLocale(null);
	}

	public static LocaleManager getInstance(){
		if(INSTANCE==null){
			INSTANCE = new LocaleManager();
		}
		return INSTANCE;
	}

	public Locale getDesiredLocale(Locale requestLocale) {
		if(requestLocale==null && UI.getCurrent()!=null){
			requestLocale = UI.getCurrent().getLocale();
		}

		if(requestLocale!=null && getAvailableLocales().contains(requestLocale.toString())){
			return requestLocale;
		}else{
			Locale appsLocale = null;
			try {
				appsLocale = getAppsLocale();
			} catch(AppsPropertiesException ignored){}
			if(appsLocale!=null){
				return appsLocale; // from futurepages.properties
			}else{
				return Locale.getDefault(); // System
			}
		}
	}

	public static Collection<String> getAvailableLocales() {
		return Txt.getInstance().getAvailableLocales();
	}

	private Locale getAppsLocale() {
		Locale appsLocale = null;
		if(Apps.get("LOCALE")!=null){
			String[] appsLocaleParts = Apps.get("LOCALE").split("_");
			if(appsLocaleParts.length==1){
				appsLocale = new Locale(appsLocaleParts[0]);
			}else if(appsLocaleParts.length==2){
				appsLocale = new Locale(appsLocaleParts[0],appsLocaleParts[1]);
			}else if(appsLocaleParts.length==3){
				appsLocale = new Locale(appsLocaleParts[0],appsLocaleParts[1],appsLocaleParts[2]); // TODO verificar, não é certeza se é assim mesmo. Se o terceiro parâmetro não deve ser tratado.
			}else{
				throw new RuntimeException("The defined locale isn't valid: "+Apps.get("LOCALE"));
			}
		}
		return appsLocale;
	}

}