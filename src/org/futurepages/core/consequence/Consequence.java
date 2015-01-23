package org.futurepages.core.consequence;

import org.futurepages.exceptions.ConsequenceException;
import org.futurepages.core.action.Action;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Defines a consequence.
 * An action has a result that in turn has a consequence.
 * 
 * @author Sergio Oliveira
 */
public interface Consequence {
	
    /**
     * Executes this consequence.
     * A consequence must do something with your web application.
     * Here is where you define what the consequence does.
     *
     * @param a The action that generated this consequence.
     * @param req The servlet request of the action.
     * @param res The servlet response of the action.
     */
    public void execute(Action a, HttpServletRequest req, HttpServletResponse res) throws ConsequenceException;
	
}