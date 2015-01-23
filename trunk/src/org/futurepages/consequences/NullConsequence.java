package org.futurepages.consequences;

import org.futurepages.exceptions.ConsequenceException;
import org.futurepages.core.action.Action;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.futurepages.core.consequence.Consequence;

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