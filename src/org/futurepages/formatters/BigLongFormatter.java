package org.futurepages.formatters;

import org.futurepages.core.formatter.AbstractFormatter;

import java.util.Locale;

/**
 *
 * @author leandro
 */
public class BigLongFormatter extends AbstractFormatter<Long>{


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
		System.out.println((new BigLongFormatter()).format(3l, null)); //3
		System.out.println((new BigLongFormatter()).format(30l, null)); //30
		System.out.println((new BigLongFormatter()).format(300l, null)); //300
		System.out.println((new BigLongFormatter()).format(3000l, null)); //3K
		System.out.println((new BigLongFormatter()).format(3500l, null)); //3K
		System.out.println((new BigLongFormatter()).format(35000l, null)); //35K
		System.out.println((new BigLongFormatter()).format(350000l, null)); //350K
		System.out.println((new BigLongFormatter()).format(3500000l, null)); //3M
		System.out.println((new BigLongFormatter()).format(35000000l, null)); //35M
		System.out.println((new BigLongFormatter()).format(350000000l, null));  //350M
		System.out.println((new BigLongFormatter()).format(3500000000l, null)); //3B
		System.out.println((new BigLongFormatter()).format(35000000000l, null)); //35B

		System.out.println((new BigLongFormatter()).format(999l, null)); //999
		System.out.println((new BigLongFormatter()).format(9999l, null)); //9K

		System.out.println((new BigLongFormatter()).format(1l, null)); //1
		System.out.println((new BigLongFormatter()).format(10l, null)); //10
		System.out.println((new BigLongFormatter()).format(100l, null)); //100
		System.out.println((new BigLongFormatter()).format(1000l, null)); //1K
		System.out.println((new BigLongFormatter()).format(10000l, null)); //10K
		System.out.println((new BigLongFormatter()).format(100000l, null)); //100K
		System.out.println((new BigLongFormatter()).format(1000000l, null)); //1M
		System.out.println((new BigLongFormatter()).format(10000000l, null)); //10M
		System.out.println((new BigLongFormatter()).format(100000000l, null)); //100M
		System.out.println((new BigLongFormatter()).format(1000000000l, null)); //1B
		System.out.println((new BigLongFormatter()).format(10000000000l, null)); //10B
		System.out.println((new BigLongFormatter()).format(100000000000l, null)); //100B
		System.out.println((new BigLongFormatter()).format(1000000000000l, null)); //1T
		System.out.println((new BigLongFormatter()).format(10000000000000l, null)); //10T
		System.out.println((new BigLongFormatter()).format(100000000000000l, null)); //100T
		System.out.println((new BigLongFormatter()).format(1000000000000000l, null)); //+100T
		System.out.println((new BigLongFormatter()).format(100000000000000000l, null)); //+100T
		System.out.println((new BigLongFormatter()).format(1000000000000000000l, null)); //+100T
		System.out.println((new BigLongFormatter()).format(Long.MAX_VALUE, null)); //+100T
	}
}