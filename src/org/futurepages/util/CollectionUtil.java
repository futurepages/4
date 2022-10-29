package org.futurepages.util;

import org.futurepages.exceptions.EmptyCollectionException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CollectionUtil {

	public static <T> T[] toArray(List<T> list) {
		T[] array = null;
		if(list!=null && !list.isEmpty()){
			Object firstEl = list.get(0);
			//noinspection unchecked
			array = (T[]) Array.newInstance(!(firstEl instanceof Enum<?>)
																	? firstEl.getClass()
																	: ((Enum)firstEl).getDeclaringClass()
											,list.size()
			);
			list.toArray(array);

		}
		return array;
	}

	public static void touch(Collection collection) {
		if (collection != null) {
			collection.size();
		}
	}

	public static <T> int addListToList(List<T> origem, List<List<T>> destino, int limite) {

		int vagasRestantes = limite;
		if (vagasRestantes < 0) {
			return vagasRestantes;
		}
		List<T> subList;
		if (origem.size() > limite) {
			subList = origem.subList(0, limite);
			vagasRestantes -= subList.size();
		} else {
			subList = origem;
			vagasRestantes -= origem.size();
		}
		if (!subList.isEmpty()) {
			destino.add(subList);
		}
		return vagasRestantes;
	}

	/**
	 * 
	 * @param <T> tipo do elemento
	 * @param origem lista origem dos elementos
	 * @param destino lista onde serão adiconados os elementos
	 * @param limite quantidade maxima de elementos que podem ser copiados de origem para destino
	 * @return
	 */
	public static <T> int addElementsToList(List<T> origem, List<T> destino, int limite) {

		int vagasRestantes = limite;
		if (vagasRestantes < 0) {
			return vagasRestantes;
		}
		List<T> subList;
		if (origem.size() > limite) {
			subList = origem.subList(0, limite);
			vagasRestantes -= subList.size();
		} else {
			subList = origem;
			vagasRestantes -= origem.size();
		}
		if (!subList.isEmpty()) {
			destino.addAll(subList);
		}
		return vagasRestantes;
	}

	public static <T> T getLast(List<T> list) throws EmptyCollectionException {
		if (!list.isEmpty()) {
			return list.get(list.size() - 1);
		}
		throw new EmptyCollectionException();
	}

	public static <T> T getFirst(List<T> list) throws EmptyCollectionException {
		if (!list.isEmpty()) {
			return list.get(0);
		}
		throw new EmptyCollectionException();
	}

	@SuppressWarnings("rawtypes")
	public static boolean empty(Collection colecao) {
		if (colecao == null) {
			return true;
		} else return colecao.size() <= 0;
	}

	@SafeVarargs
	public static <T> ArrayList<T> getListToElements(T... elementsList) {
		ArrayList<T> list = new ArrayList<T>();
		if (elementsList != null) {
			list.addAll(Arrays.asList(elementsList));
		}
		return list;
	}
}
