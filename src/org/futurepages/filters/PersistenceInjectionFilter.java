package org.futurepages.filters;

import java.io.Serializable;

import org.futurepages.core.control.InvocationChain;
import org.futurepages.core.filter.Filter;
import org.futurepages.core.input.Input;
import org.futurepages.core.input.InputHunter;
import org.futurepages.core.persistence.Dao;
import org.futurepages.util.ReflectionUtil;
import org.futurepages.util.The;

/**
 * Filtro de injeção de dependência de elementos persistentes.
 * <br>
 * O filtro realiza uma busca no banco e injeta o elemento buscado em outro objeto ou com uma chave no input.
 * <br>   
 * Ex: 
 * <li>("aluno",Aluno.class)
 * 	<br>busca um objeto Aluno na base de dados com um id representado pelo valor de input.getValue("aluno"). 
 * 	<br>O objeto Aluno retornado na base é injetado no input para a chave "aluno"
 * <br><br>    
 * <li>("aluno.turma",Turma.class):
 * 	<br>busca um objeto Turma na base de dados com um id representado pelo valor de input.getValue("turma"). 
 * 	<br>O objeto Turma retornado na base é injetado no objeto Aluno obtido por input.getValue("aluno")
 * <br><br>   
 * <li>("aluno.turma","turmaId",Turma.class)
 *  <br>busca um objeto Turma na base de dados com um id representado pelo valor de input.getValue("turmaId").
 * 	<br>O objeto Turma retornado na base é injetado no objeto Aluno obtido por input.getValue("aluno")
 * <br><br>
 *
 * //TODO Outros casos a implemtar
 * <li>("preVinculado.manifestacaoPrincipal","manifestacaoPrincipalId",Manifestacao.class)
 * <li>("preVinculado.manifestacaoPrincipal","manifestacaoPrincipalProtocolo","protocolo",Manifestacao.class)
 *
 * @author Danilo Medeiros
 */
public class PersistenceInjectionFilter implements Filter{

	private String idKey;
	private Class<? extends Serializable> classe;
	private String[] tokenedPath;

	public PersistenceInjectionFilter(String path, Class<? extends Serializable> classe) {
		this(path, null, classe);
	}

	public PersistenceInjectionFilter(String path, String idKey, Class<? extends Serializable> classe) {
		this.idKey = idKey;
		this.classe = classe;
		tokenedPath = The.explodedToArray(path, ".");
	}

	@Override
	public String filter(InvocationChain chain) throws Exception {
		Input input = chain.getAction().getInput();
		Object injecao = getInjecao(input);
		inject(injecao, input);
		return chain.invoke();
	}

	/**
	 * Se idKey é null, o idKey será o último token do tokenedPath
	 * @param input
	 * @return retorna um objeto da entidade 'classe', para o identificador 'value' para a chave 'idKey' no input.
	 */
	private Object getInjecao(Input input) {
		if(idKey == null){
			idKey = tokenedPath[tokenedPath.length-1];
		}
		Serializable keyValue = getInputFromKey(input, idKey);
		Object obj = null;
		if(keyValue!= null){
			obj = Dao.get(classe, keyValue);
		}
		return obj;
	}

	private Serializable getInputFromKey(Input input, String idKey) {
		Class<?> pkType = Dao.getIdType(classe);
		return InputHunter.getInputFromKey(input, pkType, idKey);
	}

	private void inject(Object injecao, Input input) {

		int size = tokenedPath.length;
		String nomeDoEnte = tokenedPath[0];
		if(size == 1){
			input.setValue(nomeDoEnte, injecao);
		}else{
			Object doente = input.getValue(nomeDoEnte);
			if(doente !=  null){
				String nomeParteCorpo = tokenedPath[tokenedPath.length-1];
				ReflectionUtil.setField(doente, nomeParteCorpo, injecao);
			}
		}
	}

	@Override
	public void destroy() {
	}

}
