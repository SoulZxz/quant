package com.ricequant.strategy.basic.oscillator.macd;

import com.ricequant.strategy.basic.EntrySignalGenerator;
import com.ricequant.strategy.def.HPeriod;
import com.ricequant.strategy.def.IHStatistics;
import com.ricequant.strategy.def.IHStatisticsHistory;

/**
 * Centerline Crossovers ( only take them in strong trend, ignore them in
 * whipsaw )
 * 
 * Centerline crossovers are the next most common MACD signals. A bullish
 * centerline crossover occurs when the MACD Line moves above the zero line to
 * turn positive. This happens when the 12-day EMA of the underlying security
 * moves above the 26-day EMA. A bearish centerline crossover occurs when the
 * MACD moves below the zero line to turn negative. This happens when the 12-day
 * EMA moves below the 26-day EMA.
 * 
 * Centerline crossovers can last a few days or a few months. It all depends on
 * the strength of the trend. The MACD will remain positive as long as there is
 * a sustained uptrend. The MACD will remain negative when there is a sustained
 * downtrend.
 */
public class MACDCenterlineEntrySignalGenerator extends MACDComputer implements
		EntrySignalGenerator {

	/**
	 * >= slowPeriod + 1
	 */
	private int period;

	private int fastPeriod;

	private int slowPeriod;

	private int signalPeriod;

	@Override
	public double generateSignal(IHStatistics stat) {
		IHStatisticsHistory history = stat.history(period, HPeriod.Day);
		double[] close = history.getClosingPrice();

		double[][] result = computeMACD(close, fastPeriod, slowPeriod,
				signalPeriod);

		double[] values = result[0];

		int current = result.length - 1;
		int yesterday = result.length - 2;

		double currentMACD = values[current];
		double yesterdayMACD = values[yesterday];

		// macd上升，穿过0，买入
		if (currentMACD > 0 && yesterdayMACD < 0) {
			return 1;
		}
		// madc下降，穿过0，卖出
		else if (currentMACD < 0 && yesterdayMACD > 0) {
			return -1;
		} else {
			return 0;
		}
	}

}
