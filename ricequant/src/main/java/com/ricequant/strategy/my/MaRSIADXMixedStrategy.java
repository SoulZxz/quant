package com.ricequant.strategy.my;

import java.util.Arrays;

import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import com.ricequant.strategy.def.HPeriod;
import com.ricequant.strategy.def.IHInfoPacks;
import com.ricequant.strategy.def.IHInformer;
import com.ricequant.strategy.def.IHInitializers;
import com.ricequant.strategy.def.IHPortfolio;
import com.ricequant.strategy.def.IHStatistics;
import com.ricequant.strategy.def.IHStatisticsHistory;
import com.ricequant.strategy.def.IHStrategy;
import com.ricequant.strategy.def.IHTransactionFactory;
import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;

/**
 * 短线参数 rsiPeriod = 10, adxPeriod = 10, shortPeriod = 5, longPeriod = 10,
 * maRecentPeriod = 1
 *
 * 中线 rsiPeriod = 14, adxPeriod = 14, shortPeriod = 14, longPeriod = 40,
 * maRecentPeriod = 5
 */
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
	private int shortPeriod = 14;

	private int longPeriod = 40;

	private int shortPeriodType = MA_TYPE_SMA;

	private int longPeriodType = MA_TYPE_SMA;

	private int maRecentPeriod = 5;

	/** volatility **/
	private int volumeVolatitilyPeriod = 40;

	private double volatilityTH = 1;

	private int volumeVolatitilyLookbackPeriod = 5;

	/** exit **/
	private double closingChangeSTD;

	private double closingChangeMean;

	private double highestUnclosedProfitHeld;

	private double currentUnclosedProfitHeld;

	private double unclosedPositionInitValue;

	private double lossTrigger = -0.05;

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

		computeClosingChangeSTD(stat.history(longPeriod, HPeriod.Day).getClosingPrice());
		theInformer.info("closingChangeSTD " + closingChangeSTD + " closingChangeMean "
				+ closingChangeMean);

		double rsiSignal = generateRSISignal(stat);

		if (rsiSignal != 0) {
			int direction = rsiSignal > 0 ? 1 : -1;
			theInformer.info("rsi trade " + direction + " adx " + currentAdx);
			boolean confirmedNoTrend = currentAdx < adxTrendingGrayTH;
			Boolean trendForming = null;
			if (currentAdx > adxTrendingGrayTH && currentAdx < adxTrendingTH) {
				trendForming = adxTrendForming(adx, adxTrendingGrayTH, adxTrendingTH,
						adxTrendFormingLookbackPeriod);
			}

			Boolean trendWaning = null;
			if (currentAdx >= adxTrendingTH) {
				trendWaning = adxTrendWaning(adx, adxTrendingGrayTH, adxTrendingTH,
						adxTrendFormingLookbackPeriod);
			}

			if (!filterEnabled || confirmedNoTrend || Boolean.FALSE.equals(trendForming)
					|| Boolean.TRUE.equals(trendWaning)) {
				this.entry(trans, info, stat, stockCode, direction);
			}

			if (!filterEnabled || Boolean.TRUE.equals(trendForming)
					|| Boolean.FALSE.equals(trendWaning)) {
				this.entry(trans, info, stat, stockCode, -direction);
			}
		}

		double maSignal = this.generateMASignal(stat);
		if (maSignal != 0) {
			int direction = maSignal > 0 ? 1 : -1;
			theInformer.info("ma trade " + direction + " adx " + currentAdx);

			Boolean trendForming = null;
			if (currentAdx > adxTrendingGrayTH && currentAdx < adxTrendingTH) {
				trendForming = adxTrendForming(adx, adxTrendingGrayTH, adxTrendingTH,
						adxTrendFormingLookbackPeriod);
			}

			Boolean trendWaning = null;
			if (currentAdx >= adxTrendingTH) {
				trendWaning = adxTrendWaning(adx, adxTrendingGrayTH, adxTrendingTH,
						adxTrendFormingLookbackPeriod);
			}

			if (!filterEnabled || Boolean.TRUE.equals(trendForming)
					|| Boolean.FALSE.equals(trendWaning)) {
				this.entry(trans, info, stat, stockCode, direction);
			}
		}

		double exitSignal = this.generateExitSignal(stat, info.portfolio());
		// 出现止损信号
		if (exitSignal < 0) {
			theInformer.info("exitSignal " + exitSignal + " adx " + currentAdx);
			if (hasVolumeVolatilityDisturbance(stat, info)) {
				this.exit(trans, info, stockCode);
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
		double[] closeShort = stat.history(shortPeriod + maRecentPeriod, HPeriod.Day)
				.getClosingPrice();
		double[] closeLong = stat.history(longPeriod + maRecentPeriod, HPeriod.Day)
				.getClosingPrice();

		// 计算短期MA
		double[] shortMA = computeMA(closeShort, shortPeriod, shortPeriodType);
		// 计算长期MA
		double[] longMA = computeMA(closeLong, longPeriod, longPeriodType);

		// 计算当天的短期MA与长期MA的差值
		int crossed = 0;

		double delta = shortMA[0] - longMA[0];
		for (int i = 0; i < shortMA.length; i++) {
			double previousDelta = delta;
			delta = shortMA[i] - longMA[i];

			// 短期MA与长期MA值出现交叉, 短期MA处于下降趋势, 长期MA处于上升趋势
			// 简单起见信号强度绝对值统一设置成1
			if ((previousDelta > 0) && delta < 0) {
				crossed = crossed - 1;
			}
			// 短期MA与长期MA值出现交叉, 短期MA处于上升趋势,长期MA处于下降趋势
			else if ((previousDelta < 0) && delta > 0) {
				crossed = crossed + 1;
			}
		}

		// TODO 加入whipsaws detection
		if (crossed < 0) {
			return -1;
		} else if (crossed > 0) {
			return 1;
		}
		// 什么也不做
		else {
			return 0;
		}
	}

	public double generateExitSignal(IHStatistics stat, IHPortfolio portfolio) {
		if (unclosedPositionInitValue == 0) {
			return 0;
		}

		currentUnclosedProfitHeld = currentUnclosedProfitHeld + portfolio.getProfitAndLoss();
		highestUnclosedProfitHeld = Math.max(highestUnclosedProfitHeld, currentUnclosedProfitHeld);

		double profitAndLossRate = currentUnclosedProfitHeld / unclosedPositionInitValue;
		double highestProfitRate = highestUnclosedProfitHeld / unclosedPositionInitValue;

		// 达到盈利点, 发止盈信号; 从最高盈利点发生drawdown达到lossTrigger, 止损或止浮盈
		if (profitAndLossRate - highestProfitRate >= lossTrigger) {
			return 0;
		} else {
			if (profitAndLossRate - highestProfitRate < lossTrigger) {
				return -1;
			} else {
				return 0;
			}
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

	public boolean adxTrendBoosting(double[] adx, double coeffTH) {
		double[] normalized = normalize(adx);

		double[] coeffs = linearFitting(normalized);
		boolean constantSpeed = coeffs[1] >= coeffTH;

		boolean upSpeed = linearFitting(splineDerivatives(normalized))[1] > 0;

		return constantSpeed || upSpeed;
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
		boolean adxTrendBoosting = adxTrendBoosting(adxSubset, 1);

		theInformer.info("test trend forming " + Arrays.toString(adxSubset));
		theInformer.info("hasAdxOverTH " + hasAdxOverTH);
		theInformer.info("avgOverTH " + avgOverTH);
		theInformer.info("hasUpDirection " + hasUpDirection);
		theInformer.info("adxTrendBoosting " + adxTrendBoosting);

		int satisfied = 0;
		if (hasAdxOverTH || avgOverTH) {
			satisfied++;
		}
		if (hasUpDirection) {
			satisfied++;
		}
		if (adxTrendBoosting) {
			satisfied++;
		}
		return satisfied >= 2;
	}

	public boolean adxTrendFalling(double[] adx, double coeffTH) {
		double max = adx[0];
		for (double adxValue : adx) {
			max = Math.max(max, adxValue);
		}

		// normalize
		WeightedObservedPoints points = new WeightedObservedPoints();
		for (int i = 0; i < adx.length; i++) {
			points.add(i, adx[i] - max);
		}

		// 1阶拟合
		PolynomialCurveFitter fitter = PolynomialCurveFitter.create(1);
		return fitter.fit(points.toList())[1] <= coeffTH;
	}

	public boolean adxTrendWaning(double[] adx, double trendingGrayTH, double trendingTH,
			int lookbackPeriod) {
		double[] adxSubset = Arrays.copyOfRange(adx, adx.length - lookbackPeriod, adx.length);
		double adxSum = 0;
		double directionTH = 0.5;

		// adx均值<trendingTH, 总体向下, 有adx<trendingGrayTH, adx快速下降 4者满足2个当作衰减中
		boolean hasAdxBelowTH = false;
		double directionIndex = 0;
		for (int i = 0; i < adxSubset.length - 1; i++) {
			adxSum += adxSubset[i];

			if (adxSubset[i] < trendingGrayTH) {
				hasAdxBelowTH = true;
			}

			if (adxSubset[i + 1] < adxSubset[i]) {
				directionIndex++;
			}
		}
		boolean avgBelowTH = adxSum / lookbackPeriod <= trendingTH;
		boolean hasDownDirection = directionIndex / lookbackPeriod > directionTH;
		boolean adxTrendFalling = adxTrendFalling(adxSubset, -1);

		theInformer.info("test trend waning " + Arrays.toString(adxSubset));
		theInformer.info("hasAdxBelowTH " + hasAdxBelowTH);
		theInformer.info("avgBelowTH " + avgBelowTH);
		theInformer.info("hasDownDirection " + hasDownDirection);
		theInformer.info("adxTrendFalling " + adxTrendFalling);

		int satisfied = 0;
		if (hasAdxBelowTH || avgBelowTH) {
			satisfied++;
		}
		if (hasDownDirection) {
			satisfied++;
		}
		if (adxTrendFalling) {
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

	public void entry(IHTransactionFactory trans, IHInfoPacks info, IHStatistics stat,
			String stockCode, int direction) {
		int nonClosed = (int) info.position(stockCode).getNonClosedTradeQuantity();
		int directionalNonClosed = direction * nonClosed;

		// 如果有反方向的头寸, 先平掉
		if (directionalNonClosed < 0) {
			if (nonClosed < 0) {
				// 平掉空头寸
				trans.buy(stockCode).shares(-nonClosed).commit();
				// 开多头寸
				trans.buy(stockCode).percent(100).commit();
				unclosedPositionInitValue = info.position(stockCode).getNonClosedTradeQuantity()
						* stat.getLastPrice();
				currentUnclosedProfitHeld = 0;
				highestUnclosedProfitHeld = 0;
			} else {
				// 平掉多头寸
				trans.sell(stockCode).shares(nonClosed).commit();
				// 开空头寸
				if (allowShortSell) {
					trans.sell(stockCode).percent(100).commit();
					unclosedPositionInitValue = -info.position(stockCode)
							.getNonClosedTradeQuantity() * stat.getLastPrice();
					currentUnclosedProfitHeld = 0;
					highestUnclosedProfitHeld = 0;
				}
			}
		}
		// 如果没持有头寸, 开
		else if (directionalNonClosed == 0) {
			if (direction > 0) {
				// 开多头寸
				trans.buy(stockCode).percent(100).commit();
				unclosedPositionInitValue = info.position(stockCode).getNonClosedTradeQuantity()
						* stat.getLastPrice();
				currentUnclosedProfitHeld = 0;
				highestUnclosedProfitHeld = 0;
			} else if (direction < 0) {
				// 开空头寸
				if (allowShortSell) {
					trans.sell(stockCode).percent(100).commit();
					unclosedPositionInitValue = -info.position(stockCode)
							.getNonClosedTradeQuantity() * stat.getLastPrice();
					currentUnclosedProfitHeld = 0;
					highestUnclosedProfitHeld = 0;
				}
			}
		}
	}

	public void exit(IHTransactionFactory trans, IHInfoPacks info, String stockCode) {
		int nonClosed = (int) info.position(stockCode).getNonClosedTradeQuantity();
		if (nonClosed < 0) {
			// 平掉空头寸
			trans.buy(stockCode).shares(-nonClosed).commit();
			unclosedPositionInitValue = 0;
			currentUnclosedProfitHeld = 0;
			highestUnclosedProfitHeld = 0;
		} else if (nonClosed > 0) {
			// 平掉多头寸
			trans.sell(stockCode).shares(nonClosed).commit();
			unclosedPositionInitValue = 0;
			currentUnclosedProfitHeld = 0;
			highestUnclosedProfitHeld = 0;
		}
	}

	public void computeClosingChangeSTD(double[] close) {
		SummaryStatistics stats = new SummaryStatistics();
		for (int i = 0; i < close.length - 1; i++) {
			stats.addValue((close[i + 1] - close[i]) / close[i]);
		}

		closingChangeMean = stats.getMean();
		closingChangeSTD = stats.getStandardDeviation();
	}

	public boolean hasVolumeVolatilityDisturbance(IHStatistics stat, IHInfoPacks info) {
		IHStatisticsHistory history = stat.history(volumeVolatitilyPeriod, HPeriod.Day);
		double[] result = this.computeVolatilityArray(history.getTurnoverVolume(),
				volumeVolatitilyLookbackPeriod);

		double maxVolatility = 0;
		int maxVolatilityPosition = -1;
		for (int i = 0; i < result.length; i++) {
			double value = result[i];
			if (value >= volatilityTH) {
				maxVolatility = Math.max(maxVolatility, value);
				maxVolatilityPosition = i;
			}
		}

		boolean hasVolatilityTideFall = false;
		if (maxVolatilityPosition != -1) {
			hasVolatilityTideFall = true;
			theInformer.info("volumn volatility " + Arrays.toString(result)
					+ " hasVolatilityTideFall " + hasVolatilityTideFall);
		}
		return hasVolatilityTideFall;
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

	public double[] normalize(double[] input) {
		// 正则化到0-1之间
		double yMin = input[0];
		double yMax = input[0];

		for (double value : input) {
			yMin = Math.min(yMin, value);
			yMax = Math.max(yMax, value);
		}

		double[] normalized = new double[input.length];
		for (int i = 0; i < input.length; i++) {
			normalized[i] = (input[i] - yMin) / (yMax - yMin);
		}

		return normalized;
	}

	/**
	 * 1阶线性拟合
	 *
	 * @param input
	 *            正则化过的数组
	 * @return 拟合系数数组
	 */
	public double[] linearFitting(double[] input) {
		double xStep = 1.0 / (input.length - 1);

		// normalize
		WeightedObservedPoints points = new WeightedObservedPoints();
		for (int i = 0; i < input.length; i++) {
			points.add(i * xStep, input[i]);
		}

		// 1阶拟合, 判断是否以大于coeffTH的斜率匀速上升
		PolynomialCurveFitter fitter = PolynomialCurveFitter.create(1);
		return fitter.fit(points.toList());
	}

	/**
	 * 计算分段插值拟合的导数值
	 *
	 * @param input
	 *            正则化过的数组
	 * @return
	 */
	public double[] splineDerivatives(double[] input) {
		double xStep = 1.0 / (input.length - 1);

		double[] x = new double[input.length];
		double[] y = new double[input.length];

		for (int i = 0; i < input.length; i++) {
			x[i] = i * xStep;
			y[i] = input[i];
		}

		SplineInterpolator fitter = new SplineInterpolator();
		PolynomialSplineFunction func = fitter.interpolate(x, y);

		double[] derivatives = new double[input.length];
		for (int i = 0; i < derivatives.length; i++) {
			derivatives[i] = func.derivative().value(x[i]);
		}

		return derivatives;
	}
}
