package org.futurepages.test.util;

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

	public static void assertListEquals(List<String> expected, List<String> actual) {
		for (int i = 0; i < actual.size() - 1; i++) {
			Assert.assertEquals(expected.get(i), actual.get(i));
		}
	}

	public String expected(Object template, Object... params) {
		return MessageFormat.format(template.toString(),params);
	}


	public static String[] array(String... strings) {
		return strings;
	}

	public static boolean[] array(boolean... bools) {
		return bools;
	}

	public static int[] array(int... ints) {
		return ints;
	}

	public static String currentTimeLastChars(int i) {
		return The.lastCharsOf(i, ""+System.currentTimeMillis());
	}
}
