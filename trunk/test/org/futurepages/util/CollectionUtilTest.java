package org.futurepages.util;

import java.util.ArrayList;
import java.util.List;

import org.futurepages.test.factory.ListFactory;
import org.futurepages.testutil.AssertUtil;
import org.junit.Assert;
import org.junit.Test;

public class CollectionUtilTest {
	/* 
	 * Casos de teste aplicados
	 * limite inferior tamanho origem ok
	 * limite superior a tamnho origem
	 * limite == tamanho origem
	 * limite == 0
	 * limite negativo
	 * orige == 0
	 * destino já povoado
	 */
	public void addListToListTestProcedure(List<String> origem,List<List<String>> destino, 
			int limite, List<List<String>> destinoEsperado,	int retornoEsperado,String msg){
		
		int vagasRestantes = CollectionUtil.addListToList(origem, destino, limite);
		Assert.assertEquals(msg,retornoEsperado, vagasRestantes);
		assertarListOfList(destino, destinoEsperado);
	}

	private void assertarListOfList(List<List<String>> atual,List<List<String>> esperado) {
		
		Assert.assertEquals("Listas com tamanhos deferentes.",esperado.size(), atual.size());
		List<String> itemEsperado;
		List<String> itemAtual;
		for (int i = 0; i < esperado.size(); i++) {
			itemEsperado = esperado.get(i);
			itemAtual = atual.get(i);
			
			for (int j = 0; j < itemEsperado.size(); j++) {
				AssertUtil.assertListEquals("List final deferente do esperado.", itemEsperado, itemAtual);
			}
		}
	}
	

	/**
	 * colecao esperada : colecaoDestino.add(origem.sublist(size:limite))
	 */
	@Test
	public void testAddListToList_LimiteMenorTamanhoOrigem(){
		String msg = "Resultado incorreto quando limite é MENOR ao tamanho da lista origem.";
		
		List<String> origem = ListFactory.instance.createListOfString(10);
		
		List<List<String>> destinoEsperado = new ArrayList<List<String>>();
		destinoEsperado.add(origem.subList(0, 8));
		addListToListTestProcedure(origem, new ArrayList<List<String>>(), 8,  destinoEsperado,0,msg);
	}
	
	/**
	 * colecao esperada : colecaoDestino.add(origem)
	 */
	@Test
	public void testAddListToList_LimiteIgualAOrigem(){
		List<String> origem = ListFactory.instance.createListOfString(10);
		List<List<String>> destinoEsperado = new ArrayList<List<String>>();
		destinoEsperado.add(origem);
		
		String msg = "Resultado incorreto quando limite é IGUAL ao tamanho da lista origem.";
		addListToListTestProcedure(origem, new ArrayList<List<String>>(), 10,  destinoEsperado,0,msg);
		
	}

	/**
	 * colecao esperada : colecaoDestino.add(origem) 
	 * tamanhoOrigem:10
	 * limite 11
	 * sobra 1
	 */
	@Test
	public void testAddListToList_LimiteMaiorATamanhoOrigem(){
		List<String> origem = ListFactory.instance.createListOfString(10);
		List<List<String>> destinoEsperado = new ArrayList<List<String>>();
		destinoEsperado.add(origem);
		String msg = "Resultado incorreto quando limite é MAIOR ao tamanho da lista origem.";
		addListToListTestProcedure(origem,  new ArrayList<List<String>>(), 11,  destinoEsperado,1,msg);
		
	}
	
	@Test
	public void testAddListToList_OrigemVazio(){
//		destino == destinoEsperado
		List<String> origem = new ArrayList<String>();
		List<List<String>> destino = new ArrayList<List<String>>();
		List<List<String>> destinoEsperado = new ArrayList<List<String>>();
		
		String msg = "Resultado incorreto quando a origem é vazia.";
		addListToListTestProcedure(origem,  destino, 10,  destinoEsperado,10,msg);
	}
	
	/**
	 * colecaoEsperada: colecao igual á colecao informada 
	 */
	@Test
	public void testAddListToList_limite0(){
		List<String> origem = ListFactory.instance.createListOfString(10);
		List<List<String>> destino = new ArrayList<List<String>>();
		List<List<String>> destinoEsperado = new ArrayList<List<String>>();
		String msg = "Resultado incorreto quando o limite é zero.";
		addListToListTestProcedure(origem,  destino, 0, destinoEsperado,0,msg);
	}
	
	/**
	 * colecaoEsperada: colecao igual á colecao informada 
	 */
	@Test
	public void testAddListToList_limiteNegativo(){
		List<String> origem = ListFactory.instance.createListOfString(10);
		List<List<String>> destino = new ArrayList<List<String>>();
		List<List<String>> destinoEsperado = new ArrayList<List<String>>();
		String msg = "Resultado incorreto quando o limite é negativo.";
		addListToListTestProcedure(origem,  destino, -1, destinoEsperado,-1,msg);
	}

	/**
	 * colecao esperada : colecaoDestino.add(origem)
	 */
	@Test
	public void testAddListToList_destinoComElementosPrevios(){
		List<String> origem = ListFactory.instance.createListOfString(10);
		
		List<List<String>> destino = new ArrayList<List<String>>();
		destino.add(ListFactory.instance.createListOfString(10));
		destino.add(ListFactory.instance.createListOfString(10));
		
		List<List<String>> destinoEsperado = new ArrayList<List<String>>();
		destinoEsperado.addAll(destino);
		destinoEsperado.add(origem);
		
		String msg = "Resultado incorreto quando coleção(destino) passado já possui elementos.";
		addListToListTestProcedure(origem, destino, 10,  destinoEsperado,0,msg);
		
	}
	
	/* 
	 * Casos de teste aplicados
	 * limite inferior tamanho origem ok
	 * limite superior a tamnho origem
	 * limite == tamanho origem
	 * limite == 0
	 * limite negativo
	 * orige == 0
	 * destino já povoado
	 */
	public void addElementsToListTestProcedure(List<String> origem, List<String> destino, int limite, 
			List<String> destinoEsperado,	int retornoEsperado,String msg){
		
		int vagasRestantes = CollectionUtil.addElementsToList(origem, destino, limite);
		Assert.assertEquals(msg,retornoEsperado, vagasRestantes);
		AssertUtil.assertListEquals(msg,destino, destinoEsperado);
	}
	
	
	/**
	 * colecao esperada : colecaoDestino.add(origem.sublist(size:limite))
	 * 
	 */
	@Test
	public void testAddElementListToList_LimiteMenorTamanhoOrigem(){
		String msg = "Resultado incorreto quando limite é MENOR ao tamanho da lista origem.";

		List<String> origem = ListFactory.instance.createListOfString(10);
		List<String> destinoEsperado = origem.subList(0, 9);
		addElementsToListTestProcedure(origem, new ArrayList<String>(), 9,  destinoEsperado, 0, msg);
	}
	
	/**
	 * colecao esperada : colecaoDestino.add(origem)
	 */
	@Test
	public void testAddElementToList_LimiteIgualAOrigem(){
		List<String> origem = ListFactory.instance.createListOfString(10);
		List<String> destinoEsperado = new ArrayList<String>();
		destinoEsperado.addAll(origem);
		
		String msg = "Resultado incorreto quando limite é IGUAL ao tamanho da lista origem.";
		addElementsToListTestProcedure(origem, new ArrayList<String>(), 10,  destinoEsperado,0,msg);
		
	}
	
	/**
	 * colecao esperada : colecaoDestino.add(origem) 
	 * tamanhoOrigem:10
	 * limite 11
	 * sobra 1
	 */
	@Test
	public void testAddElementToList_LimiteMaiorATamanhoOrigem(){
		List<String> origem = ListFactory.instance.createListOfString(10);
		List<String> destinoEsperado = new ArrayList<String>();
		destinoEsperado.addAll(origem);
		String msg = "Resultado incorreto quando limite é MAIOR ao tamanho da lista origem.";
		addElementsToListTestProcedure(origem,  new ArrayList<String>(), 11,  destinoEsperado,1,msg);
		
	}
	
	@Test
	public void testAddElementToList_OrigemVazio(){
//		destino == destinoEsperado
		List<String> origem = new ArrayList<String>();
		List<String> destino = new ArrayList<String>();
		List<String> destinoEsperado = new ArrayList<String>();
		
		String msg = "Resultado incorreto quando a origem é vazia.";
		addElementsToListTestProcedure(origem,  destino, 10,  destinoEsperado,10,msg);
	}
	
	/**
	 * colecaoEsperada: colecao igual á colecao informada 
	 */
	@Test
	public void testAddElementToList_limite0(){
		List<String> origem = ListFactory.instance.createListOfString(10);
		List<String> destino = new ArrayList<String>();
		List<String> destinoEsperado = new ArrayList<String>();
		String msg = "Resultado incorreto quando o limite é zero.";
		addElementsToListTestProcedure(origem,  destino, 0, destinoEsperado,0,msg);
	}
	
	/**
	 * colecaoEsperada: colecao igual á colecao informada 
	 */
	@Test
	public void testAddElementToList_limiteNegativo(){
		List<String> origem = ListFactory.instance.createListOfString(10);
		List<String> destino = new ArrayList<String>();
		List<String> destinoEsperado = new ArrayList<String>();
		String msg = "Resultado incorreto quando o limite é negativo.";
		addElementsToListTestProcedure(origem,  destino, -1, destinoEsperado,-1,msg);
	}
	
	/**
	 * colecao esperada : colecaoDestino.add(origem)
	 */
	@Test
	public void testAddElementToList_destinoComElementosPrevios(){
		List<String> origem = ListFactory.instance.createListOfString(10);
		
		List<String> destino = new ArrayList<String>();
		destino.addAll(ListFactory.instance.createListOfString(10));
		destino.addAll(ListFactory.instance.createListOfString(10));
		
		List<String> destinoEsperado = new ArrayList<String>();
		destinoEsperado.addAll(destino);
		destinoEsperado.addAll(origem);
		
		String msg = "Resultado incorreto quando coleção(destino) passado já possui elementos.";
		addElementsToListTestProcedure(origem, destino, 10,  destinoEsperado,0,msg);
		
	}

	
}
