package com.ricequant.strategy.basic;

import com.ricequant.strategy.def.IHStatistics;

public interface EntrySignalGenerator {

	/**
	 * @param stat
	 * @return 信号强度, [-1,1], 绝对值越大强度越大, <0开空头寸, >0开多头寸, 0不动
	 */
	double generateSignal(IHStatistics stat);

}
