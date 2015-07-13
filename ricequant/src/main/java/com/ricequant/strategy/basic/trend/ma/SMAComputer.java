package com.ricequant.strategy.basic.trend.ma;

import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;

/**
 * <p>
 * simple ma
 * </p>
 * 
 */
public class SMAComputer extends AbstractMAComputer {

	@Override
	protected void process(double[] input, int period, MInteger begin, MInteger length, double[] out) {
		Core c = new Core();
		c.sma(0, input.length - 1, input, period, begin, length, out);
	}

}
