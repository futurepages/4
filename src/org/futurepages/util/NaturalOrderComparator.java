package org.futurepages.util;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;

/* @see http://www.java2s.com/Code/Java/Collections-Data-Structure/NaturalOrderComparator.htm
Sugiro colocar no futurepages */
public class NaturalOrderComparator implements Comparator {

	int compareRight(String a, String b) {
		int bias = 0;
		int ia = 0;
		int ib = 0;

		// The longest run of digits wins. That aside, the greatest
		// value wins, but we can't know that it will until we've scanned
		// both numbers to know that they have the same magnitude, so we
		// remember it in BIAS.
		for (; ; ia++, ib++) {
			char ca = charAt(a, ia);
			char cb = charAt(b, ib);

			if (!Character.isDigit(ca) && !Character.isDigit(cb)) {
				return bias;
			} else if (!Character.isDigit(ca)) {
				return -1;
			} else if (!Character.isDigit(cb)) {
				return +1;
			} else if (ca < cb) {
				if (bias == 0) {
					bias = -1;
				}
			} else if (ca > cb) {
				if (bias == 0)
					bias = +1;
			} else if (ca == 0 && cb == 0) {
				return bias;
			}
		}
	}

	public <T, U extends Comparable<? super U>> Comparator<T> comparing(
			Function<? super T, ? extends U> keyExtractor)
	{
		Objects.requireNonNull(keyExtractor);
		return (Comparator<T> & Serializable)
				(c1, c2) -> compare(keyExtractor.apply(c1), keyExtractor.apply(c2));
	}

	public int compare(Object o1, Object o2) {
		String a = The.strWithoutAccentuation(o1.toString());
		String b = The.strWithoutAccentuation(o2.toString());

		int ia = 0, ib = 0;
		int nza = 0, nzb = 0;
		char ca, cb;
		int result;

		while (true) {
			// only count the number of zeroes leading the last number compared
			nza = nzb = 0;

			ca = charAt(a, ia);
			cb = charAt(b, ib);

			// skip over leading spaces or zeros
			while (Character.isSpaceChar(ca) || ca == '0') {
				if (ca == '0') {
					nza++;
				} else {
					// only count consecutive zeroes
					nza = 0;
				}

				ca = charAt(a, ++ia);
			}

			while (Character.isSpaceChar(cb) || cb == '0') {
				if (cb == '0') {
					nzb++;
				} else {
					// only count consecutive zeroes
					nzb = 0;
				}

				cb = charAt(b, ++ib);
			}

			// process run of digits
			if (Character.isDigit(ca) && Character.isDigit(cb)) {
				if ((result = compareRight(a.substring(ia), b.substring(ib))) != 0) {
					return result;
				}
			}

			if (ca == 0 && cb == 0) {
				// The strings compare the same. Perhaps the caller
				// will want to call strcmp to break the tie.
				return nza - nzb;
			}

			if (ca < cb) {
				return -1;
			} else if (ca > cb) {
				return +1;
			}

			++ia;
			++ib;
		}
	}

	static char charAt(String s, int i) {
		if (i >= s.length()) {
			return 0;
		} else {
			return s.charAt(i);
		}
	}

//	public static void main(String[] args) {
//		String[] strings = new String[]{"1-2", "1-02", "1-20", "10-20", "fred", "jane", "pic01",
//				"pic2", "pic02", "pic02a", "pic3", "pic4", "pic 4 else", "pic 5", "pic05", "pic 5",
//				"pic 5 something", "pic 6", "pic   7", "pic100", "pic100a", "pic120", "pic121",
//				"pic02000", "tom", "x2-g8", "x2-y7", "x2-y08", "x8-y8"};
//
//		List orig = Arrays.asList(strings);
//
//		System.out.println("Original: " + orig);
//
//		List scrambled = Arrays.asList(strings);
//		Collections.shuffle(scrambled);
//
//		System.out.println("Scrambled: " + scrambled);
//
//		Collections.sort(scrambled, new NaturalOrderComparator());
//
//		System.out.println("Sorted: " + scrambled);
//	}
}
