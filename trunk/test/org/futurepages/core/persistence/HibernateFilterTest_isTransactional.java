package org.futurepages.core.persistence;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import javax.servlet.ServletException;

import org.futurepages.core.action.Action;
import org.futurepages.core.control.InvocationChain;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Classe para teste de {@link HibernateFilter#isTransactional(Method)}
 * @author Danilo Medeiros
 */
@RunWith(Parameterized.class)
public class HibernateFilterTest_isTransactional {

	String caso;
	Class<Action> actionClass;
	String nomeMetodo;
	Boolean esperado;

	public HibernateFilterTest_isTransactional(String caso, Class<Action> klass, String nomeMetodo,	Boolean esperado) {
		super();
		this.caso = caso;
		this.actionClass = klass;
		this.nomeMetodo = nomeMetodo;
		this.esperado = esperado;
	}

	@Parameters
	public static Collection<Object[]> parameters() {
		Collection<Object[]> col =  Arrays.asList(new Object[][] {
				{"Classe Transactional, metodo com as duas anotações.",Transacional.class, 	"biTransacional",	false},
				{"Classe Transactional, método com "+ "@Transactional", Transacional.class, 	"transacional",		true},
				{"Classe Transactional, método com @NonTransactional.", Transacional.class, 	"naoTransacional",	false},
				{"Classe Transactional, método sem anotações.", Transacional.class, 	"metodo",			true},
				{"Classe Não anotada com @Transactional, método com as duas anotações.", NaoTransacional.class, "biTransacional",	false},
				{"Classe Não anotada com @Transactional, método com @Transactional.", NaoTransacional.class, "transacional",		true},
				{"Classe Não anotada com @Transactional, método com @NonTransactional.", NaoTransacional.class, "naoTransacional",	false},
				{"Classe Não anotada com @Transactional, método sem anotações.", NaoTransacional.class, "metodo",			false}
		});
		return col;
	}

	@Test
	public void testeIsTransational() throws SecurityException, NoSuchMethodException, InstantiationException, IllegalAccessException, ServletException{
		HibernateFilter filter = new HibernateFilter();
		
		Action action = actionClass.newInstance();
		InvocationChain chain = new InvocationChain("Cadeia", action);
		chain.setInnerAction(nomeMetodo);
		Boolean result = filter.isTransactional(chain);
		Assert.assertEquals(caso,esperado, result);
	}

}