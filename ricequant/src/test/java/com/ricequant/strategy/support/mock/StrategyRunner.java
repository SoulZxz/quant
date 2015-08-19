package com.ricequant.strategy.support.mock;

import com.ricequant.strategy.def.IHStrategy;

public class StrategyRunner {

	private int fromDay;

	private int toDay;

	private IHStrategy strategy;

	private DummyInitializers initializers = new DummyInitializers();

	private RunnerContext runnerContext = new RunnerContext();

	private int currentDay;

	public StrategyRunner(IHStrategy strategy, int fromDay, int toDay) {
		super();
		this.strategy = strategy;
		this.fromDay = fromDay;
		this.toDay = toDay;
		initializers.setRunnerContext(runnerContext);
		strategy.init(new DummyInformer(), initializers);
	}

	public void runStrategy() {
		DummyInfoPacks info = new DummyInfoPacks(runnerContext);
		DummyTransactionFactory trans = new DummyTransactionFactory(runnerContext);

		for (currentDay = fromDay; currentDay < toDay; currentDay++) {
			info.setDay(currentDay);
			trans.setDay(currentDay);
			DummyStatisticsGroup stats = new DummyStatisticsGroup(currentDay, runnerContext);
			initializers.events().getUpdateHandler().handle(stats, info, trans);
		}
	}

	public void portfolioStatus() {
		runnerContext.getPortfolioHolder().status(currentDay);
	}

	public void concludePortfolio() {
		runnerContext.getPortfolioHolder().closeLastTransactionDetail(currentDay);
	}

	private String strategyName() {
		return strategy.getClass().getSimpleName();
	}
}
