package org.futurepages.menta.core;

import org.futurepages.core.config.Apps;
import org.futurepages.core.exception.AppLogger;
import org.futurepages.core.persistence.HibernateManager;
import org.futurepages.menta.consequences.NullConsequence;
import org.futurepages.menta.consequences.StringConsequence;
import org.futurepages.menta.core.context.Context;
import org.futurepages.menta.core.control.AbstractApplicationManager;
import org.futurepages.menta.core.formatter.FormatterManager;
import org.futurepages.menta.core.i18n.LocaleManager;
import org.futurepages.menta.filters.AutoRedirectDomainFilter;
import org.futurepages.menta.filters.ExceptionFilter;
import org.futurepages.menta.filters.FileUploadFilter;
import org.futurepages.menta.filters.HeadTitleFilter;
import org.futurepages.menta.filters.HibernateFilter;
import org.futurepages.menta.filters.InjectionFilter;
import org.futurepages.menta.json.JSONGenericRenderer;
import org.futurepages.util.Is;

/**
 * ApplicationManager que gerencia a Ação Inicial e os filtros Globais
 * Carrega a Action Inicial da Aplicação
 */
public class InitManager extends AbstractApplicationManager{
    
    @Override
    public final void loadActions() {
            //Filtros Globais
			if(!Is.empty(Apps.get("AUTO_REDIRECT_DOMAIN"))){
				filter(new AutoRedirectDomainFilter(Apps.get("AUTO_REDIRECT_DOMAIN"), Apps.get("ALTERNATIVE_DOMAINS")));
			}

			if(HibernateManager.isRunning()){
                filter(new HibernateFilter());
            }else{
                filter(new ExceptionFilter());
			}
            
            if(Apps.get("GLOBAL_HEAD_TITLE")!=null){
				if(!Is.empty(Apps.get("PRETTY_HEAD_TITLE"))){
					HeadTitleFilter.itsPretty(Apps.get("PRETTY_HEAD_TITLE"));
				}
				filter(new HeadTitleFilter());
            }

            filter(new FileUploadFilter());
            filter(new InjectionFilter());

            on(NULL, new NullConsequence());
			on(EXCEPTION, fwd(Apps.get("EXCEPTION_FILE_PATH")));
			on(NOT_FOUND, fwd("/exceptions/404.jsp"));
			on(DYN_EXCEPTION, fwd(Apps.get("DYN_EXCEPTION_FILE_PATH")));
			on(STRING, new StringConsequence() );
			on(REDIR, redir());
			on(AJAX_REDIR,   ajax(new JSONGenericRenderer()));
			on(AJAX_ERROR,   ajax(new JSONGenericRenderer()));
			on(AJAX_SUCCESS, ajax(new JSONGenericRenderer()));
			on(REDIR_APPEND_OUTPUT, redir(true));

            //Ação Inicial Padrão
            try {
                Class initActionClass = Class.forName(Apps.get("INIT_ACTION"));
                action(Apps.get("START_PAGE_NAME"), initActionClass).on(SUCCESS, fwd(Apps.get("START_CONSEQUENCE")));
            } catch (ClassNotFoundException ex) {
                System.out.println("[::initManager::] A classe de Ação Inicial da Aplicação não foi encontrada.");
                AppLogger.getInstance().execute(ex);
            }
    }
    
    @Override
    public void loadLocales(){
         //Por enquanto o futurepages não contempla internacionalização.
         LocaleManager.addDefault();
    }
    
    @Override
    public void loadFormatters() {
        FormatterManager.loadDefaults();
    }

	@Override
	public void init(Context application) {
    	application.setAttribute("params", Apps.getInstance().getParamsMap());
	}
}