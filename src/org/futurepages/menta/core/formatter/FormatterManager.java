package org.futurepages.menta.core.formatter;

import org.futurepages.core.formatter.AbstractFormatter;
import org.futurepages.formatters.BigLongFormatter;
import org.futurepages.formatters.CapitalizeFormatter;
import org.futurepages.formatters.CollapsedTextFormatter;
import org.futurepages.formatters.CollectionSizeFormatter;
import org.futurepages.formatters.DBDateFormatter;
import org.futurepages.formatters.ExtensionFileNameFormatter;
import org.futurepages.formatters.FloatFormatter;
import org.futurepages.formatters.HTMLFormatter;
import org.futurepages.formatters.HTMLScriptFormatter;
import org.futurepages.formatters.JavascriptFormatter;
import org.futurepages.formatters.NoSpecialsFormatter;
import org.futurepages.formatters.PhoneFormatter;
import org.futurepages.formatters.PoorTextFormatter;
import org.futurepages.formatters.SEOURLFormatter;
import org.futurepages.formatters.TextAreaFormatter;
import org.futurepages.formatters.TimeFormatter;
import org.futurepages.formatters.UpperCaseFormatter;
import org.futurepages.formatters.UrlFormatter;
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

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sergio Oliveira
 */
public class FormatterManager {
    
    private static Map<String, AbstractFormatter> formatters = new HashMap<String, AbstractFormatter>();
    
    private static AbstractFormatter fixedDateFormatter = null;
    private static AbstractFormatter fixedTimeFormatter = null;
    
    public static void addFormatter(String name, AbstractFormatter f) {
        
        formatters.put(name, f);
    }
    
    public static AbstractFormatter getFormatter(String name) {
        
        return formatters.get(name);
        
    }
    
    public static void setFixedDateFormatter(AbstractFormatter f) {
    	
    	fixedDateFormatter = f;
    }
    
    public static AbstractFormatter getFixedDateFormatter() {
    	
    	return fixedDateFormatter;
    }
    
    public static void setFixedTimeFormatter(AbstractFormatter f) {
    	fixedTimeFormatter = f;
    }
    
    public static AbstractFormatter getFixedTimeFormatter() {
    	
    	return fixedTimeFormatter;
    }
    
    
    public static void init() {
    }

	public static void loadDefaults() {
		addFormatter("anniversary", new AnniversaryFormatter());
		addFormatter("anniversaryAbbr", new AnniversaryAbbrFormatter());
		addFormatter("bigLong", new BigLongFormatter());
		addFormatter("capitalize", new CapitalizeFormatter());
		addFormatter("collectionSize", new CollectionSizeFormatter());
		addFormatter("collapsedText", new CollapsedTextFormatter());
		addFormatter("cpfCnpj", new CPFCNPJFormatter());
		addFormatter("date", new DateFormatter());
		addFormatter("dbDate", new DBDateFormatter());
		addFormatter("dateTime", new DateTimeFormatter());
		addFormatter("elapsedTime", new ElapsedTimeFormatter());
		addFormatter("extensionFileName", new ExtensionFileNameFormatter());
		addFormatter("float", new FloatFormatter());
		addFormatter("fullDate", new FullDateTimeFormatter(false));
		addFormatter("fullDateTime", new FullDateTimeFormatter(true));
		addFormatter("fullDateLiteral", new FullDateTimeFormatter(false, true));
		addFormatter("html", new HTMLFormatter());
		addFormatter("phone", new PhoneFormatter());
		addFormatter("htmlScript", new HTMLScriptFormatter());
		addFormatter("javascript", new JavascriptFormatter());
		addFormatter("literalDate", new LiteralDateFormatter());
		addFormatter("literalDateTime", new LiteralDateTimeFormatter());
		addFormatter("literalDayOfWeek", new LiteralDayOfWeekFormatter());
		addFormatter("literalAnniversary", new LiteralAnniversaryFormatter());
		addFormatter("money", new MoneyFormatter());
		addFormatter("month", new MonthFormatter());
		addFormatter("noSpecials", new NoSpecialsFormatter());
		addFormatter("poorText", new PoorTextFormatter());
		addFormatter("remainingTime", new RemainingTimeFormatter());
		addFormatter("seoURL", new SEOURLFormatter());
		addFormatter("simpleElapsedTime", new SimpleElapsedTimeFormatter());
		addFormatter("simpleLiteralDate", new SimpleLiteralDateFormatter());
		addFormatter("smartText", new SmartTextFormatter());
		addFormatter("textarea", new TextAreaFormatter());
		addFormatter("time", new TimeFormatter());
		addFormatter("uppercase", new UpperCaseFormatter());
		addFormatter("url", new UrlFormatter());
	}
}
