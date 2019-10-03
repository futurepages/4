package org.futurepages.util;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

public class PhoneUtil {

	// ao atualizar a lib conferir se continuam iguais as opções de PhoneNumberUtil.PhoneNumberFormat
	 public static enum NumberFormat {
        E164,
        INTERNATIONAL,
        NATIONAL,
        RFC3966;
    }

	private static String cleanNumber(String number){
		return !Is.empty(number)?number.replaceAll("[^\\d.+\\(\\)\\- ]", "").trim():"";
	}

	public static String normalize(String number, String defaultRegion) {
		return normalize(number, defaultRegion, null);
	}

	public static String normalize(String number, String defaultRegion, NumberFormat numberFormat) {
		String filteredNumber = cleanNumber(number);
		if (!Is.empty(filteredNumber)) {
			PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
			try {
				Phonenumber.PhoneNumber swissNumberProto = phoneUtil.parse(filteredNumber, defaultRegion);
				if (phoneUtil.isValidNumber(swissNumberProto)) {
					if (numberFormat != null){
						return phoneUtil.format(swissNumberProto, PhoneNumberUtil.PhoneNumberFormat.valueOf(numberFormat.name()));
					}else{
						return phoneUtil.formatOutOfCountryCallingNumber(swissNumberProto, defaultRegion);
					}
				}
			} catch (NumberParseException e) {
				return null;
			}
		}
		return null;
	}

	public static String normalizeAsE164(String number, String defaultRegion) {
		return normalize(number, defaultRegion, NumberFormat.E164);
	}

	public static boolean isValid(String number, String defaultRegion){
		String filteredNumber = cleanNumber(number);
		if (!Is.empty(filteredNumber)) {
			PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
			try {
				Phonenumber.PhoneNumber swissNumberProto = phoneUtil.parse(filteredNumber, defaultRegion);
				return phoneUtil.isValidNumber(swissNumberProto);
			} catch (NumberParseException e) {
				return false;
			}
		}
		return false;
	}

	public static boolean isMobile(String number, String defaultRegion){
		String filteredNumber = cleanNumber(number);
		if (!Is.empty(filteredNumber)) {
			PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
			try {
				Phonenumber.PhoneNumber swissNumberProto = phoneUtil.parse(filteredNumber, defaultRegion);
				return phoneUtil.isValidNumber(swissNumberProto) && phoneUtil.getNumberType(swissNumberProto) == PhoneNumberUtil.PhoneNumberType.MOBILE;
			} catch (NumberParseException ignored) {
				return false;
			}
		}
		return false;
	}

}
