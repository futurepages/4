package org.futurepages.menta.core.filter;

import org.futurepages.menta.core.action.Action;
import org.futurepages.menta.core.consequence.Consequence;

public interface AfterConsequenceFilter extends Filter {
    
    public void afterConsequence(Action action, Consequence c, boolean conseqExecuted, boolean actionExecuted, String result);
    
}