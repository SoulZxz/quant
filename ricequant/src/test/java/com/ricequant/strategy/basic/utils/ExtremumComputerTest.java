package com.ricequant.strategy.basic.utils;

import static org.testng.AssertJUnit.assertEquals;

import org.testng.annotations.Test;

public class ExtremumComputerTest {

	@Test
	public void testFindExtremum() {
		ExtremumComputer computer = new ExtremumComputer();

		double[] sample1 = new double[] { 6.36, 6.34, 6.2, 5.95, 5.87 };
		int[] result = computer.findExtremum(sample1, 5, 0.03);
		inspectResult(result, sample1);
		assertEquals(0, result[0]);

		double[] sample2 = new double[] { 5.65, 5.65, 5.65, 5.65, 5.81, 5.73, 5.64, 5.65, 5.77 };
		result = computer.findExtremum(sample2, 5, 0.03);
		inspectResult(result, sample2);
		assertEquals(-1, result[0]);
		assertEquals(2, result[1]);

		double[] sample3 = new double[] { 6.25, 6.35, 6.44, 6.39, 6.41 };
		result = computer.findExtremum(sample3, 5, 0.03);
		inspectResult(result, sample3);
		assertEquals(1, result[0]);
		assertEquals(2, result[1]);

		// 1阶线性拟合斜率不符
		double[] sample4 = new double[] { 6.44, 6.35, 6.41, 6.39, 6.25 };
		result = computer.findExtremum(sample4, 5, 0.03);
		inspectResult(result, sample4);
		assertEquals(0, result[0]);
	}

	public void inspectResult(int[] result, double[] input) {
		if (result[0] != 0) {
			System.out.println("found Extremum " + result[0] + " "
					+ input[input.length - result[1] - 1]);
		}
	}
	
//	[16.657561691132404, 17.691899566925894, 18.652356165876995, 20.198137943046934, 22.26865675181441]

}
