package com.ricequant.strategy.basic.trend.ma;

import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;

/**
 * <p>
 * exponential ma
 * </p>
 * 
 */
public class ExMAComputer extends AbstractMAComputer {

	@Override
	protected void process(double[] input, int period, MInteger begin, MInteger length, double[] out) {
		Core c = new Core();
		c.ema(0, input.length - 1, input, period, begin, length, out);
	}

}
