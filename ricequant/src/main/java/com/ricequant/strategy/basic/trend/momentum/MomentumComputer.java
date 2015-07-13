package com.ricequant.strategy.basic.trend.momentum;

public class MomentumComputer {

	public double[] compute(double[] input, int period) {
		double[] out = new double[input.length - period + 1];

		for (int i = period - 1; i < input.length; i++) {
			double start = input[i - (period - 1)];
			double end = input[i];
			out[i - (period - 1)] = end - start;
		}

		return out;
	}
}
