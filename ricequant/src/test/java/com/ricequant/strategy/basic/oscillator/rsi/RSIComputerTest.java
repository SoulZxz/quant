package com.ricequant.strategy.basic.oscillator.rsi;

import org.testng.annotations.Test;

@Test
public class RSIComputerTest {

	@Test
	public void testCompute() {
		double[] input = new double[] { 1, 2, -10, 4, 5, 6, 7, 8, 9, 10 };

		RSIComputer computer = new RSIComputer();
		double[] result = computer.compute(input, 5);

		System.out.println(result.length);
		for (double r : result) {
			System.out.println(r);
		}
	}

}