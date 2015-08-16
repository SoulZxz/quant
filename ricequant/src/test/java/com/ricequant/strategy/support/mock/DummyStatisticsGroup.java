package com.ricequant.strategy.support.mock;

import java.util.Iterator;

import com.ricequant.strategy.def.IHInstrument;
import com.ricequant.strategy.def.IHStatistics;
import com.ricequant.strategy.def.IHStatisticsGroup;
import com.ricequant.strategy.def.mock.StatisticsFunction;

public class DummyStatisticsGroup implements IHStatisticsGroup {

	private int startDay;

	private StatisticsFunction statFunc;

	public DummyStatisticsGroup(int startDay) {
		super();
		this.startDay = startDay;
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
		this.statFunc = stat;
	}

}
