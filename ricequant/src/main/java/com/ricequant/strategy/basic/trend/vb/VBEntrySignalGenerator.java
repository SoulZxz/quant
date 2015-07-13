package com.ricequant.strategy.basic.trend.vb;

import com.ricequant.strategy.basic.EntrySignalGenerator;
import com.ricequant.strategy.basic.Signal;
import com.ricequant.strategy.def.HPeriod;
import com.ricequant.strategy.def.IHStatistics;

/**
 * 
 * Volatility Breakout
 *
 */
public class VBEntrySignalGenerator implements EntrySignalGenerator {

	private int period;

	private ReferenceValueType refType;

	private VolatilityMeasureType vmType;

	private double volatilityMultiplier;

	public VBEntrySignalGenerator(int period, ReferenceValueType refType,
			VolatilityMeasureType vmType, double volatilityMultiplier) {
		super();
		this.period = period;
		this.refType = refType;
		this.vmType = vmType;
		this.volatilityMultiplier = volatilityMultiplier;
	}

	@Override
	public Signal generateSignal(IHStatistics stat) {

		double[] close = stat.history(period, HPeriod.Day).getClosingPrice();
		double[] open = stat.history(period, HPeriod.Day).getOpeningPrice();
		double[] high = stat.history(period, HPeriod.Day).getHighPrice();
		double[] low = stat.history(period, HPeriod.Day).getLowPrice();

		VolatilityComputer computer = new VolatilityComputer();
		double refValue = computer.computeReferenceValue(open, close, refType);
		double volatilityMeasure = computer
				.computeVolatilityMeasure(open, close, high, low, vmType);

		double upperTrigger = refValue + volatilityMultiplier * volatilityMeasure;
		double lowerTrigger = refValue - volatilityMultiplier * volatilityMeasure;

		double todayClose = close[close.length - 1];
		// Buy when today’s close is greater than the upper trigger
		if (todayClose > upperTrigger) {
			return new Signal(1);
		}
		// Sell when today’s close is less than the lower trigger
		else if (todayClose < lowerTrigger) {
			return new Signal(-1);
		} else {
			return new Signal(0);
		}
	}

}
