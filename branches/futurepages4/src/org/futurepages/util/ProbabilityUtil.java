package org.futurepages.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author leandro
 */
public class ProbabilityUtil {

	/**
	 * Gera combinações para os elemtos informados.
	 * @param elements elementos a serem combinados
	 * @param groupSize tamanho do agrupapemto
	 * @return {@link List} com as conbinações geradas.
	 */
	public static List<String[]> listCombinations(String[] elements, int groupSize){
		List<String[]> result = new ArrayList<String[]>();
		result.addAll(combine(0, elements.length - groupSize, elements,  new String[groupSize],  groupSize, 0));
		return result;

	}
	/**
	 * Calcula quantos subconjuntos N elementos podem ser formados de um conjunto com S elementos.
	 * 
	 * @param numberOfElements : S
	 * @param groupSize : N
	 * 		
	 * @return fat(S) / [fat(N) * fat(S - N)] 
	 */
	public static int numberOfCombinations(int numberOfElements, int groupSize){
		int N = numberOfElements; 
		int S = groupSize; 
		Long fatN = FactorialUtil.fat(N);
		Long fatS = FactorialUtil.fat(S);
		Long fatN_S = FactorialUtil.fat(N - S);
		
		BigDecimal numberComb = new BigDecimal(fatN / (fatS * fatN_S));
		return numberComb.intValue();
	}

	private static List<String[]> combine(int begin, int end, String[] elements, String[] combination, int groupSize , int depth) {
		List<String[]> result = new ArrayList<String[]>();
		if(combination.length == 0)
			return result;
		if ((depth + 1) >= groupSize) {
			for (int x = begin; x <= end; x++) {
				combination[depth] = elements[x];
				result.add(combination.clone());
			}
		} else {
			for (int x = begin; x <= end; x++) {
				combination[depth] = elements[x];
				result.addAll(combine(x + 1, end+1, elements, combination, groupSize, depth + 1));
			}
		}
		return result;

	}


}