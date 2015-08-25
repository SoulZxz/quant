package com.ricequant.strategy.simulator;

import org.apache.commons.lang.StringUtils;

import com.ricequant.strategy.def.IHStrategy;
import com.ricequant.strategy.support.ReportGenerator;
import com.ricequant.strategy.support.StrategyInspetor;
import com.ricequant.strategy.support.mock.ReportBuffer;
import com.ricequant.strategy.support.mock.StrategyRunner;

public abstract class BaseStrategySim {

	protected String[] stockCodes = new String[] { "600028.XSHG", "601398.XSHG", "601988.XSHG",
			"600036.XSHG", "000024.XSHE", "000528.XSHE" };

	protected int startDay = 50;

	protected int endDay = 2534;

	protected String[] excludes;

	protected String[] includes;

	protected abstract IHStrategy createStrategy(String stockCode);

	protected StrategyRunner createStrategyRunner(ReportBuffer reportBuffer, String stockCode) {
		IHStrategy strategy = createStrategy(stockCode);

		if (StringUtils.isBlank(reportBuffer.getStrategyName())) {
			reportBuffer.setStrategyName(strategy.getClass().getSimpleName());
			reportBuffer.setStrategyParams(StrategyInspetor.showAttributes(strategy, excludes));
		}

		StrategyRunner runner = new StrategyRunner(strategy, startDay, endDay);
		runner.runStrategy();

		runner.portfolioStatus();
		runner.concludePortfolio();

		return runner;
	}

	protected void runAndReport() {
		ReportBuffer reportBuffer = new ReportBuffer();
		reportBuffer.setStartDay(startDay);
		reportBuffer.setEndDay(endDay);

		for (String stockCode : stockCodes) {
			StrategyRunner runner = this.createStrategyRunner(reportBuffer, stockCode);

			reportBuffer.addStrategyInstTx(
					StrategyInspetor.showSpecifiedAttributes(runner.getStrategy(), includes)
							.toString(), runner.exportTxDetails());
		}

		reportBuffer.createReportFragments();

		ReportGenerator.generateReport(reportBuffer);
	}

}
