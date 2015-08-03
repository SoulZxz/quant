package com.ricequant.strategy.basic.di;

import static org.testng.AssertJUnit.assertEquals;

import org.testng.annotations.Test;

import com.ricequant.strategy.basic.di.vs.VolatilityStatisticComputer;
import com.ricequant.strategy.def.IHStatisticsHistory;
import com.ricequant.strategy.support.BaseTest;
import com.ricequant.strategy.support.HistoryDataProvider;

public class VolatilityStatisticComputerTest extends BaseTest {

	@Test
	public void testCompute() {
		IHStatisticsHistory history = HistoryDataProvider.getData("data/sampleRaw.csv");
		double[] close = history.getClosingPrice();

		int lookbackPeriod = 5;

		int expectedLength = 5;
		double[] expectedValues = new double[] { 0.7863437111779268, 1.9788209874697298,
				2.0695529541441067, 1.8880890207953531, 2.134361501768662 };

		VolatilityStatisticComputer computer = new VolatilityStatisticComputer();
		double[] result = computer.computeVolatilityStatistic(close, lookbackPeriod);

		assertEquals(expectedLength, result.length);
		assertArrayEquals(expectedValues, result);
	}

	@Test
	public void testComputeDelta() {
		IHStatisticsHistory history = HistoryDataProvider.getData("data/sampleRaw.csv");
		double[] close = history.getClosingPrice();

		int lookbackPeriod = 5;

		int expectedLength = 5;
		double[] expectedValues = new double[] { 0.9041582514189225, 2.5293651858977126,
				0.0770987656248688, 0.7210486830598624, 0.2908726157665577 };

		VolatilityStatisticComputer computer = new VolatilityStatisticComputer();
		double[] result = computer.computeDeltaVolatilityStatistic(close, lookbackPeriod);

		assertEquals(expectedLength, result.length);
		assertArrayEquals(expectedValues, result);
	}
}
