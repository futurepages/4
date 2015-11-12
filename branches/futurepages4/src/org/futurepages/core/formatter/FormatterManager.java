package org.futurepages.core.formatter;

import org.futurepages.util.The;

/**
 * @author Sergio Oliveira
 */
public class FormatterManager {

    public static AbstractFormatter getFormatter(String name) {
        try {
            return (AbstractFormatter) Class.forName("org.futurepages.formatters." + The.capitalizedWord(name)).newInstance();
        } catch (Exception e) {
            try {
                //TODO mantain while migrating brazil formatters to out there.
                return (AbstractFormatter) Class.forName("org.futurepages.formatters.brazil." + The.capitalizedWord(name)).newInstance();
            } catch (Exception e1) {
                throw new RuntimeException(e1.getMessage(), e);
            }
        }
    }
}
