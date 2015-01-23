package org.futurepages.util;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

/**
 * @author Sergio Oliveira
 */
public class EnumerationUtil {

	public static Iterator<String> toIterator(final Enumeration en) {

		return new Iterator() {

			public boolean hasNext() {
				return en.hasMoreElements();
			}

			public Object next() {
				return en.nextElement();
			}

			public void remove() {

				throw new UnsupportedOperationException("Iterator is backed by Enumeration!");

			}

		};
	}

	/**
	 * Lista de enums de acordo com as chaves
	 * @param aClass tipo de enum
	 * @param keys chaves
	 * @return lista de enums
	 */
	public static List listByKeys(Class aClass, String[] keys) {
		List list = new ArrayList();
		for (String key : keys) {
			list.add(Enum.valueOf(aClass, key));
		}
		return list;
	}
}

