package com.ricequant.strategy.support;

import com.ricequant.strategy.def.HPeriod;
import com.ricequant.strategy.def.IHInstrument;
import com.ricequant.strategy.def.IHStatistics;
import com.ricequant.strategy.def.IHStatisticsHistory;

public class DummyStatistics implements IHStatistics {

	private int startDay;

	private String fileName;

	public DummyStatistics(int startDay, String fileName) {
		super();
		this.startDay = startDay;
		this.fileName = fileName;
	}

	@Override
	public IHInstrument getInstrument() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getLastPrice() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getHighPrice() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getLowPrice() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getOpeningPrice() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getClosingPrice() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getTurnoverVolume() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double vwap(int numTicks, HPeriod tickPeriod) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double mavg(int numTicks, HPeriod tickPeriod) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public IHStatisticsHistory history(int numTicks, HPeriod tickPeriod) {
		return HistoryDataProvider.getData(fileName, startDay - numTicks, startDay);
	}

}
