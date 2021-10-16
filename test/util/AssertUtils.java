package org.futurepages.util;

import org.futurepages.util.The;
import org.junit.Assert;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

public class AssertUtils extends Assert{

	/**
	 * Method for compare msg
	 * @param msg
	 * @param results
	 */
	public static void assertTrues(String[] msg, boolean... results) {
		boolean[] expectArray = new boolean[results.length];
		Arrays.fill(expectArray, true);
		Assert.assertEquals(The.implodedArray(msg,",",null), Arrays.toString(expectArray), Arrays.toString(results));
	}

	/**
	 * Methods for compare lists
	 * @param expected
	 * @param actual
	 */
	public static void assertListEquals(List<String> expected, List<String> actual) {
		for (int i = 0; i < actual.size() - 1; i++) {
			Assert.assertEquals(expected.get(i), actual.get(i));
		}
	}

	/**
	 * Method for template object
	 * @param template
	 * @param params
	 * @return
	 */
	public String expected(Object template, Object... params) {
		return MessageFormat.format(template.toString(),params);
	}


}
