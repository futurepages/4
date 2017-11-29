package org.futurepages.util.brazil;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * Rotinas úteis para manipulação de valores monetários.
 * @author leandro
 */
public class MoneyUtil {
    
    /**
     * Recebe como entrada uma string do tipo 9.999,99 e retorna um double
     *
     */
    public static Double valueOf(String moneyFormat){
        try{
            moneyFormat = moneyFormat.replace(".","");
            moneyFormat = moneyFormat.replace(",",".");
            return Double.parseDouble(moneyFormat);
        }
        catch(Exception ex){
            return null;
        }
    }

	/**
     * Recebe como entrada uma string do tipo 9.999,99 e retorna um BigDecimal
     *
     */
    public static BigDecimal valueBigDecimalOf(String moneyFormat){
            return BigDecimal.valueOf(valueOf(moneyFormat));
    }

    /**
     * Recebe um double como entrada e converte para uma string no formato 9.999,99 (duas casas decimais)
     *
     */    
    public static String moneyFormat(Double value){
	   return moneyFormat(value,2);
    }

	/**
     * Recebe um double como entrada e converte para uma string no formato 9.999,99 (duas casas decimais)
     *
     */
    public static String moneyFormat(BigDecimal value){
	   return moneyFormat(value.doubleValue(),2);
    }


	public static int intFormat(BigDecimal value){
		return  Integer.valueOf(moneyFormat(value).replaceFirst(",",""));
	}


    /**
     * Recebe um double como entrada e converte para a formataçao padrao de número (NumberFormat) do sistema.
     * com o número de casas decimais especificadas através do parâmetro 'decimals'
     *
     */
    public static String moneyFormat(Double value, Integer decimals){
	   if(value == null){
		return "";
	   }
	   else{
		   NumberFormat  nf = NumberFormat.getNumberInstance();
		   if(decimals != null){
               nf.setMaximumFractionDigits(decimals);
               nf.setMinimumFractionDigits(decimals);
           }
		   String money = nf.format(value);
		   return money;
	   }
    }
	
	/**
	 * Retorna valor double com duas casas arredondadas para cima.
	 * 34.550001 => 34.56
	 * @param rawValue
	 */
	public static double ceilValue(Double rawValue){
		return Math.ceil((rawValue)*100)/100;
	}

	/**
	 * Retorna valor double com duas casas arredondadas para baixo.
	 * 34.559999 => 34.55 
	 * @param rawValue
	 */
	public static double floorValue(Double rawValue){
		return Math.floor((rawValue)*100)/100;
	}

	/**
	 * Retorna valor double com duas casas arredondadas.
	 * @param rawValue
	 */
	public static double roundValue(Double rawValue){
		return  ((double) Math.round((rawValue)*100)) / 100;
	}

    /**
	 * Retorna valor double com duas casas arredondadas.
	 * @param rawValue
	 */
	public static double roundValue(String rawValue){
		return  ((double) Math.round((valueOf(rawValue))*100)) / 100;
	}
}