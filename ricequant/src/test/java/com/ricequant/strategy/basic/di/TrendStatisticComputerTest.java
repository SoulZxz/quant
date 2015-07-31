package com.ricequant.strategy.basic.di;

import static org.testng.AssertJUnit.assertEquals;

import org.testng.annotations.Test;

import com.ricequant.strategy.basic.di.ts.TrendStatisticComputer;
import com.ricequant.strategy.def.IHStatisticsHistory;
import com.ricequant.strategy.support.BaseTest;
import com.ricequant.strategy.support.HistoryDataProvider;

public class TrendStatisticComputerTest extends BaseTest {

	@Test
	public void testCompute() {
		IHStatisticsHistory history = HistoryDataProvider.getData("data/sampleRaw.csv");
		double[] close = history.getClosingPrice();
		double[] high = history.getHighPrice();
		double[] low = history.getLowPrice();

		int period = 20;

		// close.length 36, 36 - (20 - 1) = 17
		int expectedLength = 17;
		double[] expectedTs = new double[] { 0.6, 0.7, 0.6, 0.55, 0.45, 0.45, 0.8, 0.8, 0.85, 0.9,
				0.8, 0.8, 0.9, 0.95, 0.95, 0.85, 0.9 };
		double[] expectedHigher = new double[] { 0.45, 0.4, 0.4, 0.55, 0.45, 0.45, 0.4, 0.3, 0.3,
				0.25, 0.2, 0.15, 0.0, 0.0, 0.0, 0.0, 0.0 };
		double[] expectedLower = new double[] { 0.15, 0.3, 0.2, 0.0, 0.0, 0.0, 0.4, 0.5, 0.55,
				0.65, 0.6, 0.65, 0.9, 0.95, 0.95, 0.85, 0.9 };

		TrendStatisticComputer computer = new TrendStatisticComputer();
		double[][] result = computer.computeTrendStatistic(high, low, close, period);

		double[] ts = result[0];
		double[] higher = result[1];
		double[] lower = result[2];

		assertEquals(expectedLength, ts.length);
		assertArrayEquals(expectedTs, ts);
		assertArrayEquals(expectedHigher, higher);
		assertArrayEquals(expectedLower, lower);
	}

}
