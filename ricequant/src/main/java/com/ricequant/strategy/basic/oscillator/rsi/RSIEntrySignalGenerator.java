package com.ricequant.strategy.basic.oscillator.rsi;

import static com.ricequant.strategy.basic.oscillator.rsi.RSIComputer.computeRSI;

import com.ricequant.strategy.basic.EntrySignalGenerator;
import com.ricequant.strategy.def.HPeriod;
import com.ricequant.strategy.def.IHStatistics;

/**
 * RSI
 */
public class RSIEntrySignalGenerator implements EntrySignalGenerator {

	private double lowRSI;

	private double highRSI;

	// 建议250
	private int dataSetSize;

	// 建议14
	private int period;

	public RSIEntrySignalGenerator() {

	}

	public RSIEntrySignalGenerator(double lowRSI, double highRSI, int dataSetSize, int period) {
		super();
		this.lowRSI = lowRSI;
		this.highRSI = highRSI;
		this.dataSetSize = dataSetSize;
		this.period = period;
	}

	@Override
	public double generateSignal(IHStatistics stat) {
		double[] close = stat.history(dataSetSize, HPeriod.Day).getClosingPrice();

		double[] result = computeRSI(close, period);

		double currentRSI = result[result.length - 1];

		if (currentRSI > highRSI) {
			return -1;
		} else if (currentRSI < lowRSI) {
			return 1;
		} else {
			return 0;
		}
	}

}
