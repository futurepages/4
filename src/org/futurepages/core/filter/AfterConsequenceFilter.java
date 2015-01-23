package org.futurepages.core.filter;

import org.futurepages.core.action.Action;
import org.futurepages.core.consequence.Consequence;

public interface AfterConsequenceFilter extends Filter {
    
    public void afterConsequence(Action action, Consequence c, boolean conseqExecuted, boolean actionExecuted, String result);
    
}