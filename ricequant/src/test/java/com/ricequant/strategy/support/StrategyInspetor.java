package com.ricequant.strategy.support;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.ricequant.strategy.def.IHStrategy;

public class StrategyInspetor {

	public static Map<String, Object> showAttributes(IHStrategy strategy) {
		Map<String, Object> result = new HashMap<String, Object>();

		try {
			Field[] fields = strategy.getClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				String name = field.getName();
				Object value = field.get(strategy);
				result.put(name, value);
			}
			return result;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}
