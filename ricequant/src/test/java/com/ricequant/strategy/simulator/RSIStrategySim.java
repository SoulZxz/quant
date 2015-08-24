package com.ricequant.strategy.simulator;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import com.ricequant.strategy.sample.RSIStrategy;
import com.ricequant.strategy.support.StrategyInspetor;
import com.ricequant.strategy.support.mock.ReportBuffer;
import com.ricequant.strategy.support.mock.StrategyRunner;

public class RSIStrategySim {

	private String[] stockCodes = new String[] { "000528.XSHE" };

	private int startDay = 50;

	private int endDay = 226;

	@Test
	public void testRun() {
		ReportBuffer reportBuffer = new ReportBuffer();

		RSIStrategy strategy = null;

		for (String stockCode : stockCodes) {
			strategy = new RSIStrategy();
			List<String> stockCodeList = new ArrayList<String>();
			stockCodeList.add(stockCode);
			strategy.setStockCode(stockCodeList);

			StrategyRunner runner = new StrategyRunner(strategy, startDay, endDay);
			runner.runStrategy();

			runner.portfolioStatus();
			runner.concludePortfolio();

			reportBuffer.addStrategyInstTx("11", runner.exportTxDetails());
		}

		reportBuffer.setStrategy(strategy);

		System.out.println(StrategyInspetor.showAttributes(strategy));
	}

}
