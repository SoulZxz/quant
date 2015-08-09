package com.ricequant.strategy.basic.utils;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

public class VolatilityComputer {

	public static double computePriceChangeSTD(double[] open, double[] close) {
		SummaryStatistics stats = new SummaryStatistics();
		for (int i = 0; i < open.length; i++) {
			stats.addValue(close[i] - open[i]);
		}

		return stats.getStandardDeviation();
	}

	/**
	 * 以close价计算
	 *
	 * @param close
	 * @return
	 */
	public static double computePriceSTD(double[] close) {
		SummaryStatistics stats = new SummaryStatistics();
		for (int i = 0; i < close.length; i++) {
			stats.addValue(close[i]);
		}

		return stats.getStandardDeviation();
	}

	public static double computeATR(double[] close, double[] high, double[] low) {
		int today = close.length - 1;
		int yesterday = close.length - 2;

		// Today’s high minus today’s low
		double m1 = Math.abs(high[today] - low[today]);
		// Today’s high minus yesterday’s close
		double m2 = Math.abs(high[today] - close[yesterday]);
		// Yesterday’s close minus today’s low
		double m3 = Math.abs(close[yesterday] - low[today]);

		double max12 = Math.max(m1, m2);
		return Math.max(max12, m3);
	}

	/**
	 * 计算closing价格变动百分比
	 * 
	 * @param close
	 * @return
	 */
	public static double computeClosingChangeSTD(double[] close) {
		SummaryStatistics stats = new SummaryStatistics();
		for (int i = 0; i < close.length; i++) {
			stats.addValue(close[i]);
		}

		return stats.getStandardDeviation();
	}

	public double[] computeVolatilityArray(double[] input, int lookbackPeriod) {
		SummaryStatistics stats = new SummaryStatistics();
		for (int i = 0; i < input.length; i++) {
			stats.addValue(input[i]);
		}

		double mean = stats.getMean();
		double std = stats.getStandardDeviation();

		double[] result = new double[lookbackPeriod];

		for (int i = 0; i < lookbackPeriod; i++) {
			double volatility = (input[input.length - lookbackPeriod + i] - mean) / std;
			result[i] = volatility;
		}

		return result;
	}
}
