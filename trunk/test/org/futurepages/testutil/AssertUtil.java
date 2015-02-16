package org.futurepages.testutil;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

@SuppressWarnings({ "serial", "unchecked" })
public class AssertUtil {

	
	public static <T extends Comparable> void  assertListOfComparable(String message, List<T> expected, List<T> actual){
		Assert.assertEquals(message+" The 'expected' and 'actual' lists have different size (number of elements).",expected.size(), actual.size());
		
		T expectedItem = null;
		T actualItem = null;
		
		for (int i = 0; i < expected.size(); i++) {
			expectedItem = expected.get(i);
			actualItem = actual.get(i);
			Assert.assertEquals(message+" Different elements at index '"+i+"'.",
					0,expectedItem.compareTo(actualItem));
		}
	}

	public static <T extends Object> void  assertListEquals(String message, List<T> expected, List<T> actual){
		assertCollectionEquals(message, expected, actual);
		
		T expectedItem = null;
		T actualItem = null;
		
		for (int i = 0; i < expected.size(); i++) {
			expectedItem = expected.get(i);
			actualItem = actual.get(i);
			Assert.assertEquals("Different elements at index '"+i+"'.",expectedItem, actualItem);
		}
	}

	public static <T extends Object> void  assertCollectionEquals(String message, Collection<T> expected, Collection<T> atual){
		Assert.assertEquals(message+" The 'expected' and 'actual' lists have different size (number of elements).",expected.size(), atual.size());
		Assert.assertTrue(message+" (Expected collection does not contain all elemements from atual collection.)",expected.containsAll(atual));
	}
	
	public static <T extends Object> void  assertArrayEquals(String message, Object[] expected, Object[] atual){
		assertListEquals(message, Arrays.asList(expected), Arrays.asList(atual));
	}
	
	public static void assertCalendarTimeEquals(String message, Calendar expected, Calendar actual){
		assertCalendarDateEquals(message, expected, actual);
		Assert.assertEquals("Difference in field Hour. "+message,expected.get(Calendar.HOUR), actual.get(Calendar.HOUR));
		Assert.assertEquals("Difference in field Minute. "+message,expected.get(Calendar.MINUTE), actual.get(Calendar.MINUTE));
		Assert.assertEquals("Difference in field Second. "+message,expected.get(Calendar.SECOND), actual.get(Calendar.SECOND));
	}
	
	public static void assertCalendarDateEquals(String message, Calendar expected, Calendar actual){
		Assert.assertEquals("Difference in field Year. "+message,expected.get(Calendar.YEAR), actual.get(Calendar.YEAR));
		Assert.assertEquals("Difference in field Month. "+message,expected.get(Calendar.MONTH), actual.get(Calendar.MONTH));
		Assert.assertEquals("Difference in field Day. "+message,expected.get(Calendar.DAY_OF_MONTH), actual.get(Calendar.DAY_OF_MONTH));
	}
	
	public static void assertBigDecimalEquals(String message, BigDecimal expected, BigDecimal actual, double error){
		BigDecimal diff = expected.subtract(actual).abs();
		boolean isDiffLoweEqualsError = diff.compareTo(new BigDecimal(error))<=0;
		Assert.assertTrue(message+ " diff: "+diff + " error: "+error, isDiffLoweEqualsError);
	}
	
	public static <K extends Object,V extends Object> void assertMapEquals(String message, Map<K,V> expected, Map<K,V> actual){
		assertCollectionEquals(message +" >Different values.", expected.values(), actual.values());
		assertCollectionEquals(message +" >Different set of keys.", expected.keySet(), actual.keySet());
		for (K  key : expected.keySet()) {
			Assert.assertEquals(message +" > Different values for key: '"+key+"'. ",expected.get(key), actual.get(key));
		}
	}
	
	public static void assertDoubleEquals(String message, Double expected, Double actual, double error){
		double diff = expected - actual;
		if(diff<0){
			diff *=-1;
		}
		Assert.assertTrue(message+ " diff: "+diff + " error: "+error, diff <= error);
	}	
}