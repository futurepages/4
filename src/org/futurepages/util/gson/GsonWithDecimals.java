package org.futurepages.util.gson;

import com.google.gson.GsonBuilder;
import org.futurepages.menta.util.InjectionUtils;
import org.futurepages.util.gson.core.GsonRegister;

import java.math.BigDecimal;

public class GsonWithDecimals {


	public static void registerFor(GsonBuilder gb){
		new GsonRegister<BigDecimal>(BigDecimal.class, gb, false) {
			@Override
			public BigDecimal fromJson(String jsonStr) {
				return InjectionUtils.realToBigDecimal(jsonStr);
			}
		};

		new GsonRegister<Float>(Float.class, gb, false) {
			@Override
			public Float fromJson(String jsonStr) {
				return InjectionUtils.realToFloat(jsonStr);
			}
		};


		new GsonRegister<Double>(Double.class, gb, false) {
			@Override
			public Double fromJson(String jsonStr) {
				return InjectionUtils.realToDouble(jsonStr);
			}
		};
	}
}