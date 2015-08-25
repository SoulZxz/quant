package com.ricequant.strategy.support;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.ricequant.strategy.def.IHStrategy;

public class StrategyInspetor {

	public static Map<String, Object> showAttributes(IHStrategy strategy, String... excludes) {
		Map<String, Object> result = new TreeMap<String, Object>();

		Set<String> excluded = new HashSet<String>();
		for (String exclude : excludes) {
			excluded.add(exclude);
		}

		try {
			Field[] fields = strategy.getClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				String name = field.getName();
				Object value = field.get(strategy);

				if (!excluded.contains(name)) {
					result.put(name, value);
				}
			}
			return result;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public static Map<String, Object> showSpecifiedAttributes(IHStrategy strategy,
			String... includes) {
		Map<String, Object> result = new TreeMap<String, Object>();

		Set<String> included = new HashSet<String>();
		for (String include : includes) {
			included.add(include);
		}

		try {
			Field[] fields = strategy.getClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				String name = field.getName();
				Object value = field.get(strategy);

				if (included.contains(name)) {
					result.put(name, value);
				}
			}
			return result;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
