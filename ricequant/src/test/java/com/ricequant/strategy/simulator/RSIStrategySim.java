package com.ricequant.strategy.simulator;

import org.testng.annotations.Test;

import com.ricequant.strategy.sample.RSIStrategy;
import com.ricequant.strategy.support.mock.StrategyRunner;

public class RSIStrategySim {

	private String[] stockCode = new String[] { "000528.XSHE" };

	private int startDay = 50;

	private int endDay = 226;

	@Test
	public void testRun() {
		RSIStrategy strategy = new RSIStrategy();
		strategy.setStockCode(stockCode);

		StrategyRunner runner = new StrategyRunner(strategy, startDay, endDay);
		runner.runStrategy();

		runner.portfolioStatus();
	}

}
