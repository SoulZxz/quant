package com.ricequant.strategy.basic.trend.ma;

import com.ricequant.strategy.basic.EntrySignalGenerator;
import com.ricequant.strategy.basic.Signal;
import com.ricequant.strategy.def.HPeriod;
import com.ricequant.strategy.def.IHStatistics;
import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;

/**
 * MA算法仅适用于长期均线趋势变化明显，转向少的情况，如果长期均线有大量whipsaws，不适合
 */
public class MAEntrySignalGenerator implements EntrySignalGenerator {

	public static final int MA_TYPE_SMA = 1;

	public static final int MA_TYPE_EMA = 2;

	public static final int MA_TYPE_WMA = 3;

	private Core core = new Core();

	private int shortPeriod;

	private int longPeriod;

	private int shortPeriodType;

	private int longPeriodType;

	private double delta = 0;

	public MAEntrySignalGenerator() {

	}

	public MAEntrySignalGenerator(int shortPeriod, int longPeriod, int shortPeriodType,
			int longPeriodType) {
		super();
		this.shortPeriod = shortPeriod;
		this.longPeriod = longPeriod;
		this.shortPeriodType = shortPeriodType;
		this.longPeriodType = longPeriodType;
	}

	public Signal generateSignal(IHStatistics stat) {
		// 储存进去对应的数组
		double[] closeShort = stat.history(shortPeriod, HPeriod.Day).getClosingPrice();
		double[] closeLong = stat.history(longPeriod, HPeriod.Day).getClosingPrice();

		// 计算短期MA
		double[] shortMA = computeMA(closeShort, shortPeriod, shortPeriodType);
		// 计算长期MA
		double[] longMA = computeMA(closeLong, longPeriod, longPeriodType);

		// 计算当天的短期MA与长期MA的差值
		double previousDelta = delta;
		delta = shortMA[shortMA.length - 1] - longMA[longMA.length - 1];

		// TODO 加入whipsaws detection
		// 短期MA与长期MA值出现交叉, 短期MA处于下降趋势, 长期MA处于上升趋势
		// 简单起见信号强度绝对值统一设置成1
		if ((previousDelta > 0) && delta < 0) {
			return new Signal(-1);
		}
		// 短期MA与长期MA值出现交叉, 短期MA处于上升趋势,长期MA处于下降趋势
		else if ((previousDelta < 0) && delta > 0) {
			return new Signal(1);
		}
		// 什么也不做
		else {
			return new Signal(0);
		}
	}

	public double[] computeMA(double[] input, int period, int maType) {
		MInteger begin = new MInteger();
		MInteger length = new MInteger();
		double[] out = new double[input.length - period + 1];

		switch (maType) {
		case MA_TYPE_SMA:
			core.sma(0, input.length - 1, input, period, begin, length, out);
			break;
		case MA_TYPE_EMA:
			core.ema(0, input.length - 1, input, period, begin, length, out);
			break;
		case MA_TYPE_WMA:
			core.wma(0, input.length - 1, input, period, begin, length, out);
			break;
		default:
			throw new RuntimeException("unsupported MaType " + maType);
		}

		return out;
	}
}
