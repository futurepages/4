package util;

import javassist.CannotCompileException;
import javassist.NotFoundException;
import org.futurepages.ant.BuildWidgetsets;

public class FlashTests {

	public static void main(String[] args) throws CannotCompileException, NotFoundException {
		(new BuildWidgetsets()).execute();
	}
}