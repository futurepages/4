package org.futurepages.util;

import org.futurepages.util.The;
import org.junit.Assert;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

public class AssertUtils extends Assert{

	public static void assertTrues(String[] msg, boolean... results) {
		boolean[] expectArray = new boolean[results.length];
		Arrays.fill(expectArray, true);
		Assert.assertEquals(The.implodedArray(msg,",",null), Arrays.toString(expectArray), Arrays.toString(results));
	}
}
