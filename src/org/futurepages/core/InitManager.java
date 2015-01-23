package org.futurepages.core;

import org.futurepages.consequences.NullConsequence;
import org.futurepages.consequences.StringConsequence;
import org.futurepages.core.config.Params;
import org.futurepages.core.context.Context;
import org.futurepages.core.control.AbstractApplicationManager;
import org.futurepages.core.exception.DefaultExceptionLogger;
import org.futurepages.core.formatter.FormatterManager;
import org.futurepages.core.i18n.LocaleManager;
import org.futurepages.core.persistence.HibernateFilter;
import org.futurepages.core.persistence.HibernateManager;
import org.futurepages.filters.*;
import org.futurepages.formatters.*;
import org.futurepages.json.JSONGenericRenderer;
import org.futurepages.util.Is;

import java.util.Locale;

/**
 * ApplicationManager que gerencia a Ação Inicial e os filtros Globais
 * Carrega a Action Inicial da Aplicação
 */
public class InitManager extends AbstractApplicationManager{
    
    @Override
    public final void loadActions() {
            //Filtros Globais
			if(!Is.empty(Params.get("AUTO_REDIRECT_DOMAIN"))){
				filter(new AutoRedirectDomainFilter(Params.get("AUTO_REDIRECT_DOMAIN")));
			}

			if(HibernateManager.isRunning()){
                filter(new HibernateFilter());
            }else{
                filter(new ExceptionFilter());
			}
            
            if(Params.get("GLOBAL_HEAD_TITLE")!=null){
				if(!Is.empty(Params.get("PRETTY_HEAD_TITLE"))){
					HeadTitleFilter.itsPretty(Params.get("PRETTY_HEAD_TITLE"));
				}
				filter(new HeadTitleFilter());
            }

            filter(new FileUploadFilter());
            filter(new InjectionFilter());

            on(NULL, new NullConsequence());
			on(EXCEPTION, fwd(Params.get("EXCEPTION_FILE_PATH")));
			on(DYN_EXCEPTION, fwd(Params.get("DYN_EXCEPTION_FILE_PATH")));
			on(STRING, new StringConsequence() );
			on(REDIR, redir());
			on(AJAX_REDIR,   ajax(new JSONGenericRenderer()));
			on(AJAX_ERROR,   ajax(new JSONGenericRenderer()));
			on(AJAX_SUCCESS, ajax(new JSONGenericRenderer()));
			on(REDIR_APPEND_OUTPUT, redir(true));

            //Ação Inicial Padrão
            try {
                Class initActionClass = Class.forName(Params.get("INIT_ACTION"));
                action(Params.get("START_PAGE_NAME"), initActionClass).on(SUCCESS, fwd(Params.get("START_CONSEQUENCE")));
            } catch (ClassNotFoundException ex) {
                System.out.println("[::initManager::] A classe de Ação Inicial da Aplicação não foi encontrada.");
                DefaultExceptionLogger.getInstance().execute(ex);
            }
    }
    
    @Override
    public void loadLocales(){
         //Por enquanto o futurepages não contempla internacionalização.
         LocaleManager.add(new Locale("pt","BR"));
    }
    
    @Override
    public void loadFormatters() {
        FormatterManager.addFormatter("anniversary"        , new AnniversaryFormatter());
        FormatterManager.addFormatter("anniversaryAbbr"    , new AnniversaryAbbrFormatter());
        FormatterManager.addFormatter("bigLong" 	       , new BigLongFormatter());
        FormatterManager.addFormatter("collectionSize" 	   , new CollectionSizeFormatter());
		FormatterManager.addFormatter("collapsedText"      , new CollapsedTextFormatter());
        FormatterManager.addFormatter("cpfCnpj"		 	   , new CPFCNPJFormatter());
        FormatterManager.addFormatter("date"         	   , new DateFormatter());
        FormatterManager.addFormatter("dateTime"     	   , new DateTimeFormatter());
        FormatterManager.addFormatter("elapsedTime"   	   , new ElapsedTimeFormatter());
        FormatterManager.addFormatter("extensionFileName"  , new ExtensionFileNameFormatter());
        FormatterManager.addFormatter("float"        	   , new FloatFormatter());
        FormatterManager.addFormatter("fullDate"		   , new FullDateTimeFormatter(false));
        FormatterManager.addFormatter("fullDateTime"	   , new FullDateTimeFormatter(true));
        FormatterManager.addFormatter("fullDateLiteral"	   , new FullDateTimeFormatter(false,true));
        FormatterManager.addFormatter("html"        	   , new HTMLFormatter());
        FormatterManager.addFormatter("javascript"         , new JavascriptFormatter());
        FormatterManager.addFormatter("literalDate" 	   , new LiteralDateFormatter());
        FormatterManager.addFormatter("literalDateTime"	   , new LiteralDateTimeFormatter());
        FormatterManager.addFormatter("literalDayOfWeek"   , new LiteralDayOfWeekFormatter());
        FormatterManager.addFormatter("literalAnniversary" , new LiteralAnniversaryFormatter());
        FormatterManager.addFormatter("money"              , new MoneyFormatter());
        FormatterManager.addFormatter("month"              , new MonthFormatter());
        FormatterManager.addFormatter("noSpecials"  	   , new NoSpecialsFormatter());
        FormatterManager.addFormatter("poorText"		   , new PoorTextFormatter());
        FormatterManager.addFormatter("remainingTime"	   , new RemainingTimeFormatter());
        FormatterManager.addFormatter("seoURL"	     	   , new SEOURLFormatter());
        FormatterManager.addFormatter("simpleElapsedTime"  , new SimpleElapsedTimeFormatter());
        FormatterManager.addFormatter("smartText"		   , new SmartTextFormatter());
        FormatterManager.addFormatter("textarea"		   , new TextAreaFormatter());
        FormatterManager.addFormatter("time"			   , new TimeFormatter());
        FormatterManager.addFormatter("uppercase"		   , new UpperCaseFormatter());
		FormatterManager.addFormatter("url"                , new UrlFormatter());
    }

	@Override
	public void init(Context application) {
		application.setAttribute("params", Params.getParamsMap());
	}
}