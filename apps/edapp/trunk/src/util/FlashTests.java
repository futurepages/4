package util;

import javassist.CannotCompileException;
import javassist.NotFoundException;
import org.futurepages.ant.BuildWidgetsets;
import org.futurepages.core.locale.Txt;

public class FlashTests {

	public static void main(String[] args) throws CannotCompileException, NotFoundException {
		//(new BuildWidgetsets()).execute();
		System.out.println(Txt.get("admin.user.my_name_is_leandro[asdaksd kaksdka dkaks dkaskd ks.dasda.a.dasd.d]"));
	}
}