package com.ricequant.strategy.support.mock;

import com.ricequant.strategy.def.HPeriod;
import com.ricequant.strategy.def.IHInstrument;
import com.ricequant.strategy.def.IHStatistics;
import com.ricequant.strategy.def.IHStatisticsHistory;
import com.ricequant.strategy.support.HistoryDataProvider;

public class DummyStatistics implements IHStatistics {

	private int startDay;

	private String stockCode;

	public DummyStatistics(int startDay, String stockCode) {
		super();
		this.startDay = startDay;
		this.stockCode = stockCode;
	}

	@Override
	public IHInstrument getInstrument() {
		throw new IllegalArgumentException("unimplemented");
	}

	@Override
	public double getLastPrice() {
		throw new IllegalArgumentException("unimplemented");
	}

	@Override
	public double getHighPrice() {
		throw new IllegalArgumentException("unimplemented");
	}

	@Override
	public double getLowPrice() {
		throw new IllegalArgumentException("unimplemented");
	}

	@Override
	public double getOpeningPrice() {
		throw new IllegalArgumentException("unimplemented");
	}

	@Override
	public double getClosingPrice() {
		throw new IllegalArgumentException("unimplemented");
	}

	@Override
	public double getTurnoverVolume() {
		throw new IllegalArgumentException("unimplemented");
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
