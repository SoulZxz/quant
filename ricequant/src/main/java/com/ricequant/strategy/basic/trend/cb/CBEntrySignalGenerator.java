package com.ricequant.strategy.basic.trend.cb;

import com.ricequant.strategy.basic.EntrySignalGenerator;
import com.ricequant.strategy.basic.Signal;
import com.ricequant.strategy.def.HPeriod;
import com.ricequant.strategy.def.IHStatistics;

/**
 * Channel Breakout
 */
public class CBEntrySignalGenerator implements EntrySignalGenerator {

	private int period;

	public CBEntrySignalGenerator(int period) {
		super();
		this.period = period;
	}

	public Signal generateSignal(IHStatistics stat) {
		// 储存进去对应的数组
		double[] close = stat.history(period, HPeriod.Day).getClosingPrice();

		ChannelComputer channelComputer = new ChannelComputer();

		// 计算周期channel
		ChannelItem[] channel = channelComputer.compute(close, period);

		// 计算当期close
		double currentClose = close[close.length - 1];

		// 当期close值是channel高值
		// 简单起见信号强度绝对值统一设置成1
		if (currentClose == channel[0].getHighest()) {
			return new Signal(1);
		}
		// 当期close值是channel低值
		else if (currentClose == channel[0].getLowest()) {
			return new Signal(-1);
		}
		// 什么也不做
		else {
			return new Signal(0);
		}
	}

}
