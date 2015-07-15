package com.ricequant.strategy.basic.oscillator.macd;

import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;

public class MACDComputer {

	public MACDItem[] compute(double[] close, int fastPeriod, int slowPeriod, int signalPeriod) {
		MInteger begin = new MInteger();
		MInteger length = new MInteger();
		int resultLegnth = close.length - slowPeriod + 1;
		double[] values = new double[resultLegnth];
		double[] signals = new double[resultLegnth];
		double[] histograms = new double[resultLegnth];
		MACDItem[] result = new MACDItem[resultLegnth];

		Core core = new Core();
		core.macd(0, close.length - 1, close, fastPeriod, slowPeriod, signalPeriod, begin, length,
				values, signals, histograms);

		for (int i = 0; i < resultLegnth; i++) {
			result[i] = new MACDItem(values[i], signals[i], histograms[i]);
		}

		return result;
	}

}
