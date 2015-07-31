package com.ricequant.strategy.sample;

import com.ricequant.strategy.def.HPeriod;
import com.ricequant.strategy.def.IHInfoPacks;
import com.ricequant.strategy.def.IHInformer;
import com.ricequant.strategy.def.IHInitializers;
import com.ricequant.strategy.def.IHStatistics;
import com.ricequant.strategy.def.IHStatisticsHistory;
import com.ricequant.strategy.def.IHStrategy;
import com.ricequant.strategy.def.IHTransactionFactory;
import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;

public class TrendingIndexStrategy implements IHStrategy {

	private IHInformer theInformer;

	private Core core = new Core();

	private int adxSmoothingPeriod = 150;

	private int period = 20;

	@Override
	public void init(IHInformer informer, IHInitializers initializers) {
		theInformer = informer;

		String stockCode = "000024.XSHE";

		initializers.instruments((universe) -> {
			return universe.add(stockCode);
		});

		// 允许卖空
		// initializers.shortsell().allow();

		initializers.events().statistics((stats, info, trans) -> {
			stats.each((stat) -> {
				TrendingIndexStrategy.this.logTrendingIndex(stats.get(stockCode), info, trans);
			});
		});
	}

	private void logTrendingIndex(IHStatistics stat, IHInfoPacks info, IHTransactionFactory trans) {
		IHStatisticsHistory history = stat.history(adxSmoothingPeriod, HPeriod.Day);
		double[] adx = this.computeADX(history.getClosingPrice(), history.getHighPrice(),
				history.getLowPrice(), period);
		double[] vhf = this.computeVHF(history.getClosingPrice(), period);
		double[][] tsMatrix = this.computeTrendStatistic(history.getHighPrice(),
				history.getLowPrice(), history.getClosingPrice(), period);
		double[] ts = tsMatrix[0];

		String infoStr = "adx " + adx[adx.length - 1] + ", vhf " + vhf[vhf.length - 1] + ", ts "
				+ ts[ts.length - 1];
		theInformer.info(infoStr);

		theInformer.plot("ADX", adx[adx.length - 1]);
		theInformer.plot("VHF", vhf[vhf.length - 1]);
		theInformer.plot("TS", ts[ts.length - 1]);
		theInformer.plot("CLOSING", stat.getClosingPrice());
	}

	/**
	 * 
	 * 计算adx
	 *
	 * @param close
	 *            至少150元素,150 periods are required to absorb the smoothing
	 *            techniques
	 * @param high
	 * @param low
	 * @param period
	 *            建议14
	 * @return
	 */
	public double[] computeADX(double[] close, double[] high, double[] low, int period) {
		MInteger begin = new MInteger();
		MInteger length = new MInteger();
		double[] out = new double[close.length - period * 2 + 1];

		core.adx(0, close.length - 1, high, low, close, period, begin, length, out);
		return out;
	}

	public double[] computeVHF(double[] close, int period) {
		double[] out = new double[close.length - period + 1];

		for (int i = 0; i < out.length; i++) {
			double lowestClose = computeLowest(close, period, i);
			double highestClose = computeHighest(close, period, i);
			double verticalDist = highestClose - lowestClose;
			double accumulatedDist = computeAccumulatedDist(close, period, i);
			out[i] = verticalDist / accumulatedDist;
		}

		return out;
	}

	/**
	 * 
	 * 返回double[3][input.length - period + 1]数组，[0]是TrendStatistic, [1]是高于范围的概率,
	 * [2]是低于范围的概率, [1] + [2] = [0]
	 *
	 * [1]持续高表示上行趋势, [2]持续高表示下行趋势
	 *
	 * @param high
	 * @param low
	 * @param close
	 * @param period
	 * @return
	 */
	public double[][] computeTrendStatistic(double[] high, double[] low, double[] close, int period) {
		int resultLength = high.length - (period - 1);
		double[] ts = new double[resultLength];
		double[] higherP = new double[resultLength];
		double[] lowerP = new double[resultLength];

		double[][] result = new double[3][resultLength];
		result[0] = ts;
		result[1] = higherP;
		result[2] = lowerP;

		for (int j = 0; j < resultLength; j++) {
			int higher = 0;
			int lower = 0;
			double currentClose = close[(period - 1) + j];

			for (int i = j; i < j + period; i++) {
				if (currentClose < low[i] && currentClose <= high[i]) {
					lower++;
				} else if (currentClose > high[i]) {
					higher++;
				}
			}

			int overlaps = higher + lower;
			higherP[j] = Double.valueOf(higher) / period;
			lowerP[j] = Double.valueOf(lower) / period;
			ts[j] = Double.valueOf(overlaps) / period;
		}

		return result;
	}

	private double computeLowest(double[] input, int period, int start) {
		double currentLowest = input[start];
		for (int i = start; i < start + period; i++) {
			currentLowest = Math.min(currentLowest, input[i]);
		}
		return currentLowest;
	}

	private double computeHighest(double[] input, int period, int start) {
		double currentHighest = input[start];
		for (int i = start; i < start + period; i++) {
			currentHighest = Math.max(currentHighest, input[i]);
		}
		return currentHighest;
	}

	private double computeAccumulatedDist(double[] input, int period, int start) {
		double sum = 0;
		for (int i = start + 1; i < start + period; i++) {
			sum = sum + Math.abs(input[i] - input[i - 1]);
		}
		return sum;
	}
}
