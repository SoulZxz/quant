package com.ricequant.strategy.simulator;

import org.testng.annotations.Test;

import com.ricequant.strategy.sample.RSIStrategy;
import com.ricequant.strategy.support.mock.PortfolioHolder;
import com.ricequant.strategy.support.mock.StrategyRunner;

public class RSIStrategySim {

	@Test
	public void testRun() {
		StrategyRunner runner = new StrategyRunner(new RSIStrategy(), 50, 220);
		runner.runStrategy();

		PortfolioHolder.status();
	}

}
