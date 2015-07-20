package com.ricequant.strategy.basic.oscillator.macd;

import com.ricequant.strategy.basic.EntrySignalGenerator;
import com.ricequant.strategy.def.HPeriod;
import com.ricequant.strategy.def.IHStatistics;
import com.ricequant.strategy.def.IHStatisticsHistory;

/**
 * 1.Signal Line Crossovers
 * 
 * Signal line crossovers are the most common MACD signals. The signal line is a
 * 9-day EMA of the MACD Line. As a moving average of the indicator, it trails
 * the MACD and makes it easier to spot MACD turns. A bullish crossover occurs
 * when the MACD turns up and crosses above the signal line. A bearish crossover
 * occurs when the MACD turns down and crosses below the signal line. Crossovers
 * can last a few days or a few weeks, it all depends on the strength of the
 * move.
 * 
 * 2.Centerline Crossovers ( only take them in strong trend, ignore them in
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
public class MACDEntrySignalGenerator extends MACDComputer implements
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
		double[] signals = result[1];

		int current = result.length - 1;
		int yesterday = result.length - 2;
		double macdDelta = values[current] - values[yesterday];
		double yesterdayDelta = values[yesterday] - signals[yesterday];
		double currentDelta = values[current] - signals[current];

		// macd上升，穿过signal line，买入
		if (macdDelta > 0 && yesterdayDelta < 0 && currentDelta > 0) {
			return 1;
		}
		// madc下降，穿过signal line，卖出
		else if (macdDelta < 0 && yesterdayDelta > 0 && currentDelta < 0) {
			return -1;
		} else {
			return 0;
		}
	}

}
