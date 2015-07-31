package com.ricequant.strategy.basic.oscillator.macd;

import static org.testng.AssertJUnit.assertEquals;

import org.testng.annotations.Test;

import com.ricequant.strategy.def.IHStatisticsHistory;
import com.ricequant.strategy.support.BaseTest;
import com.ricequant.strategy.support.HistoryDataProvider;

public class MACDComputerTest extends BaseTest {

	@Test
	public void testCompute() {
		IHStatisticsHistory history = HistoryDataProvider.getData("data/sampleRaw.csv");
		double[] close = history.getClosingPrice();
		int fastPeriod = 6;
		int slowPeriod = 13;
		int signalPeriod = 8;

		// close.length 36, 36 - (13 - 1) - (8 - 1) = 17
		int expectedLength = 17;
		double[] expectedValues = new double[] { 0.056916425549305316, 0.03582989821798144,
				0.024314477475620322, 0.05198607794047483, 0.05680599341118331,
				0.056009778662181375, 0.013236186829642449, -0.02920626477516386,
				-0.06828506107299681, -0.1265665665176705, -0.135654567136136,
				-0.15425315661127392, -0.21505828663232585, -0.3749365994357312,
				-0.46751774607309216, -0.4851177237664519, -0.5032353540372263 };
		double[] expectedSignals = new double[] { -0.20190499615685087, -0.14907501962911035,
				-0.11054402027250354, -0.07442622066961946, -0.04526350642944107,
				-0.02275833196463608, -0.014759550010351962, -0.01796993106919905,
				-0.029151071070042996, -0.050798958947293554, -0.06965576076703632,
				-0.08845518206575578, -0.11658920530277135, -0.173999737332318,
				-0.23922596149693448, -0.29386857533460503, -0.3403945261574097 };

		MACDComputer computer = new MACDComputer();
		double[][] result = computer.computeMACD(close, fastPeriod, slowPeriod, signalPeriod);

		double[] values = result[0];
		double[] signals = result[1];

		assertEquals(expectedLength, values.length);
		assertArrayEquals(expectedValues, values);
		assertArrayEquals(expectedSignals, signals);
	}

}
