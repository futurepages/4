package org.futurepages.core.input;

import static org.junit.Assert.*;

import modules.escola.beans.Aluno;
import modules.escola.beans.Turma;

import org.easymock.classextension.EasyMock;
import org.futurepages.testutil.AssertUtil;
import org.junit.Before;
import org.junit.Test;

public class InputHunterTest {

	InputHunter hunter;
	Input input;
	@Before
	public void setUp() throws Exception {
		input = EasyMock.createMock(Input.class);
	}

	//	      EasyMock.expect(source.read()).andThrow(new RuntimeException("Erro na leitura"));
	//	      EasyMock.replay(source, destination);
//	EasyMock.expect(input.getValue("aluno")).andReturn(null);
//	EasyMock.expect(input.getValue("turma")).andReturn(null);
//	EasyMock.expect(input.getValue("nome")).andReturn(null);
	
	private void getValuesTestProcedure(String path, String msg, Object[] expected) {
		hunter = new InputHunter(path);
		Object[] result = hunter.getValues(input);
		AssertUtil.assertArrayEquals(msg, expected, result);
	}
	
	@Test
	public final void testGetValues_inputVazio() {
		EasyMock.expect(input.getValue("aluno")).andReturn(null);
		EasyMock.replay(input);

		String path = "aluno.turma.nome";
		hunter = new InputHunter(path);
		Object[] result = hunter.getValues(input);
		AssertUtil.assertArrayEquals("Erro quando não há nenhum valor no input", new Object[3], result);
	}
	
	@Test
	public final void testGetValues_naoVazio_elementoComAtributosNulos() {
		Aluno aluno = new Aluno();
		EasyMock.expect(input.getValue("aluno")).andReturn(aluno);
		EasyMock.replay(input);
		String path = "aluno.turma.nome";
		String msg = "Erro quando não há elemento com atributo nulo";
		Object[] expected = new Object[]{aluno, null, null};
		getValuesTestProcedure(path, msg, expected);
	}

	@Test
	public final void testGetValues_naoVazio_elementoComAtributosNaoNulos() {
		Aluno aluno = new Aluno();
		Turma turma = new Turma();
		aluno.setTurma(turma);
		turma.setNome("turmaA");
		
		EasyMock.expect(input.getValue("aluno")).andReturn(aluno);
		EasyMock.replay(input);
		String path = "aluno.turma.nome";
		String msg = "Erro quando os atributos não são nulos";
		Object[] expected = new Object[]{aluno, aluno.getTurma(), "turmaA"};
		getValuesTestProcedure(path, msg, expected);
	}

	@Test
	public final void testGetValues_pathInexistente() {
		Aluno aluno = new Aluno();
		Turma turma = new Turma();
		aluno.setTurma(turma);
		turma.setNome("turmaA");
		
		EasyMock.expect(input.getValue("aluno")).andReturn(aluno);
		EasyMock.replay(input);
		String path = "aluno.turma.nome.turma";
		String msg = "Erro quando os atributos não são nulos";
		Object[] expected = new Object[]{aluno, aluno.getTurma(), "turmaA", null};
		getValuesTestProcedure(path, msg, expected);
	}
}
