package com.ricequant.strategy.basic;

public interface TradingSystem {

	EntrySignalGenerator getEntryGenerator();

	ExitSignalGenerator getExitGenerator();

	EntryFilter getEntryFilter();

}
