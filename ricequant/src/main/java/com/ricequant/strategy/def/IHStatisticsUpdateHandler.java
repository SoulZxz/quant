package com.ricequant.strategy.def;

public interface IHStatisticsUpdateHandler {
	
	void handle (IHStatisticsGroup stats, IHInfoPacks info, IHTransactionFactory trans);
	
}
