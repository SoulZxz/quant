package com.ricequant.strategy.basic;

import com.ricequant.strategy.def.IHPortfolio;
import com.ricequant.strategy.def.IHStatistics;

public interface ExitSignalGenerator {

	/**
	 * @param stat
	 * @return 信号强度, [-1,1], 绝对值越大强度越大, <0止损, >0止盈, 0不动
	 */
	double generateSignal(IHStatistics stat, IHPortfolio portfolio);

}
