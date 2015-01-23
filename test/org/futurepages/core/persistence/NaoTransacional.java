package org.futurepages.core.persistence;

import org.futurepages.actions.CrudActions;
import org.futurepages.annotations.NonTransactional;
import org.futurepages.annotations.Transactional;

public class NaoTransacional extends CrudActions{
	public void metodo(){

	}

	@NonTransactional
	public void naoTransacional(){

	}
	@Transactional
	public void transacional(){

	}
	
	@Transactional
	@NonTransactional
	public void biTransacional(){

	}
}

