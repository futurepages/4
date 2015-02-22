package org.futurepages.formatters;

import org.futurepages.core.formatter.AbstractFormatter;

import java.util.Locale;

/**
 *
 * @author leandro
 */
public class BigLongFormatter extends AbstractFormatter<Long> {


	@Override
    public String format(Long value, Locale locale) {
		long longPivot = 1000;

		if(value<longPivot){
			return value.toString();
		}
		long num = (value / longPivot);

		String strQuant = "K"; //mil

		if(num>=longPivot){
			num = (num / longPivot);
			strQuant = "M"; //milhão
		}

		if(num>=longPivot){
			num = (num / longPivot);
			strQuant = "B"; //bilhão
		}

		if(num>=longPivot){
			num = (num / longPivot);
			strQuant = "T"; //trilhão
		}

		if(num>=longPivot){
			return "+100T"; //trilhão
		}

        return num + strQuant;
	}

	public static void main(String[] args) {
		System.out.println((new BigLongFormatter()).format(3l)); //3
		System.out.println((new BigLongFormatter()).format(30l)); //30
		System.out.println((new BigLongFormatter()).format(300l)); //300
		System.out.println((new BigLongFormatter()).format(3000l)); //3K
		System.out.println((new BigLongFormatter()).format(3500l)); //3K
		System.out.println((new BigLongFormatter()).format(35000l)); //35K
		System.out.println((new BigLongFormatter()).format(350000l)); //350K
		System.out.println((new BigLongFormatter()).format(3500000l)); //3M
		System.out.println((new BigLongFormatter()).format(35000000l)); //35M
		System.out.println((new BigLongFormatter()).format(350000000l));  //350M
		System.out.println((new BigLongFormatter()).format(3500000000l)); //3B
		System.out.println((new BigLongFormatter()).format(35000000000l)); //35B

		System.out.println((new BigLongFormatter()).format(999l)); //999
		System.out.println((new BigLongFormatter()).format(9999l)); //9K

		System.out.println((new BigLongFormatter()).format(1l)); //1
		System.out.println((new BigLongFormatter()).format(10l)); //10
		System.out.println((new BigLongFormatter()).format(100l)); //100
		System.out.println((new BigLongFormatter()).format(1000l)); //1K
		System.out.println((new BigLongFormatter()).format(10000l)); //10K
		System.out.println((new BigLongFormatter()).format(100000l)); //100K
		System.out.println((new BigLongFormatter()).format(1000000l)); //1M
		System.out.println((new BigLongFormatter()).format(10000000l)); //10M
		System.out.println((new BigLongFormatter()).format(100000000l)); //100M
		System.out.println((new BigLongFormatter()).format(1000000000l)); //1B
		System.out.println((new BigLongFormatter()).format(10000000000l)); //10B
		System.out.println((new BigLongFormatter()).format(100000000000l)); //100B
		System.out.println((new BigLongFormatter()).format(1000000000000l)); //1T
		System.out.println((new BigLongFormatter()).format(10000000000000l)); //10T
		System.out.println((new BigLongFormatter()).format(100000000000000l)); //100T
		System.out.println((new BigLongFormatter()).format(1000000000000000l)); //+100T
		System.out.println((new BigLongFormatter()).format(100000000000000000l)); //+100T
		System.out.println((new BigLongFormatter()).format(1000000000000000000l)); //+100T
		System.out.println((new BigLongFormatter()).format(Long.MAX_VALUE)); //+100T
	}
}