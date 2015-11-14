package org.futurepages.menta.consequences;

import org.futurepages.menta.core.action.Action;
import org.futurepages.menta.core.consequence.Consequence;
import org.futurepages.menta.core.output.Output;
import org.futurepages.menta.exceptions.ConsequenceException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Returns a String as the result of an action.
 * 
 * @author Sergio Oliveira Jr.
 */
public class StringConsequence implements Consequence {

	public static final String KEY = "string";
	private String key = KEY;

	public StringConsequence() {
	}

	public StringConsequence(String key) {
		this.key = key;
	}

	@Override
	public void execute(Action a, HttpServletRequest req, HttpServletResponse res) throws ConsequenceException {
		Output output = a.getOutput();
		res.setContentType("text/plain");
		PrintWriter out = null;
		try {
			out = res.getWriter();
		} catch (IOException e) {
			throw new ConsequenceException(e);
		}
		Object value = output.getValue(key);

		if (value == null) {
			throw new ConsequenceException("Cannot find string: " + key);
		}

		out.print(value.toString());
		out.close();
	}
}
