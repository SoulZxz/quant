package com.ricequant.strategy.basic.trend.vb;

import static com.ricequant.strategy.basic.utils.MAComputer.MA_TYPE_SMA;
import static com.ricequant.strategy.basic.utils.MAComputer.computeMA;
import static com.ricequant.strategy.basic.utils.VolatilityComputer.computeATR;
import static com.ricequant.strategy.basic.utils.VolatilityComputer.computePriceChangeSTD;
import static com.ricequant.strategy.basic.utils.VolatilityComputer.computePriceSTD;

import com.ricequant.strategy.basic.EntrySignalGenerator;
import com.ricequant.strategy.def.HPeriod;
import com.ricequant.strategy.def.IHStatistics;

/**
 * 
 * Volatility Breakout
 *
 */
public class VBEntrySignalGenerator implements EntrySignalGenerator {

	private static final int VMT_PRICE_CHANGE_STD = 1;

	private static final int VMT_PRICE_STD = 2;

	private static final int VMT_ATR = 3;

	private static final int RVT_PREVIOUS_CLOSE = 1;

	private static final int RVT_CURRENT_OPEN = 2;

	private static final int RVT_CLOSE_MA = 3;

	private int period;

	private int refType;

	private int vmType;

	private double volatilityMultiplier;

	public VBEntrySignalGenerator(int period, int refType, int vmType, double volatilityMultiplier) {
		super();
		this.period = period;
		this.refType = refType;
		this.vmType = vmType;
		this.volatilityMultiplier = volatilityMultiplier;
	}

	@Override
	public double generateSignal(IHStatistics stat) {

		double[] close = stat.history(period, HPeriod.Day).getClosingPrice();
		double[] open = stat.history(period, HPeriod.Day).getOpeningPrice();
		double[] high = stat.history(period, HPeriod.Day).getHighPrice();
		double[] low = stat.history(period, HPeriod.Day).getLowPrice();

		double refValue = computeReferenceValue(open, close, refType);
		double volatilityMeasure = computeVolatilityMeasure(open, close, high, low, vmType);

		double upperTrigger = refValue + volatilityMultiplier * volatilityMeasure;
		double lowerTrigger = refValue - volatilityMultiplier * volatilityMeasure;

		double todayClose = close[close.length - 1];
		// Buy when today’s close is greater than the upper trigger
		if (todayClose > upperTrigger) {
			return 1;
		}
		// Sell when today’s close is less than the lower trigger
		else if (todayClose < lowerTrigger) {
			return -1;
		} else {
			return 0;
		}
	}

	public double computeVolatilityMeasure(double[] open, double[] close, double[] high,
			double[] low, int volatilityMeasureType) {
		switch (volatilityMeasureType) {
		case VMT_PRICE_CHANGE_STD:
			return computePriceChangeSTD(open, close);
		case VMT_PRICE_STD:
			return computePriceSTD(close);
		case VMT_ATR:
			return computeATR(close, high, low);
		default:
			throw new RuntimeException("unsupported VolatilityMeasureType " + volatilityMeasureType);
		}
	}

	public double computeReferenceValue(double[] open, double[] close, int referenceValueType) {
		switch (referenceValueType) {
		case RVT_PREVIOUS_CLOSE:
			return close[close.length - 2];
		case RVT_CURRENT_OPEN:
			return open[open.length - 1];
		case RVT_CLOSE_MA:
			int maPeriod = 5;
			double[] sma = computeMA(close, maPeriod, MA_TYPE_SMA);
			return sma[sma.length - 1];
		default:
			throw new RuntimeException("unsupported ReferenceValueType " + referenceValueType);
		}
	}

}
