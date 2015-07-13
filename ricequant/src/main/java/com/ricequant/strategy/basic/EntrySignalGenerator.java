package com.ricequant.strategy.basic;

import com.ricequant.strategy.def.IHStatistics;

public interface EntrySignalGenerator {

	Signal generateSignal(IHStatistics stat);

}
