package com.ricequant.strategy.basic.trend.vb;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import com.ricequant.strategy.basic.trend.ma.MAComputerFactory;
import com.ricequant.strategy.basic.trend.ma.MAType;

public class VolatilityComputer {

	public double computeVolatilityMeasure(double[] open, double[] close, double[] high,
			double[] low, VolatilityMeasureType type) {
		switch (type) {
		case PRICE_CHANGE_STD:
			return computePriceChangeSTD(open, close);
		case PRICE_STD:
			return computePriceSTD(close);
		case ATR:
			return computeATR(close, high, low);
		default:
			throw new RuntimeException("unsupported VolatilityMeasureType " + type);
		}
	}

	public double computeReferenceValue(double[] open, double[] close, ReferenceValueType type) {
		switch (type) {
		case PREVIOUS_CLOSE:
			return close[close.length - 2];
		case CURRENT_OPEN:
			return open[open.length - 1];
		case CLOSE_MA:
			int maPeriod = 5;
			double[] sma = MAComputerFactory.create(MAType.SMA).compute(close, maPeriod);
			return sma[sma.length - 1];
		default:
			throw new RuntimeException("unsupported ReferenceValueType " + type);
		}
	}

	private double computePriceChangeSTD(double[] open, double[] close) {
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
	private double computePriceSTD(double[] close) {
		SummaryStatistics stats = new SummaryStatistics();
		for (int i = 0; i < close.length; i++) {
			stats.addValue(close[i]);
		}

		return stats.getStandardDeviation();
	}

	private double computeATR(double[] close, double[] high, double[] low) {
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
}
