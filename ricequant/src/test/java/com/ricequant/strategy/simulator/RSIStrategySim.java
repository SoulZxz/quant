package com.ricequant.strategy.simulator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.testng.annotations.Test;

import com.ricequant.strategy.sample.RSIStrategy;
import com.ricequant.strategy.support.ReportGenerator;
import com.ricequant.strategy.support.StrategyInspetor;
import com.ricequant.strategy.support.mock.ReportBuffer;
import com.ricequant.strategy.support.mock.StrategyRunner;

public class RSIStrategySim {

	private String[] stockCodes = new String[] { "600036.XSHG", "000024.XSHE", "000528.XSHE" };

	private int startDay = 50;

	 private int endDay = 2534;

//	private int endDay = 2384;

	@Test
	public void testRun() {
		ReportBuffer reportBuffer = new ReportBuffer();
		reportBuffer.setStartDay(startDay);
		reportBuffer.setEndDay(endDay);

		for (String stockCode : stockCodes) {
			RSIStrategy strategy = new RSIStrategy();

			if (StringUtils.isBlank(reportBuffer.getStrategyName())) {
				reportBuffer.setStrategyName(RSIStrategy.class.getSimpleName());
				reportBuffer.setStrategyParams(StrategyInspetor.showAttributes(strategy, "core",
						"theInformer", "stockCode", "currentUnclosedProfitHeld",
						"highestUnclosedProfitHeld", "unclosedPositionInitValue"));
			}

			List<String> stockCodeList = new ArrayList<String>();
			stockCodeList.add(stockCode);
			strategy.setStockCode(stockCodeList);

			StrategyRunner runner = new StrategyRunner(strategy, startDay, endDay);
			runner.runStrategy();

			runner.portfolioStatus();
			runner.concludePortfolio();

			reportBuffer.addStrategyInstTx(
					StrategyInspetor.showSpecifiedAttributes(strategy, "stockCode").toString(),
					runner.exportTxDetails());
		}

		reportBuffer.createReportFragments();

		ReportGenerator.generateReport(reportBuffer);
	}

}
