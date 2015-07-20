package com.ricequant.strategy.basic.oscillator.macd;

import org.testng.annotations.Test;

import com.ricequant.strategy.def.IHStatisticsHistory;
import com.ricequant.strategy.support.HistoryDataProvider;

@Test
public class MACDComputerTest {

	@Test
	public void testCompute() {
		IHStatisticsHistory history = HistoryDataProvider
				.getData("data/sampleRaw.csv");
		double[] close = history.getClosingPrice();
		int fastPeriod = 6;
		int slowPeriod = 13;
		int signalPeriod = 8;

		MACDComputer computer = new MACDComputer();
		double[][] result = computer.computeMACD(close, fastPeriod, slowPeriod,
				signalPeriod);

		System.out.println(result.length);
		for (double[] r : result) {
			System.out.println(r);
		}
	}

}
