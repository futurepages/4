package org.futurepages.core.services;

import java.io.Serializable;

public interface EntityForServices<ES extends EntityServices> extends Serializable {

	public void prepareToCreate();
	public void prepareToRead();
	public void prepareToUpdate();
	public void prepareToDelete();
	public ES services();
}
