package org.futurepages.util.brazil;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
		Double val = valueOf(moneyFormat);
		if(val!=null){
			return BigDecimal.valueOf(val).setScale(2,RoundingMode.HALF_UP);
		}else{
			return null;
		}
    }

	public static boolean areEquals(BigDecimal obj1, BigDecimal obj2){
        return moneyFormat(obj1).equals(moneyFormat(obj2));
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
		return  Integer.valueOf(moneyFormat(value.setScale(2,BigDecimal.ROUND_HALF_UP)).replaceAll("\\.","").replaceFirst(",",""));
	}


	public static BigDecimal fromIntFormat(int value){
		return new BigDecimal(value).divide(new BigDecimal(100d),2,BigDecimal.ROUND_HALF_UP).setScale(2,BigDecimal.ROUND_HALF_UP);
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

	public static String intInMoneyFormat(int i) {
		return moneyFormat(fromIntFormat(i));
	}
}