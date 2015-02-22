package org.futurepages.core.services;

public interface EntityForServices<ES extends EntityServices> {

	public void prepareToCreate();
	public void prepareToRead();
	public void prepareToUpdate();
	public void prepareToDelete();
	public ES services();
}
