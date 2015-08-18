package com.ricequant.strategy.support.mock;

import java.util.Iterator;

import com.ricequant.strategy.def.IHInstrument;
import com.ricequant.strategy.def.IHStatistics;
import com.ricequant.strategy.def.IHStatisticsGroup;
import com.ricequant.strategy.def.mock.StatisticsFunction;

public class DummyStatisticsGroup implements IHStatisticsGroup {

	private int startDay;

	private RunnerContext runnerContext;

	public DummyStatisticsGroup(int startDay, RunnerContext runnerContext) {
		super();
		this.startDay = startDay;
		this.runnerContext = runnerContext;
	}

	@Override
	public Iterator<IHStatistics> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IHStatistics get(String idOrSymbol) {
		return new DummyStatistics(startDay, idOrSymbol);
	}

	@Override
	public IHStatistics get(IHInstrument instrument) {
		throw new IllegalArgumentException("unimplemented");
	}

	@Override
	public void each(StatisticsFunction stat) {
		for (String stockCode : runnerContext.getStockPool().getStockCodes()) {
			stat.calculate(new DummyStatistics(startDay, stockCode));
		}
	}

}
