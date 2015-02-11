package org.futurepages.core.persistence.annotations;

import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.lang.annotation.Annotation;

public class TableAnnotation implements Table {

	private String name;

	public TableAnnotation(String name) {
		this.name = name;
	}

	@Override
	public Class<Table> annotationType() {
		return Table.class;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Annotation && ((Annotation) obj).annotationType()==this.annotationType();
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public String toString() {
		return Table.class.toString();
	}


	@Override
	public String name() {
		return name;
	}

	@Override
	public String catalog() {
		return "";
	}

	@Override
	public String schema() {
		return "";
	}

	@Override
	public UniqueConstraint[] uniqueConstraints() {
		return new UniqueConstraint[0];
	}
}
