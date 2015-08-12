package com.ricequant.strategy.basic.oscillator.rsi;

import static com.ricequant.strategy.basic.oscillator.rsi.RSIComputer.computeRSI;
import static com.ricequant.strategy.basic.utils.BehaviorComputer.simpleCross;

import java.util.Arrays;

import com.ricequant.strategy.basic.EntrySignalGenerator;
import com.ricequant.strategy.def.HPeriod;
import com.ricequant.strategy.def.IHStatistics;

public class RSICrossEntrySignalGenerator implements EntrySignalGenerator {

	private double lowRSI;

	private double highRSI;

	// 建议250
	private int dataSetSize;

	// 建议14
	private int period;

	private int crossLookBackPeriod;

	private int decisionDelay = 0;

	public RSICrossEntrySignalGenerator(double lowRSI, double highRSI, int dataSetSize, int period,
			int crossLookBackPeriod) {
		super();
		this.lowRSI = lowRSI;
		this.highRSI = highRSI;
		this.dataSetSize = dataSetSize;
		this.period = period;
		this.crossLookBackPeriod = crossLookBackPeriod;
	}

	@Override
	public double generateSignal(IHStatistics stat) {
		double[] close = stat.history(dataSetSize, HPeriod.Day).getClosingPrice();

		double[] result = computeRSI(close, period);
		double[] subsetResult = Arrays.copyOfRange(result, result.length - crossLookBackPeriod,
				result.length);

		int[] highRSICrossResult = simpleCross(subsetResult, highRSI, decisionDelay);
		int[] lowRSICrossResult = simpleCross(subsetResult, lowRSI, decisionDelay);

		double signal = 0;

		int highRSICrossFirst = highRSICrossResult[2];
		int highRSICrossUp = highRSICrossResult[0];
		int highRSICrossDown = highRSICrossResult[1];

		if (highRSICrossFirst == -1) {
			// 下穿不上穿, sell
			if (highRSICrossDown > highRSICrossUp) {
				signal = signal - 1;
			}
		} else if (highRSICrossFirst == 1) {
			// 上穿 -> 下穿, sell
			if (highRSICrossUp == highRSICrossDown) {
				signal = signal - 1;
			}
		}

		int lowRSICrossFirst = lowRSICrossResult[2];
		int lowRSICrossUp = lowRSICrossResult[0];
		int lowRSICrossDown = lowRSICrossResult[1];

		if (lowRSICrossFirst == 1) {
			// 上穿不下穿, buy
			if (lowRSICrossUp > lowRSICrossDown) {
				signal = signal + 1;
			}
		} else if (lowRSICrossFirst == -1) {
			// 下穿 -> 上穿, buy
			if (lowRSICrossUp == lowRSICrossDown) {
				signal = signal + 1;
			}
		}

		return signal;
	}

}
