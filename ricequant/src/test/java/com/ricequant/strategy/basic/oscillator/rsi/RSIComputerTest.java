package com.ricequant.strategy.basic.oscillator.rsi;

import static org.testng.AssertJUnit.assertEquals;

import org.testng.annotations.Test;

import com.ricequant.strategy.def.IHStatisticsHistory;
import com.ricequant.strategy.support.BaseTest;
import com.ricequant.strategy.support.HistoryDataProvider;

public class RSIComputerTest extends BaseTest {

	@Test
	public void testCompute() {
		IHStatisticsHistory history = HistoryDataProvider.getData("data/sampleRaw.csv");
		double[] close = history.getClosingPrice();

		int expectedLength = 22;
		double[] expectedValues = new double[] { 37.081339712918655, 35.43791769257826,
				40.92079526603778, 37.3132449146523, 40.08076060820155, 39.661718181072345,
				37.348871225469466, 37.70452210868632, 42.127000124838, 41.2440957633654,
				41.11153850865911, 37.47928824408987, 36.12881768384712, 34.89774649326531,
				31.858200680368498, 35.35636093629221, 33.739752740220865, 29.397164967886173,
				22.15351750138033, 21.715079513086483, 24.915665493974913, 23.510817417158357 };

		RSIEntrySignalGenerator computer = new RSIEntrySignalGenerator();
		double[] result = computer.computeRSI(close, 14);

		assertEquals(expectedLength, result.length);
		this.assertArrayEquals(expectedValues, result);
	}

}
