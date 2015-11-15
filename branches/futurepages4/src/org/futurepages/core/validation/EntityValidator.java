package org.futurepages.core.validation;

import org.futurepages.core.services.EntityServices;

import java.io.Serializable;

public abstract class EntityValidator<SERVICES extends EntityServices,BEAN extends Serializable> extends ServiceValidator<SERVICES> {

	public abstract void create(BEAN bean);
	public abstract void read(BEAN bean);
	public abstract void update(BEAN bean);
	public abstract void delete(BEAN bean);
}