package org.futurepages.util;

import org.futurepages.core.config.Apps;

import java.util.Collection;

/**
 * Classe responsável por comparações diversas com retornos lógicos (true/false).
 *
 */
public class Is {

    /**
     * Se o parâmetro é vazio ou nulo ou possui somente espaços, retorna true
     */
    public static boolean empty(Object fieldObj) {
        if (fieldObj == null) {
            return true;
        } else {
	        String nbsp = String.valueOf((char) 160); //pode ser trocado por ... String nbsp = "\u00A0";
	        if (fieldObj.toString().replaceAll(nbsp, "").trim().equals("")) {
		        return true;
	        }
	        if (fieldObj instanceof Collection) {
		        Collection collection = (Collection) fieldObj;
				if (collection.isEmpty()) {
					return true;
				}
	        }
        }
        return false;
    }

    /**
     * Retorna true se um campo numérico foi selecionado
     * ou seja, não é nem nulo e nem -1.
     */
    public static boolean selected(Long fieldName) {
        return (fieldName != null) && (fieldName > 0);
    }

    /**
     * Retorna true se um campo numérico foi selecionado
     * ou seja, não é nem nulo e nem -1.
     */
    public static boolean selected(Integer fieldName) {
        return (fieldName != null) && (fieldName > 0);
    }

    /**
     * Verifica a valiade do email dado com entrada
     * @param mailStr email de entrada
     * @return verdadeiro se o email é válido
     */
    public static boolean validMail(String mailStr) {
    	String regex = "^(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])$";
    	return mailStr.matches(regex);
    }

    /**
     * TODO altera para regex
     * @param urlStr
     */
    public static boolean validURL(String urlStr) {
        if (urlStr != null) {
            return (urlStr.contains(".") || (!Is.empty(Apps.get("APP_HOST")) && urlStr.startsWith(Apps.get("APP_HOST"))))
	            && ((urlStr.startsWith("http://") || urlStr.startsWith("https://") || urlStr.startsWith("HTTPS://") || urlStr.startsWith("HTTP://")))
	            && !urlStr.contains(" ")
            ;
        } else {
            return false;
        }
    }

    public static boolean validURLOrScript(String urlStr) {
		return urlStr != null && (urlStr.startsWith("javascript:") || validURL(urlStr));
    }

    /**
     * Testa se todos os caracteres de uma String "str" são iguais ao caracter "testStr"
     * @param str
     * @param testStr
     */
    public static boolean everyCharsLike(String str, String testStr) {
        if (testStr.length() > 1) {
            return false;
        }
        char testChar = testStr.charAt(0);
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) != testChar) {
                return false;
            }
        }
        return true;
    }

    /**
     *  Verificar se a string não possui acentuacao, nem especiais com excecao do de '_', '.', '-',' '
     */
    public static boolean validStringKey(String str) {

        return validStringKey(str, 4, 50, true);

    }

	public static boolean validStringKey(String str, Integer min, Integer max, boolean allowsInitialNumber) {

		if ((min != null && (str.length() < min)) || (max != null && (str.length() > max))) {
            return false;
        }

		String strToVerify;

		if (!allowsInitialNumber){
			strToVerify = The.stringWithoutInitialNumbers(str);
		} else{
			strToVerify = str;
		}

		//TIRA CARACTERES ESPECIAIS MANTENDO SOMENTE PONTO, UNDERLINE E HIFEN
        strToVerify = The.stringKeyIn(strToVerify);

        if (strToVerify.equalsIgnoreCase(str)) {
            return true;
        }

        return false;

    }

    // DETALHE IMPORTANTE DO MÉTODO: null == ""
    public static boolean bothValuesEquals(String str1, String str2) {
	    return (empty(str1) ? empty(str2) : str1.equals(str2));
    }

	public static boolean validCapitalizedPersonName(String in){
		if(in.equals(in.toUpperCase()) || in.equals(in.toLowerCase())){
			return false;
		}
		String[] palavras = in.split("[ |\\t]+");
		if(palavras.length==1){
			return false;
		}
		for (String palavra : palavras) {
			if(palavra.length()>1){
				String primeiraLetra = String.valueOf(palavra.charAt(0));
				String segundaLetra = String.valueOf(palavra.charAt(1));

				//vendo se é letra o primeiro caractere.
				if(primeiraLetra.toUpperCase().equals(primeiraLetra.toLowerCase())){
					return false;
				}

				if(primeiraLetra.equals(primeiraLetra.toLowerCase())
				   && (segundaLetra.equals(segundaLetra.toUpperCase())
				   && !segundaLetra.equals("'") && !segundaLetra.equals("`") && !segundaLetra.equals("’")) ){
					return false;
				}

				if(palavra.length()>3){
					if(primeiraLetra.equals(primeiraLetra.toLowerCase())
					&& !palavra.contains("'") && !palavra.contains("`") && !palavra.contains("’")){
						return false;
					}
				}
			}
			else if(palavra.equalsIgnoreCase("e")){
				if(palavra.equals("E")){
					return false;
				}
			}

			if(palavra.equalsIgnoreCase("de")
			|| palavra.equalsIgnoreCase("do")
			|| palavra.equalsIgnoreCase("da")
			|| palavra.equalsIgnoreCase("dos")
			|| palavra.equalsIgnoreCase("das")
			|| palavra.equalsIgnoreCase("e")
			){
				if(!palavra.equals(palavra.toLowerCase())){
					return false;
				}
			}else if(
					 palavra.equalsIgnoreCase("I")
				  || palavra.equalsIgnoreCase("II")
				  || palavra.equalsIgnoreCase("III")
				  || palavra.equalsIgnoreCase("IV")
				  || palavra.equalsIgnoreCase("V")
				  || palavra.equalsIgnoreCase("VI")
				  || palavra.equalsIgnoreCase("VII")
				  || palavra.equalsIgnoreCase("VIII")
				  || palavra.equalsIgnoreCase("IX")
				  || palavra.equalsIgnoreCase("X")
				  || palavra.equalsIgnoreCase("XI")
				  || palavra.equalsIgnoreCase("XII")
				  || palavra.equalsIgnoreCase("XIII")
				  || palavra.equalsIgnoreCase("XIV")
				  || palavra.equalsIgnoreCase("XV")
				  || palavra.equalsIgnoreCase("XVI")
				  || palavra.equalsIgnoreCase("XVII")
				  || palavra.equalsIgnoreCase("XVII")
				  || palavra.equalsIgnoreCase("XVIII")
				  || palavra.equalsIgnoreCase("XIX")
				  || palavra.equalsIgnoreCase("XX")
				  || palavra.equalsIgnoreCase("XXI")
				  || palavra.equalsIgnoreCase("XXII")
				  || palavra.equalsIgnoreCase("XXIII")
				  || palavra.equalsIgnoreCase("XXIV")
				  || palavra.equalsIgnoreCase("XXV")
				  || palavra.equalsIgnoreCase("XXVI")
				  || palavra.equalsIgnoreCase("XXVII")
				  || palavra.equalsIgnoreCase("XXVIII")
				  || palavra.equalsIgnoreCase("XXVIX")
				  || palavra.equalsIgnoreCase("XXX")
				  //TODO seria interessante os demais??
			){
				if(!palavra.equals(palavra.toUpperCase())){
					return false;
				}
			}else{
				if(palavra.length()==2 && !The.capitalizedWord(palavra).equals(palavra)){
					if(palavra.equals("dI") || !palavra.equalsIgnoreCase("di")){
						return false;
					}
				}
				if(palavra.toUpperCase().equals(palavra)){
					return false;
				}else{
						if(palavra.contains("'") || palavra.contains("`") || palavra.contains("’")){
							if(palavra.length()>3){
								for(int i = 3; i<palavra.length();i++){
									String letra = String.valueOf(palavra.charAt(i));
									if(letra.toUpperCase().equals(letra)){
										return false;
									}
								}
							}else{ //3 caracteres com aspostofo, so pode no meio
								String letra = String.valueOf(palavra.charAt(2));
								if(letra.toUpperCase().equals(letra)){
									return false;
								}
								letra = String.valueOf(palavra.charAt(0));

								if(!letra.toUpperCase().equals(letra)){
									return false;
								}
							}
						}else if(palavra.length()>2){
							for(int i = 2; i<palavra.length();i++){
								String letra = String.valueOf(palavra.charAt(i));
								if(letra.toUpperCase().equals(letra)){
									return false;
								}
						}
					}
				}
			}
		}
		return true;
	}

	public static boolean NaN(String str) {
		try{
			double d = Double.valueOf(str);
			return Double.isNaN(d);
		}catch(Exception ex){
			return true;
		}
	}

//	public static void testNaN(String[] args) {

//		Resultados false
//		System.out.println(NaN("1.79769313486231570e+308"));
//		System.out.println(NaN("1"));
//		System.out.println(NaN("2"));
//		System.out.println(NaN("1.03"));
//		System.out.println(NaN("01.03"));
//		System.out.println(NaN("1234322493249234"));

//		Resultados true
//		System.out.println(NaN(""));
//		System.out.println(NaN("abc"));
//		System.out.println(NaN("1,3d,d3"));
//		System.out.println(NaN("1,3d"));
//	}
}