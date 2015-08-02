package com.ricequant.strategy.sample;

import java.util.Arrays;

import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;

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

public class Experimental implements IHStrategy {

	private IHInformer theInformer;

	private Core core = new Core();

	private int adxSmoothingPeriod = 150;

	private int period = 20;

	private boolean allowShortSell = true;

	@Override
	public void init(IHInformer informer, IHInitializers initializers) {
		theInformer = informer;

		String stockCode = "000024.XSHE";

		initializers.instruments((universe) -> {
			return universe.add(stockCode);
		});

		// 允许卖空
		initializers.shortsell().allow();

		initializers.events().statistics((stats, info, trans) -> {
			stats.each((stat) -> {
				Experimental.this.logTrendingIndex(stats.get(stockCode), info, trans);
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
		double[] high = tsMatrix[1];
		double[] low = tsMatrix[2];

		double signal = this.hasCrossSignal(Arrays.copyOfRange(high, high.length - 3, high.length),
				Arrays.copyOfRange(low, low.length - 3, low.length));
		if (signal > 0) {
			this.entry(trans, info, stat.getInstrument().getSymbol(), 1);
		} else if (signal < 0) {
			this.entry(trans, info, stat.getInstrument().getSymbol(), -1);
		}

		String infoStr = "adx " + adx[adx.length - 1] + ", vhf " + vhf[vhf.length - 1] + ", ts "
				+ ts[ts.length - 1];
		theInformer.info(infoStr);

		theInformer.plot("ADX", adx[adx.length - 1]);
		theInformer.plot("VHF", vhf[vhf.length - 1]);
		theInformer.plot("TS", ts[ts.length - 1]);
		theInformer.plot("CLOSING", stat.getClosingPrice());
	}

	public void entry(IHTransactionFactory trans, IHInfoPacks info, String stockCode, int direction) {
		int nonClosed = (int) info.position(stockCode).getNonClosedTradeQuantity();
		int directionalNonClosed = direction * nonClosed;

		// 如果有反方向的头寸, 先平掉
		if (directionalNonClosed < 0) {
			if (nonClosed < 0) {
				// 平掉空头寸
				trans.buy(stockCode).shares(-nonClosed).commit();
				// 开多头寸
				trans.buy(stockCode).percent(100).commit();
			} else {
				// 平掉多头寸
				trans.sell(stockCode).shares(nonClosed).commit();
				// 开空头寸
				if (allowShortSell) {
					trans.sell(stockCode).percent(100).commit();
				}
			}
		}
		// 如果没持有头寸, 开
		else if (directionalNonClosed == 0) {
			if (direction > 0) {
				// 开多头寸
				trans.buy(stockCode).percent(100).commit();
			} else if (direction < 0) {
				// 开空头寸
				if (allowShortSell) {
					trans.sell(stockCode).percent(100).commit();
				}
			}
		}
	}

	private double hasCrossSignal(double[] high, double[] low) {
		int negative = 0;
		int positive = 0;

		WeightedObservedPoints hps = new WeightedObservedPoints();
		WeightedObservedPoints lps = new WeightedObservedPoints();

		for (int i = 0; i < high.length; i++) {
			hps.add(i, high[i]);
			lps.add(i, low[i]);

			double delta = high[i] - low[i];
			if (delta > 0) {
				positive++;
			} else if (delta < 0) {
				negative++;
			}
		}

		// 有交叉
		if (negative > 0 && positive > 0) {
			// 算high拟合直线和low拟合直线的斜率
			PolynomialCurveFitter fitter = PolynomialCurveFitter.create(1);

			double hCoeff = fitter.fit(hps.toList())[1];
			double lCoeff = fitter.fit(lps.toList())[1];
			// TODO 加filter和止损策略
			double direction = hCoeff - lCoeff;

			if (direction > 0) {
				return 1;
			} else if (direction < 0) {
				return -1;
			} else {
				return 0;
			}
		} else {
			return 0;
		}
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
