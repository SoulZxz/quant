package com.ricequant.strategy.support.mock;

import com.ricequant.strategy.def.HPeriod;
import com.ricequant.strategy.def.IHInstrument;
import com.ricequant.strategy.def.IHStatistics;
import com.ricequant.strategy.def.IHStatisticsHistory;
import com.ricequant.strategy.support.HistoryDataProvider;

public class DummyStatistics implements IHStatistics {

	private int startDay;

	private String stockCode;

	private IHStatisticsHistory current;

	public DummyStatistics(int startDay, String stockCode) {
		super();
		this.startDay = startDay;
		this.stockCode = stockCode;
		current = HistoryDataProvider.getData("data/pool/" + stockCode, startDay, startDay + 2);
	}

	@Override
	public IHInstrument getInstrument() {
		return new DummyInstrument(stockCode);
	}

	@Override
	public double getLastPrice() {
		return current.getOpeningPrice()[1];
	}

	@Override
	public double getHighPrice() {
		return current.getHighPrice()[0];
	}

	@Override
	public double getLowPrice() {
		return current.getLowPrice()[0];
	}

	@Override
	public double getOpeningPrice() {
		return current.getOpeningPrice()[0];
	}

	@Override
	public double getClosingPrice() {
		return current.getClosingPrice()[0];
	}

	@Override
	public double getTurnoverVolume() {
		return current.getTurnoverVolume()[0];
	}

	@Override
	public double vwap(int numTicks, HPeriod tickPeriod) {
		throw new IllegalArgumentException("unimplemented");
	}

	@Override
	public double mavg(int numTicks, HPeriod tickPeriod) {
		throw new IllegalArgumentException("unimplemented");
	}

	@Override
	public IHStatisticsHistory history(int numTicks, HPeriod tickPeriod) {
		return HistoryDataProvider.getData("data/pool/" + stockCode, startDay - numTicks, startDay);
	}

}
