package com.ricequant.strategy.basic.di.adx;

import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;

public class ADXComputer {

	private Core core = new Core();

	/**
	 * 
	 * 计算adx
	 *
	 * @param close
	 *            至少150元素,150 periods are required to absorb the smoothing
	 *            techniques
	 * @param high
	 * @param low
	 * @param period
	 *            建议14
	 * @return
	 */
	public double[] computeADX(double[] close, double[] high, double[] low,
			int period) {
		MInteger begin = new MInteger();
		MInteger length = new MInteger();
		double[] out = new double[close.length - period + 1];

		core.adx(0, close.length - 1, high, low, close, period, begin, length,
				out);
		return out;
	}

}
