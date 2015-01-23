
package org.futurepages.core.resource;

import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;

/**
 *
 * @author leandro
 */
public class FpgErrorReporter implements ErrorReporter {

	public void warning(String string, String string1, int i, String string2, int i1) {
		log("[WARNING]  "+string+" - "+string1 + " - "+i+" - "+string2+" - "+i1);
	}

	public void error(String string, String string1, int i, String string2, int i1) {
		log("[ERROR]  "+string+" - "+string1 + " - "+i+" - "+string2+" - "+i1);
	}

	public EvaluatorException runtimeError(String string, String string1, int i, String string2, int i1) {
		return new EvaluatorException(string, string1, i, string2, i1);
	}

	private void log(String msg){
		System.out.println("[ JS-Min ... ] "+msg);
	}
}
