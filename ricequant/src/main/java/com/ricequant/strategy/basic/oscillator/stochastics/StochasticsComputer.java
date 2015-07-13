package com.ricequant.strategy.basic.oscillator.stochastics;

import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MAType;
import com.tictactec.ta.lib.MInteger;

public class StochasticsComputer {

	public SlowStochastic[] compute(double[] close, double[] high,
			double[] low, int period, int maPeriod) {
		MInteger begin = new MInteger();
		MInteger length = new MInteger();
		int resultLegnth = close.length - period + 1;
		double[] slowK = new double[resultLegnth];
		double[] slowD = new double[resultLegnth];
		SlowStochastic[] result = new SlowStochastic[resultLegnth];

		Core core = new Core();
		core.stoch(0, close.length - 1, high, low, close, period, maPeriod,
				MAType.Sma, maPeriod, MAType.Sma, begin, length, slowK, slowD);

		for (int i = 0; i < resultLegnth; i++) {
			result[i] = new SlowStochastic(slowK[i], slowD[i]);
		}

		return result;
	}

}
