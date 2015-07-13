package com.ricequant.strategy.basic.oscillator.rsi;

import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;

public class RSIComputer {

	/**
	 * rsi计算结果与input数据量大小有很大关系， input越大计算结果越有参考意义，rsi才是平滑曲线
	 *
	 * @param input
	 *            一般是250个元素
	 * @param period
	 *            建议14
	 * @return
	 */
	public double[] compute(double[] input, int period) {
		MInteger begin = new MInteger();
		MInteger length = new MInteger();
		double[] out = new double[input.length - period];

		Core core = new Core();
		core.rsi(0, input.length - 1, input, period, begin, length, out);
		return out;
	}

}
