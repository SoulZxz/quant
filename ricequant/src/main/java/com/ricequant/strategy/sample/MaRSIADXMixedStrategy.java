package com.ricequant.strategy.sample;

import java.util.Arrays;

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

public class MaRSIADXMixedStrategy implements IHStrategy {

	public static final int MA_TYPE_SMA = 1;

	public static final int MA_TYPE_EMA = 2;

	public static final int MA_TYPE_WMA = 3;

	private IHInformer theInformer;

	private Core core = new Core();

	/** rsi **/
	private double lowRSI = 30;

	private double highRSI = 70;

	// 建议250
	private int dataSetSize = 40;

	// 建议14
	private int rsiPeriod = 14;

	/** adx **/
	private int adxSmoothingPeriod = 150;

	private int adxPeriod = 14;

	private double adxTrendingTH = 25;

	private double adxTrendingGrayTH = 20;

	private int adxTrendFormingLookbackPeriod = 5;

	/** ma **/
	private int shortPeriod = 10;

	private int longPeriod = 40;

	private int shortPeriodType = MA_TYPE_SMA;

	private int longPeriodType = MA_TYPE_SMA;

	private double delta = 0;

	/** other **/
	private String stockCode = "000528.XSHE";

	private boolean filterEnabled = true;

	private boolean allowShortSell = false;

	@Override
	public void init(IHInformer informer, IHInitializers initializers) {
		theInformer = informer;

		initializers.instruments((universe) -> {
			return universe.add(stockCode);
		});

		// 允许卖空
		initializers.shortsell().allow();

		initializers.events().statistics((stats, info, trans) -> {
			stats.each((stat) -> {
				MaRSIADXMixedStrategy.this.eaWork(stats.get(stockCode), info, trans);
			});
		});
	}

	public void eaWork(IHStatistics stat, IHInfoPacks info, IHTransactionFactory trans) {
		IHStatisticsHistory history = stat.history(adxSmoothingPeriod, HPeriod.Day);
		double[] adx = this.computeADX(history.getClosingPrice(), history.getHighPrice(),
				history.getLowPrice(), adxPeriod);
		double currentAdx = adx[adx.length - 1];

		double rsiSignal = generateRSISignal(stat);

		if (rsiSignal != 0) {
			int direction = rsiSignal > 0 ? 1 : -1;
			theInformer.info("rsi trade " + direction + " adx " + currentAdx);
			boolean confirmedNoTrend = currentAdx < adxTrendingGrayTH;
			boolean possibleNoTrend = currentAdx > adxTrendingGrayTH
					&& currentAdx < adxTrendingTH
					&& !adxTrendForming(adx, adxTrendingGrayTH, adxTrendingTH,
							adxTrendFormingLookbackPeriod);
			if (!filterEnabled || confirmedNoTrend || possibleNoTrend) {
				this.entry(trans, info, stockCode, direction);
			}
		}

		double maSignal = this.generateMASignal(stat);
		if (maSignal != 0) {
			int direction = maSignal > 0 ? 1 : -1;
			theInformer.info("ma trade " + direction + " adx " + currentAdx);
			if (!filterEnabled || currentAdx >= adxTrendingTH) {
				this.entry(trans, info, stockCode, direction);
			}
		}

		theInformer.plot("CLOSING", stat.getClosingPrice());
	}

	public double generateRSISignal(IHStatistics stat) {
		double[] close = stat.history(dataSetSize, HPeriod.Day).getClosingPrice();

		double[] result = computeRSI(close, rsiPeriod);

		double currentRSI = result[result.length - 1];

		if (currentRSI > highRSI) {
			return -1;
		} else if (currentRSI < lowRSI) {
			return 1;
		} else {
			return 0;
		}
	}

	public double generateMASignal(IHStatistics stat) {
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
			return -1;
		}
		// 短期MA与长期MA值出现交叉, 短期MA处于上升趋势,长期MA处于下降趋势
		else if ((previousDelta < 0) && delta > 0) {
			return 1;
		}
		// 什么也不做
		else {
			return 0;
		}
	}

	public double[] computeRSI(double[] input, int period) {
		MInteger begin = new MInteger();
		MInteger length = new MInteger();
		double[] out = new double[input.length - period];

		core.rsi(0, input.length - 1, input, period, begin, length, out);
		return out;
	}

	public double[] computeADX(double[] close, double[] high, double[] low, int period) {
		MInteger begin = new MInteger();
		MInteger length = new MInteger();
		double[] out = new double[close.length - period * 2 + 1];

		core.adx(0, close.length - 1, high, low, close, period, begin, length, out);
		return out;
	}

	/**
	 * 根据输入的adx数组判断趋势是否在形成当中
	 * 
	 * @param adx
	 * @param trendingGrayTH
	 *            判断趋势正在形成 adx的下限值, 比如20-25一般是趋势模糊的值
	 * @Param trendingTH 强趋势下限值, 一般25
	 * @param lookbackPeriod
	 *            从最后adx元素起, 往前取多少个元素做趋势形成判断
	 * 
	 * @return
	 */
	public boolean adxTrendForming(double[] adx, double trendingGrayTH, double trendingTH,
			int lookbackPeriod) {
		double[] adxSubset = Arrays.copyOfRange(adx, adx.length - lookbackPeriod, adx.length);
		double adxSum = 0;
		double directionTH = 0.5;

		// adx均值>trendingGrayTH, 总体向上, 有adx>trendingTH 3者满足2个当作形成中
		boolean hasAdxOverTH = false;
		double directionIndex = 0;
		for (int i = 0; i < adxSubset.length; i++) {
			adxSum += adxSubset[i];

			if (adxSubset[i] > trendingTH) {
				hasAdxOverTH = true;
			}

			if (i < adxSubset.length - 1 && adxSubset[i + 1] > adxSubset[i]) {
				directionIndex++;
			}
		}
		boolean avgOverTH = adxSum / lookbackPeriod >= trendingGrayTH;
		boolean hasUpDirection = directionIndex / lookbackPeriod > directionTH;

		theInformer.info("test trend forming " + Arrays.toString(adxSubset));
		theInformer.info("hasAdxOverTH " + hasAdxOverTH);
		theInformer.info("avgOverTH " + avgOverTH);
		theInformer.info("hasUpDirection " + hasUpDirection);

		int satisfied = 0;
		if (hasAdxOverTH) {
			satisfied++;
		}
		if (avgOverTH) {
			satisfied++;
		}
		if (hasUpDirection) {
			satisfied++;
		}
		return satisfied >= 2;
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
}
