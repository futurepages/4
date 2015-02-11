package modules.global.model.core;

import modules.global.model.entities.Local;

/**
 * Tudo que é localizado
 * 
 * @author leandro
 */
public interface Localizavel {

	public Local getLocal();
	public void setLocal(Local local);
	
}