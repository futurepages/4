package org.futurepages.json;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JSONGenericRendererTest {

	private JSONGenericRenderer renderer;
	
	@Before
	public void setUp(){
		renderer = new JSONGenericRenderer();
	}
	
	@Test
	public void testeSemConcorrencia() throws Exception{
		Caixa caixa = criarCaixa(1);
		String encode = renderer.encode(caixa, null, true);
		Assert.assertNotNull(encode);
	}
	
	@Test
	public void testeConcorrente() throws InterruptedException{
		Map<String, Renderer> threads = new HashMap<String, Renderer>();
		String[] nomes = new String[]{"A","B","C","D", "E","F","G","H","I","J"};
		Map<String, String> answers = new ConcurrentHashMap<String, String>();
		Caixa caixa = criarCaixa(10);
		for (String nome : nomes) {
			Renderer r = new Renderer(nome, renderer, caixa, answers);
			threads.put(nome, r);
		}
		
		System.out.println("JSONGenericRendererTest.teste() - start");
		for (Renderer render : threads.values()) {
			render.start();
		}
		System.out.println("JSONGenericRendererTest.teste() - started");
		
//		while(answers.keySet().size()<4){
//			System.out.println("JSONGenericRendererTest.teste() - . "+answers.keySet().size());
//			Thread.sleep(1000L);
//		}

		
		boolean acabou = false;
		while (!acabou){
			acabou = true;
			for (Renderer thread : threads.values()) {
				if(thread.isAlive()){
					acabou = false;
					Thread.sleep(1000L);
					break;
				}
			}
		}
		
		System.out.println("JSONGenericRendererTest.teste() acabou");
		for (String string : answers.keySet()) {
			System.out.println(string +" - " +answers.get(string));
		}
	}

	private Caixa criarCaixa(int niveis) {
		Caixa c = new Caixa(0);
		Caixa superCx = c; 
		for (int j = 0; j < niveis; j++) {
			Caixa subCaixa = new Caixa(j);
			superCx.setSubCaixa(subCaixa);
			superCx = subCaixa;
		}
		return c;
	}
	
	class Renderer extends Thread {
		
		String name;
		JSONGenericRenderer lapis;
		private Object obj;
		private Map answers;

		
		public Renderer(String name, JSONGenericRenderer lapis, Object cx, Map answers) {
			super();
			this.lapis = lapis;
			this.obj = cx;
			this.answers = answers;
			this.name = name;
		}

		@Override
		public void run(){
			try {
				String encode = lapis.encode(obj, null, true);
				this.answers.put(name, encode);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
