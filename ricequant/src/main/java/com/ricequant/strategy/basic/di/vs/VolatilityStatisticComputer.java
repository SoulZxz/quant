package com.ricequant.strategy.basic.di.vs;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

public class VolatilityStatisticComputer {

	/**
	 * 计算lookback波动性与整体波动性的比较系数, 如果lookback阶段发生过相对较大的波动, 会在返回值体现
	 * 
	 * 波动性用(lookback当前值 - 样本均值)/样本标准差衡量
	 *
	 * @return
	 */
	public double[] computeVolatilityStatistic(double[] input, int lookbackPeriod) {
		double[] result = new double[lookbackPeriod];

		SummaryStatistics stats = new SummaryStatistics();
		for (int i = 0; i < input.length; i++) {
			stats.addValue(input[i]);
		}

		double mean = stats.getMean();
		double std = stats.getStandardDeviation();

		for (int i = 0; i < lookbackPeriod; i++) {
			double lookbackValue = input[input.length - (lookbackPeriod - i)];
			result[i] = Math.abs((lookbackValue - mean) / std);
		}

		return result;
	}

	/**
	 * 计算lookback波动性与整体价格变动波动性的比较系数, 如果lookback阶段发生过相对较大的价格变动波动, 会在返回值体现
	 * 
	 * 波动性用(lookback当前值 - 样本均值)/样本标准差衡量
	 *
	 * @return
	 */
	public double[] computeDeltaVolatilityStatistic(double[] input, int lookbackPeriod) {
		double[] result = new double[lookbackPeriod];

		SummaryStatistics stats = new SummaryStatistics();
		for (int i = 0; i < input.length - 1; i++) {
			stats.addValue(input[i + 1] - input[i]);
		}

		double mean = stats.getMean();
		double std = stats.getStandardDeviation();

		for (int i = 0; i < lookbackPeriod; i++) {
			double lookbackValue = input[input.length - (lookbackPeriod - i)]
					- input[input.length - (lookbackPeriod - i) - 1];
			result[i] = Math.abs((lookbackValue - mean) / std);
		}

		return result;
	}
}
