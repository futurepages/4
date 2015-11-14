package org.futurepages.menta.consequences;

import org.futurepages.menta.core.action.Action;
import org.futurepages.menta.core.consequence.Consequence;
import org.futurepages.menta.exceptions.ConsequenceException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A consequence that does nothing.
 *
 * @author Sergio Oliveira
 */
public class NullConsequence implements Consequence {
    
    /**
     * This method does nothing.
     */
	@Override
	public void execute(Action a, HttpServletRequest req, HttpServletResponse res) throws ConsequenceException {
        // do nothing...
    }
}