package org.futurepages.enums;

/**
 * Created by sticdev30 on 12/09/14.
 */
public enum HashType {
	MD5("MD5"), SHA1("SHA-1"), SHA256("SHA-256"), SHA512("SHA-512");
	private String descricao;

	HashType(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}
