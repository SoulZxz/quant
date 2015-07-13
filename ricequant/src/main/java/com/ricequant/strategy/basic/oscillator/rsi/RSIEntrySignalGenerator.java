package com.ricequant.strategy.basic.oscillator.rsi;

import com.ricequant.strategy.basic.EntrySignalGenerator;
import com.ricequant.strategy.basic.Signal;
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

	public RSIEntrySignalGenerator(double lowRSI, double highRSI, int dataSetSize, int period) {
		super();
		this.lowRSI = lowRSI;
		this.highRSI = highRSI;
		this.dataSetSize = dataSetSize;
		this.period = period;
	}

	@Override
	public Signal generateSignal(IHStatistics stat) {
		double[] close = stat.history(dataSetSize, HPeriod.Day).getClosingPrice();

		RSIComputer computer = new RSIComputer();
		double[] result = computer.compute(close, period);

		double currentRSI = result[result.length - 1];

		if (currentRSI > highRSI) {
			return new Signal(-1);
		} else if (currentRSI < lowRSI) {
			return new Signal(1);
		} else {
			return new Signal(0);
		}
	}

}
