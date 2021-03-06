package org.futurepages.util.gson;

import com.google.gson.GsonBuilder;
import org.futurepages.menta.util.InjectionUtils;
import org.futurepages.util.Is;
import org.futurepages.util.gson.core.GsonRegister;

import java.math.BigDecimal;

public class GsonWithDecimals {


	public static void registerFor(GsonBuilder gb,boolean defaultInputFloat){
		new GsonRegister<BigDecimal>(BigDecimal.class, gb, false) {
			@Override
			public BigDecimal fromJson(String jsonStr) {
				if(Is.empty(jsonStr)){
					return null;
				}
				if(defaultInputFloat){
					return new BigDecimal(jsonStr); // str default
				}else{
					return InjectionUtils.realToBigDecimal(jsonStr); // str com format br
				}
			}
		};

		new GsonRegister<Float>(Float.class, gb, false) {
			@Override
			public Float fromJson(String jsonStr) {
				if(Is.empty(jsonStr)){
					return null;
				}
				if(defaultInputFloat){
					return Float.valueOf(jsonStr); // str default
				}else{
					return InjectionUtils.realToFloat(jsonStr); // str com format br
				}
			}
		};


		new GsonRegister<Double>(Double.class, gb, false) {
			@Override
			public Double fromJson(String jsonStr) {
				if(Is.empty(jsonStr)){
					return null;
				}
				if(defaultInputFloat){
					return Double.valueOf(jsonStr); // str default
				}else{
					return InjectionUtils.realToDouble(jsonStr); // str com format br
				}
			}
		};
	}
}