/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.futurepages.formatters;

import org.futurepages.core.formatter.Formatter;
import java.util.Locale;
import org.futurepages.util.html.HtmlStripper;

/**
 *
 * @author diogenes
 */
public class PoorTextFormatter implements Formatter {

    @Override
    public String format(Object value, Locale loc) {
        return new HtmlStripper((String)value).poorText();
    }
}
