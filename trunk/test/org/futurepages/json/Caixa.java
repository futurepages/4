package org.futurepages.json;

import java.io.Serializable;

public class Caixa implements Serializable{
	
	public int nivel;

	public Caixa subCaixa;
	
	public Caixa() {
		super();
	}

	public Caixa(int nivel) {
		this.nivel = nivel;
	}

	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	public Caixa getSubCaixa() {
		return subCaixa;
	}

	public void setSubCaixa(Caixa subCaixa) {
		this.subCaixa = subCaixa;
	}
	
}
