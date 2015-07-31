package com.ricequant.strategy.basic.di;

import static org.testng.AssertJUnit.assertEquals;

import org.testng.annotations.Test;

import com.ricequant.strategy.basic.di.adx.ADXComputer;
import com.ricequant.strategy.def.IHStatisticsHistory;
import com.ricequant.strategy.support.BaseTest;
import com.ricequant.strategy.support.HistoryDataProvider;

public class ADXComputerTest extends BaseTest {

	@Test
	public void testCompute() {
		IHStatisticsHistory history = HistoryDataProvider.getData("data/sampleRaw.csv");
		double[] close = history.getClosingPrice();
		double[] high = history.getHighPrice();
		double[] low = history.getLowPrice();

		int period = 10;

		// close.length 36, 36 - (10*2 - 1) = 17
		int expectedLength = 17;
		double[] expectedValues = new double[] { 42.23858479180687, 38.36238791989631,
				36.05570251121973, 33.32227705732314, 30.748593028355153, 28.432277402283955,
				27.356477946495346, 27.236796174325058, 27.772390044335634, 28.8493479109979,
				29.936828408388713, 30.251853447607296, 32.23053704077381, 35.74565780013975,
				39.05085185143186, 42.08458257200292, 43.96887409096041 };

		ADXComputer computer = new ADXComputer();
		double[] values = computer.computeADX(close, high, low, period);

		assertEquals(expectedLength, values.length);
		assertArrayEquals(expectedValues, values);
	}

}
