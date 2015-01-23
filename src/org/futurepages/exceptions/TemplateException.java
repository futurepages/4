package org.futurepages.exceptions;

/**
 * Exception para o EasyTemplates. Eh uma excecao unchecked, isto e,
 * estende de RuntimeException. Isto porque nenhuma exception sera tratada:
 * todas elas serao erros de configuracao ou de execucao, devendo parar o
 * processo.
 *
 * @author Davi Luan Carneiro
 */
public class TemplateException extends RuntimeException {

	public TemplateException(String message) {
		super(message);
	}

	public TemplateException(Throwable e) {
		super(e);
	}
}
