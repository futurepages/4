package org.futurepages.menta.core;

import org.futurepages.core.config.Apps;
import org.futurepages.core.exception.AppLogger;
import org.futurepages.formatters.DBDateFormatter;
import org.futurepages.formatters.brazil.AnniversaryAbbrFormatter;
import org.futurepages.formatters.brazil.AnniversaryFormatter;
import org.futurepages.formatters.brazil.CPFCNPJFormatter;
import org.futurepages.formatters.brazil.DateFormatter;
import org.futurepages.formatters.brazil.DateTimeFormatter;
import org.futurepages.formatters.brazil.ElapsedTimeFormatter;
import org.futurepages.formatters.brazil.FullDateTimeFormatter;
import org.futurepages.formatters.brazil.LiteralAnniversaryFormatter;
import org.futurepages.formatters.brazil.LiteralDateFormatter;
import org.futurepages.formatters.brazil.LiteralDateTimeFormatter;
import org.futurepages.formatters.brazil.LiteralDayOfWeekFormatter;
import org.futurepages.formatters.brazil.MoneyFormatter;
import org.futurepages.formatters.brazil.MonthFormatter;
import org.futurepages.formatters.brazil.RemainingTimeFormatter;
import org.futurepages.formatters.brazil.SimpleElapsedTimeFormatter;
import org.futurepages.formatters.brazil.SimpleLiteralDateFormatter;
import org.futurepages.formatters.brazil.SmartTextFormatter;
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
import org.futurepages.core.persistence.HibernateManager;
import org.futurepages.formatters.BigLongFormatter;
import org.futurepages.formatters.CollapsedTextFormatter;
import org.futurepages.formatters.CollectionSizeFormatter;
import org.futurepages.formatters.ExtensionFileNameFormatter;
import org.futurepages.formatters.FloatFormatter;
import org.futurepages.formatters.HTMLFormatter;
import org.futurepages.formatters.JavascriptFormatter;
import org.futurepages.formatters.NoSpecialsFormatter;
import org.futurepages.formatters.PoorTextFormatter;
import org.futurepages.formatters.SEOURLFormatter;
import org.futurepages.formatters.TextAreaFormatter;
import org.futurepages.formatters.TimeFormatter;
import org.futurepages.formatters.UpperCaseFormatter;
import org.futurepages.formatters.UrlFormatter;
import org.futurepages.menta.filters.InjectionFilter;
import org.futurepages.menta.json.JSONGenericRenderer;
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
			if(!Is.empty(Apps.get("AUTO_REDIRECT_DOMAIN"))){
				filter(new AutoRedirectDomainFilter(Apps.get("AUTO_REDIRECT_DOMAIN")));
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
         LocaleManager.add(new Locale("pt","BR"));
    }
    
    @Override
    public void loadFormatters() {
        FormatterManager.addFormatter("anniversary", new AnniversaryFormatter());
        FormatterManager.addFormatter("anniversaryAbbr", new AnniversaryAbbrFormatter());
        FormatterManager.addFormatter("bigLong", new BigLongFormatter());
        FormatterManager.addFormatter("collectionSize", new CollectionSizeFormatter());
		FormatterManager.addFormatter("collapsedText", new CollapsedTextFormatter());
        FormatterManager.addFormatter("cpfCnpj", new CPFCNPJFormatter());
        FormatterManager.addFormatter("date", new DateFormatter());
        FormatterManager.addFormatter("dbDate", new DBDateFormatter());
        FormatterManager.addFormatter("dateTime", new DateTimeFormatter());
        FormatterManager.addFormatter("elapsedTime", new ElapsedTimeFormatter());
        FormatterManager.addFormatter("extensionFileName", new ExtensionFileNameFormatter());
        FormatterManager.addFormatter("float", new FloatFormatter());
        FormatterManager.addFormatter("fullDate", new FullDateTimeFormatter(false));
        FormatterManager.addFormatter("fullDateTime", new FullDateTimeFormatter(true));
        FormatterManager.addFormatter("fullDateLiteral", new FullDateTimeFormatter(false, true));
        FormatterManager.addFormatter("html", new HTMLFormatter());
        FormatterManager.addFormatter("javascript", new JavascriptFormatter());
        FormatterManager.addFormatter("literalDate", new LiteralDateFormatter());
        FormatterManager.addFormatter("literalDateTime", new LiteralDateTimeFormatter());
        FormatterManager.addFormatter("literalDayOfWeek", new LiteralDayOfWeekFormatter());
        FormatterManager.addFormatter("literalAnniversary", new LiteralAnniversaryFormatter());
        FormatterManager.addFormatter("money", new MoneyFormatter());
        FormatterManager.addFormatter("month", new MonthFormatter());
        FormatterManager.addFormatter("noSpecials", new NoSpecialsFormatter());
        FormatterManager.addFormatter("poorText", new PoorTextFormatter());
        FormatterManager.addFormatter("remainingTime", new RemainingTimeFormatter());
        FormatterManager.addFormatter("seoURL", new SEOURLFormatter());
        FormatterManager.addFormatter("simpleElapsedTime", new SimpleElapsedTimeFormatter());
        FormatterManager.addFormatter("simpleLiteralDate", new SimpleLiteralDateFormatter());
        FormatterManager.addFormatter("smartText", new SmartTextFormatter());
        FormatterManager.addFormatter("textarea", new TextAreaFormatter());
        FormatterManager.addFormatter("time", new TimeFormatter());
        FormatterManager.addFormatter("uppercase", new UpperCaseFormatter());
		FormatterManager.addFormatter("url", new UrlFormatter());
    }

	@Override
	public void init(Context application) {
		application.setAttribute("params", Apps.getInstance().getParamsMap());
	}
}