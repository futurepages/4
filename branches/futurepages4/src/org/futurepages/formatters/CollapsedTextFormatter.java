package org.futurepages.formatters;

import java.util.Locale;
import org.futurepages.core.formatter.AbstractFormatter;

/**
 *
 * @author jlrterceiro
 */
public class CollapsedTextFormatter extends AbstractFormatter<String> {

	@Override
	public String format(String texto, Locale loc) {
		return format(texto, loc, "40,true");
	}

	@Override
	public String format(String texto, Locale loc, String param) {
		String []vet = param.split(",");
		int tam=40;
		boolean cortaPalavras=true;
		if (vet.length>0) {
			String tamStr=vet[0];
			if (tamStr!=null && !tamStr.isEmpty()) {
				try {
					tam=Integer.parseInt(tamStr);
				}
				catch (NumberFormatException e) {
					if (vet.length==1) {
						String cortaPalavrasStr = vet[1];
						if (cortaPalavrasStr.equalsIgnoreCase("false")) {
							cortaPalavras=false;
						}
					}
					else {
						throw e;
					}
				}
			}
			if (vet.length>1) {
				String cortaPalavrasStr = vet[1];
				if (cortaPalavrasStr.equalsIgnoreCase("false")) {
					cortaPalavras=false;
				}
			}
		}
		if (cortaPalavras==true) {
			return formatCortandoPalavras(texto, loc, tam);
		}
		else {
			return formatNaoCortandoPalavras(texto, loc, tam);
		}
	}

	private String formatCortandoPalavras(String texto, Locale loc, int tam) {
		String novoTexto;
		if (texto.length()>tam && tam>=20) {
			int tamInferior = (tam - 3)/2;
			int tamSuperior = (tam - 3)/2;
			if ((tam - 3)%2==1) {
				tamInferior++;
			}
			int inicioSuperior=texto.length()-tamSuperior;
			String metadeInferior = texto.substring(0, tamInferior);
			String metadeSuperior = texto.substring(inicioSuperior);
			novoTexto = metadeInferior + "..." + metadeSuperior;
		}
		else {
			novoTexto = texto;
		}
		return novoTexto;
	}

	private String formatNaoCortandoPalavras(String texto, Locale loc, int tam) {
		String novoTexto;
		if (texto.length()>tam && tam>=20) {
			int tamInferior = (tam - 3)/2;
			int tamSuperior = (tam - 3)/2;
			if ((tam - 3)%2==1) {
				tamInferior++;
			}
			int i;
			for (i=tamInferior-1; i>=0; i--) {
				if (Character.isWhitespace(texto.charAt(i))) {
					break;
				}
			}
			tamInferior=i+1;
			int inicioSuperior=texto.length()-tamSuperior;
			for (i=inicioSuperior; i<texto.length(); i++) {
				if (Character.isWhitespace(texto.charAt(i))) {
					break;
				}
			}
			inicioSuperior = i;
			String metadeInferior = texto.substring(0, tamInferior);
			String metadeSuperior = texto.substring(inicioSuperior);
			novoTexto = metadeInferior + "..." + metadeSuperior;
		}
		else {
			novoTexto = texto;
		}
		return novoTexto;
	}


}
