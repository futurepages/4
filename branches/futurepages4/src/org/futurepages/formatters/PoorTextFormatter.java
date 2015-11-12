/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.futurepages.formatters;

import org.futurepages.core.formatter.AbstractFormatter;
import java.util.Locale;
import org.futurepages.util.html.HtmlStripper;

/**
 *
 * @author diogenes
 */
public class PoorTextFormatter extends AbstractFormatter {

    @Override
    public String format(Object value, Locale loc) {
        return new HtmlStripper((String)value).poorText();
    }
}
