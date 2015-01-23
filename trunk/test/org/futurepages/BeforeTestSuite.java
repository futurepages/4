package org.futurepages;

import org.futurepages.core.persistence.SchemaGeneration;
import org.junit.Test;

public class BeforeTestSuite {

	@Test
	public void setUp() throws Exception{
		exportDatabase();
	}

	private void exportDatabase() throws Exception {
		SchemaGeneration.export();
	}
}