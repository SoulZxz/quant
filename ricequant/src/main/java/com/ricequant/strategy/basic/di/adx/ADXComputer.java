package com.ricequant.strategy.basic.di.adx;

import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;

/**
 * At its most basic the Average Directional Index (ADX) can be used to
 * determine if a security is trending or not. This determination helps traders
 * choose between a trend following system or a non-trend following system.
 * Wilder suggests that a strong trend is present when ADX is above 25 and no
 * trend is present when below 20. There appears to be a gray zone between 20
 * and 25. As noted above, chartists may need to adjust the settings to increase
 * sensitivity and signals. ADX also has a fair amount of lag because of all the
 * smoothing techniques. Many technical analysts use 20 as the key level for
 * ADX.
 *
 */
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
	public double[] computeADX(double[] close, double[] high, double[] low, int period) {
		MInteger begin = new MInteger();
		MInteger length = new MInteger();
		double[] out = new double[close.length - period * 2 + 1];

		core.adx(0, close.length - 1, high, low, close, period, begin, length, out);
		return out;
	}

}
