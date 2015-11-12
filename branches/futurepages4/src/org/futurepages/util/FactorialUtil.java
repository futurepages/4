package org.futurepages.util;

import java.util.HashMap;
import java.util.Map;

public class FactorialUtil {
	
	private static  Map<Long, Long> fatoriais = new HashMap<Long, Long>();
	
	static {
		fatoriais.put(0L, 1L);
		fatoriais.put(1L, 1L);
		fatoriais.put(2L, 2L);
		fatoriais.put(3L, 6L);
		fatoriais.put(4L, 24L);
		fatoriais.put(5L, 120L);
		fatoriais.put(6L, 720L);
		fatoriais.put(7L, 5040L);
		fatoriais.put(8L, 40320L);
		fatoriais.put(9L, 362880L);
		fatoriais.put(10L, 3628800L);
		fatoriais.put(11L, 39916800L);
		fatoriais.put(12L, 479001600L);
		fatoriais.put(13L, 6227020800L);
		fatoriais.put(14L, 87178291200L);
		fatoriais.put(15L, 1307674368000L);
		fatoriais.put(16L, 20922789888000L);
		fatoriais.put(17L, 355687428096000L);
		fatoriais.put(18L, 6402373705728000L);
		fatoriais.put(19L, 121645100408832000L);

	}

	public static long fat(Integer n){
		return fat(n.longValue());
	}

	public static long fat(Long n){
		if(n<0) return 1;

		long fatN = 1;
		if(fatoriais.containsKey(n)){
			fatN = fatoriais.get(n);
			fatoriais.put(n, fatN);
			return fatN;
		}
		for (int i = 1; i <= n; i++)
			fatN *= i;
		return fatN;
	}
}
