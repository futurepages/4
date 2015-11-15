package org.futurepages.core.locale;

import org.futurepages.core.config.Apps;
import org.futurepages.core.control.UI;
import org.futurepages.exceptions.AppsPropertiesException;
import org.futurepages.menta.core.i18n.LocaleManager;

import java.util.Collection;
import java.util.Locale;

public class NewLocaleManager {

	private static NewLocaleManager INSTANCE;

	private NewLocaleManager(){}

	public static Locale getDefaultLocale() {
// newLocaleManager and Txt not implemented yet.
//		return getInstance().getDesiredLocale(null);
		return LocaleManager.getDefaultLocale(); //TODO menta for a while.
	}

	public static NewLocaleManager getInstance(){
		if(INSTANCE==null){
			INSTANCE = new NewLocaleManager();
		}
		return INSTANCE;
	}

	//TODO descomentar a seguir quando for instalar Txt e internacionalização...

//	public Locale getDesiredLocale(Locale requestLocale) {
//		if(requestLocale==null && UI.getCurrent()!=null){
//			requestLocale = UI.getCurrent().getLocale();
//		}
//
//		if(requestLocale!=null && getAvailableLocales().contains(requestLocale.toString())){
//			return requestLocale;
//		}else{
//			Locale appsLocale = null;
//			try {
//				appsLocale = getAppsLocale();
//			} catch(AppsPropertiesException ignored){}
//			if(appsLocale!=null){
//				return appsLocale; // from futurepages.properties
//			}else{
//				return Locale.getDefault(); // System
//			}
//		}
//	}

//	public static Collection<String> getAvailableLocales() {
//		return Txt.getInstance().getAvailableLocales();
//	}
//
//	private Locale getAppsLocale() {
//		Locale appsLocale = null;
//		if(Apps.get("LOCALE")!=null){
//			String[] appsLocaleParts = Apps.get("LOCALE").split("_");
//			if(appsLocaleParts.length==1){
//				appsLocale = new Locale(appsLocaleParts[0]);
//			}else if(appsLocaleParts.length==2){
//				appsLocale = new Locale(appsLocaleParts[0],appsLocaleParts[1]);
//			}else if(appsLocaleParts.length==3){
//				appsLocale = new Locale(appsLocaleParts[0],appsLocaleParts[1],appsLocaleParts[2]); // TODO verificar, não é certeza se é assim mesmo. Se o terceiro parâmetro não deve ser tratado.
//			}else{
//				throw new RuntimeException("The defined locale isn't valid: "+Apps.get("LOCALE"));
//			}
//		}
//		return appsLocale;
//	}

}