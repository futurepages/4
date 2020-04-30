package org.futurepages.formatters.brazil;

import org.futurepages.util.brazil.CNPJUtil;
import org.futurepages.util.brazil.CPFUtil;
import java.util.Locale;
import org.futurepages.core.formatter.AbstractFormatter;
 
 public class CPFCNPJFormatter extends AbstractFormatter {
 	
 	public String format(Object value, Locale loc) {
 		return format((String) value);
 	}

	public static String format(String cpfCnpj){
		if(cpfCnpj!=null){
			if(cpfCnpj.length() == CPFUtil.QUANTIDADE_DIGITOS_CPF){
				return CPFUtil.formata(cpfCnpj);
			}
			else if(cpfCnpj.length() == CNPJUtil.QUANTIDADE_DIGITOS_CNPJ){
				return CNPJUtil.formata(cpfCnpj);
			}
		}
		return cpfCnpj;
	}
 }