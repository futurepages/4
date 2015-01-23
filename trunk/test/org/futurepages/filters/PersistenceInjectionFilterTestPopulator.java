package org.futurepages.filters;

import modules.escola.beans.Aluno;
import modules.escola.beans.Turma;

import org.futurepages.core.persistence.Dao;

public class PersistenceInjectionFilterTestPopulator {

	public static Turma criarTurma() {
		Turma a = new Turma();
		Dao.save(a);
		return a;
	}

	public static Aluno criarUmAluno() {
		Aluno a = new Aluno();
		Dao.save(a);
		return a;
	}
}
