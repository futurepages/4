package org.futurepages.filters;

import modules.escola.actions.AlunoActions;
import modules.escola.beans.Aluno;
import modules.escola.beans.Turma;

import org.futurepages.core.control.InvocationChain;
import org.futurepages.core.input.Input;
import org.futurepages.core.input.MapInput;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PersistenceInjectionFilterTest {

	private PersistenceInjectionFilter filter;
	private InvocationChain chain;
	private PersistenceInjectionFilterTestPopulator populator;
	
	
	@Before
	public void setUp(){
		AlunoActions action = new AlunoActions();
		action.setInput(new MapInput());
		chain = new InvocationChain("execute", action);
	}
	
	private String filterTestProcedure() throws Exception{
		String retorno = filter.filter(chain);
		return retorno;
	}
	
	
	/**
	 * ("aluno","alunoId",Aluno.class);
	 * @throws Exception 
	 */
	@Test
	public void testFilter_semKeyNoInput() throws Exception{
		Aluno alunoBD = populator.criarUmAluno();
		Input input = chain.getAction().getInput();
		
		filter = new PersistenceInjectionFilter("aluno","alunoId",Aluno.class);
		filterTestProcedure();
		
		Aluno aluno = (Aluno) input.getValue("aluno");
		Assert.assertNull("Aluno não foi injetado no input",aluno);		
	}
	
	/**
	 * ("aluno","alunoId",Aluno.class);
	 * @throws Exception 
	 */
	@Test
	public void testFilter_semCaminho_comId() throws Exception{
		Aluno alunoBD = populator.criarUmAluno();
		
		Input input = chain.getAction().getInput();
		input.setValue("alunoId", alunoBD.getId());
		
		filter = new PersistenceInjectionFilter("aluno","alunoId",Aluno.class);
		filterTestProcedure();
		
		Aluno aluno = (Aluno) input.getValue("aluno");
		Assert.assertNotNull("Aluno não foi injetado no input",aluno);
		Assert.assertEquals("Aluno diferente do aluno injetado.",alunoBD.getId(), aluno.getId());
		
	}
	
	/**
	 * ("aluno",Aluno.class);
	 * @throws Exception 
	 */
	@Test
	public void testFilter_substituicaoInput() throws Exception{
		Aluno alunoBD = populator.criarUmAluno();
		
		Input input = chain.getAction().getInput();
		input.setValue("aluno", alunoBD.getId());
		
		filter = new PersistenceInjectionFilter("aluno",Aluno.class);
		filterTestProcedure();
		
		Aluno aluno = (Aluno) input.getValue("aluno");
		Assert.assertNotNull("Aluno não foi injetado no input",aluno);
		Assert.assertEquals("Aluno diferente do aluno injetado.",alunoBD.getId(), aluno.getId());
		
	}
	
	/**
	 * ("aluno.turma",Turma.class);
	 * @throws Exception 
	 */
	@Test
	public void testFilter_comCaminho_semId() throws Exception{
		Input input = chain.getAction().getInput();
		
		Turma t = populator.criarTurma();
		input.setValue("turma", t.getId());
		
		Aluno alunoBD = populator.criarUmAluno();
		input.setValue("aluno", alunoBD);
		
		filter = new PersistenceInjectionFilter("aluno.turma",Turma.class);
		filterTestProcedure();
		
		Aluno aluno = (Aluno) input.getValue("aluno");
		Assert.assertNotNull("Aluno não foi injetado no input, teste não faz sentido",aluno);
		Assert.assertNotNull("a Turma não foi injetado no aluno.",aluno.getTurma());
		
	}

	/**
	 * ("aluno.turma","turmaId",Turma.class);
	 * @throws Exception 
	 */
	@Test
	public void testFilter_comCaminho_comId() throws Exception{
		Input input = chain.getAction().getInput();
		
		Turma t = populator.criarTurma();
		input.setValue("turmaId", t.getId());
		
		Aluno alunoBD = populator.criarUmAluno();
		input.setValue("aluno", alunoBD);
		
		filter = new PersistenceInjectionFilter("aluno.turma","turmaId",Turma.class);
		filterTestProcedure();
		
		Aluno aluno = (Aluno) input.getValue("aluno");
		Assert.assertNotNull("Aluno não foi injetado no input, teste não faz sentido",aluno);
		Assert.assertNotNull("a Turma não foi injetado no aluno.",aluno.getTurma());
	}

	
	
}
