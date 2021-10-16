package org.futurepages.test.util;

import org.futurepages.menta.core.action.Action;
import org.futurepages.menta.core.input.MapInput;
import org.futurepages.menta.core.output.MapOutput;

public class ActionUtils {
	public static <A extends Action> A get(Class<A> actionClass) {
		A action;
		try {
			action = actionClass.newInstance();
			action.setInput(new MapInput());
			action.setOutput(new MapOutput());
			return action;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
