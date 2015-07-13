package com.ricequant.strategy.basic.trend.momentum;

import com.ricequant.strategy.basic.EntrySignalGenerator;
import com.ricequant.strategy.basic.Signal;
import com.ricequant.strategy.def.HPeriod;
import com.ricequant.strategy.def.IHStatistics;

/**
 * Momentum
 */
public class MomentumEntrySignalGenerator implements EntrySignalGenerator {

	private int period;

	public MomentumEntrySignalGenerator(int period) {
		super();
		this.period = period;
	}

	public Signal generateSignal(IHStatistics stat) {
		// 储存进去对应的数组
		double[] close = stat.history(period, HPeriod.Day).getClosingPrice();

		MomentumComputer momentumComputer = new MomentumComputer();

		// 计算周期momentum
		double[] momentum = momentumComputer.compute(close, period);

		// 计算当期momentum
		double currentMomentum = momentum[0];

		// 简单起见信号强度绝对值统一设置成1
		if (currentMomentum > 0) {
			return new Signal(1);
		}
		// 当期close值是channel低值
		else if (currentMomentum < 0) {
			return new Signal(-1);
		}
		// 什么也不做
		else {
			return new Signal(0);
		}
	}
}
