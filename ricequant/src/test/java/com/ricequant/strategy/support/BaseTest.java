package com.ricequant.strategy.support;

import static org.testng.AssertJUnit.assertEquals;

public class BaseTest {

	public void assertArrayEquals(double[] expected, double[] actual) {
		assertEquals(arrayStr(expected), arrayStr(actual));
	}

	private String arrayStr(double[] array) {
		StringBuilder builder = new StringBuilder();
		for (double value : array) {
			builder.append(value).append(",");
		}
		return builder.toString();
	}

}
