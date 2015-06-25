package com.ricequant.strategy.def;

import com.ricequant.strategy.def.mock.StatisticsFunction;

public interface IHStatisticsGroup extends Iterable<IHStatistics> {

	/**
	 * 访问单个股票的行情
	 * 
	 * @param idOrSymbol
	 * @return
	 */
	IHStatistics get(String idOrSymbol);

	/**
	 * 访问单个股票的行情
	 * 
	 * @param instrument
	 * @return
	 */
	IHStatistics get(IHInstrument instrument);
	
	void each(StatisticsFunction stat);
}
