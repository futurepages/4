package org.futurepages.util.brazil;

import org.futurepages.util.PhoneUtil;

public class BrazilPhoneUtil {

	private static final String DEFAULT_REGION = "BR";

	public static String normalize(String number) {
		return PhoneUtil.normalize(number, DEFAULT_REGION, null);
	}

	public static String normalize(String number, PhoneUtil.NumberFormat numberFormat) {
		return PhoneUtil.normalize(number, DEFAULT_REGION, numberFormat);
	}

	public static String normalizeAsE164(String number) {
		return PhoneUtil.normalizeAsE164(number, DEFAULT_REGION);
	}

	public static boolean isValid(String number){
		return PhoneUtil.isValid(number, DEFAULT_REGION);
	}

	public static boolean isMobile(String number){
		return PhoneUtil.isMobile(number, DEFAULT_REGION);
	}

}
