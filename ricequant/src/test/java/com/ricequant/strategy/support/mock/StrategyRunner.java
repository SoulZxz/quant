package com.ricequant.strategy.support.mock;

import com.ricequant.strategy.def.IHStrategy;

public class StrategyRunner {

	private int fromDay;

	private int toDay;

	private IHStrategy strategy;

	private DummyInitializers initializers = new DummyInitializers();

	public StrategyRunner(IHStrategy strategy, int fromDay, int toDay) {
		super();
		this.strategy = strategy;
		this.fromDay = fromDay;
		this.toDay = toDay;
		strategy.init(new DummyInformer(), initializers);
	}

	public void runStrategy() {
		DummyInfoPacks info = new DummyInfoPacks();
		DummyTransactionFactory trans = new DummyTransactionFactory();

		for (int day = fromDay; day < toDay; day++) {
			info.setDay(day);
			trans.setDay(day);
			DummyStatisticsGroup stats = new DummyStatisticsGroup(day);
			initializers.events().getUpdateHandler().handle(stats, info, trans);
		}
	}

}
