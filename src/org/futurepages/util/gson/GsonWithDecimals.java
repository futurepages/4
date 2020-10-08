package org.futurepages.util.gson;

import com.google.gson.GsonBuilder;
import org.futurepages.formatters.FloatFormatter;
import org.futurepages.menta.util.InjectionUtils;
import org.futurepages.util.brazil.MoneyUtil;
import org.futurepages.util.gson.core.GsonRegister;

import java.math.BigDecimal;

public class GsonWithDecimals {


	public static void registerFor(GsonBuilder gb){
		new GsonRegister<BigDecimal>(BigDecimal.class, gb) {
			@Override
			public BigDecimal fromJson(String jsonStr) {
				return new BigDecimal(InjectionUtils.realToFloat(jsonStr));
			}

			@Override
			public String toJson(BigDecimal data) {
				return MoneyUtil.moneyFormat(data).replace(",",".");
			}
		};

		new GsonRegister<Float>(Float.class, gb) {
			@Override
			public Float fromJson(String jsonStr) {
				return InjectionUtils.realToFloat(jsonStr);
			}

			@Override
			public String toJson(Float data) {
				return new FloatFormatter().format(data, "#.##############################").replace(",",".");
			}
		};


		new GsonRegister<Double>(Double.class, gb) {
			@Override
			public Double fromJson(String jsonStr) {
				return InjectionUtils.realToDouble(jsonStr);
			}

			@Override
			public String toJson(Double data) {
				return new FloatFormatter().format(data, "#.##############################").replace(",",".");
			}
		};
	}
}