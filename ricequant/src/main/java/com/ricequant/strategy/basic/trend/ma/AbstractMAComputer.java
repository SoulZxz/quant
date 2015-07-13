package com.ricequant.strategy.basic.trend.ma;

import com.tictactec.ta.lib.MInteger;

public abstract class AbstractMAComputer implements MovingAvgComputer {

	@Override
	public double[] compute(double[] input, int period) {
		MInteger begin = new MInteger();
		MInteger length = new MInteger();
		double[] out = new double[input.length - period + 1];

		this.process(input, period, begin, length, out);

		return out;
	}

	protected abstract void process(double[] input, int period, MInteger begin, MInteger length,
			double[] out);

}
