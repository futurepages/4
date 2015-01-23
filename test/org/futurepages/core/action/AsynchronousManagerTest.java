package org.futurepages.core.action;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.futurepages.core.control.InvocationChain;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class AsynchronousManagerTest {
	private ActionImpl actions;

	@Before
	public void setUp(){
		actions = new ActionImpl();
	}

	@Test
	public void isDynActionDeveSer_True_QuandoActionImplementar_DynAction(){

		InvocationChain chain = mock(InvocationChain.class);
		when(chain.getAction()).thenReturn(actions.new ClasseDynImplements());

		boolean result = AsynchronousManager.isDynAction(chain);
		Assert.assertTrue("Deve ser true quando action implementar DynAction", result);
	}

	@Test
	public void isDynActionDeveSer_True_QuandoActionPossuirAnnotation_AsynchronousAction_DYN(){
		InvocationChain chain = mock(InvocationChain.class);
		when(chain.getAction()).thenReturn(actions.new ClasseDynAnotada());

		boolean result = AsynchronousManager.isDynAction(chain);
		Assert.assertTrue("Deve ser true quando action possuir AsynchronousAction(DYN)", result);
	}

	@Test
	public void isDynActionDeveSer_True_QuandoInnerActionPossuirAnnotation_AsynchronousAction_DYN(){
		InvocationChain chain = mock(InvocationChain.class);
		when(chain.getAction()).thenReturn(actions.new ClasseInnerActionDynAnotada());
		when(chain.getInnerAction()).thenReturn("action");
		when(chain.getMethod()).thenCallRealMethod();

		boolean result = AsynchronousManager.isDynAction(chain);
		Assert.assertTrue("Deve ser true quando innter action possuir AsynchronousAction(DYN)", result);
	}

	@Test
	public void isDynActionDeveSer_False_QuandoInnerActionNaoPossuirAnnotation_AsynchronousAction(){

		InvocationChain chain = mock(InvocationChain.class);
		when(chain.getAction()).thenReturn(actions.new ClasseInnerActionDynNaoAnotada());
		when(chain.getInnerAction()).thenReturn("action");
		when(chain.getMethod()).thenCallRealMethod();

		boolean result = AsynchronousManager.isDynAction(chain);
		Assert.assertFalse("Deve ser true quando innte não action possuir AsynchronousAction(DYN)", result);
	}

	@Test
	public void isDynActionDeveSer_False_QuandoActionNaoImplementar_DynAction_NemPossuirAnnotation(){
		InvocationChain chain = mock(InvocationChain.class);
		when(chain.getAction()).thenReturn(actions.new ClasseNaoAsynchronousAction());
		when(chain.getInnerAction()).thenReturn("action");
		when(chain.getMethod()).thenCallRealMethod();

		boolean result = AsynchronousManager.isDynAction(chain);
		Assert.assertFalse("Deve ser true quando a action não implementar DynAction", result);
	}
	
	@Test
	public void isAjaxActionDeveSer_True_QuandoActionImplementar_AjaxAction(){
		
		InvocationChain chain = mock(InvocationChain.class);
		when(chain.getAction()).thenReturn(actions.new ClasseAjaxImplements());
		
		boolean result = AsynchronousManager.isAjaxAction(chain);
		Assert.assertTrue("Deve ser true quando action implementar AjaxAction", result);
	}
	
	@Test
	public void isAjaxActionDeveSer_True_QuandoActionPossuirAnnotation_AsynchronousAction_AJAX(){
		InvocationChain chain = mock(InvocationChain.class);
		when(chain.getAction()).thenReturn(actions.new ClasseAjaxAnotada());
		
		boolean result = AsynchronousManager.isAjaxAction(chain);
		Assert.assertTrue("Deve ser true quando action possuir AsynchronousAction(AJAX)", result);
	}
	
	@Test
	public void isAjaxActionDeveSer_True_QuandoInnerActionPossuirAnnotation_AsynchronousAction_AJAX(){
		InvocationChain chain = mock(InvocationChain.class);
		when(chain.getAction()).thenReturn(actions.new ClasseInnerActionAjaxAnotada());
		when(chain.getInnerAction()).thenReturn("action");
		when(chain.getMethod()).thenCallRealMethod();
		
		boolean result = AsynchronousManager.isAjaxAction(chain);
		Assert.assertTrue("Deve ser true quando innter action possuir AsynchronousAction(AJAX)", result);
	}
	
	@Test
	public void isAjaxActionDeveSer_False_QuandoInnerActionNaoPossuirAnnotation_AsynchronousAction(){
		
		InvocationChain chain = mock(InvocationChain.class);
		when(chain.getAction()).thenReturn(actions.new ClasseInnerActionAjaxNaoAnotada());
		when(chain.getInnerAction()).thenReturn("action");
		when(chain.getMethod()).thenCallRealMethod();
		
		boolean result = AsynchronousManager.isAjaxAction(chain);
		Assert.assertFalse("Deve ser true quando innte não action possuir AsynchronousAction(AJAX)", result);
	}
	
	@Test
	public void isAjaxActionDeveSer_False_QuandoActionNaoImplementar_AjaxAction_NemPossuirAnnotation(){
		InvocationChain chain = mock(InvocationChain.class);
		when(chain.getAction()).thenReturn(actions.new ClasseNaoAsynchronousAction());
		when(chain.getInnerAction()).thenReturn("action");
		when(chain.getMethod()).thenCallRealMethod();
		
		boolean result = AsynchronousManager.isAjaxAction(chain);
		Assert.assertFalse("Deve ser true quando a action não implementar AjaxAction", result);
	}
	
	@Test
	public void isAjaxActionDeveSer_False_QuandoActionImplementar_AjaxAction_MetodoDyn(){
		InvocationChain chain = mock(InvocationChain.class);
		when(chain.getAction()).thenReturn(actions.new ClasseAjaxImplements_metodoDyn());
		when(chain.getInnerAction()).thenReturn("action");
		when(chain.getMethod()).thenCallRealMethod();
		
		boolean result = AsynchronousManager.isAjaxAction(chain);
		Assert.assertFalse("idAjax deve ser false quando a action implementar AjaxAction, mas o método sobrescrever " +
				"anotando com Dyn ", result);
	}
	
	@Test
	public void isDynActionDeveSer_False_QuandoActionImplementar_DynAction_MetodoAjaxs(){
		InvocationChain chain = mock(InvocationChain.class);
		when(chain.getAction()).thenReturn(actions.new ClasseDynImplements_metodoAjax());
		when(chain.getInnerAction()).thenReturn("action");
		when(chain.getMethod()).thenCallRealMethod();
		
		boolean result = AsynchronousManager.isDynAction(chain);
		Assert.assertFalse("isDyn deve ser false quando a action implementar DynAction, mas o método sobrescrever " +
				"anotando com Ajax ", result);
	}
}
