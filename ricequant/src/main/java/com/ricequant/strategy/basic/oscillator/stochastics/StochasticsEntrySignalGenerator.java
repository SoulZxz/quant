package com.ricequant.strategy.basic.oscillator.stochastics;

import com.ricequant.strategy.basic.EntrySignalGenerator;
import com.ricequant.strategy.basic.Signal;
import com.ricequant.strategy.def.IHStatistics;

/**
 * Fast, Slow or Full
 * 
 * There are three versions of the Stochastic Oscillator available on
 * SharpCharts. The Fast Stochastic Oscillator is based on George Lane's
 * original formulas for %K and %D. %K in the fast version that appears rather
 * choppy. %D is the 3-day SMA of %K. In fact, Lane used %D to generate buy or
 * sell signals based on bullish and bearish divergences. Lane asserts that a %D
 * divergence is the “only signal which will cause you to buy or sell.” Because
 * %D in the Fast Stochastic Oscillator is used for signals, the Slow Stochastic
 * Oscillator was introduced to reflect this emphasis. The Slow Stochastic
 * Oscillator smooths %K with a 3-day SMA, which is exactly what %D is in the
 * Fast Stochastic Oscillator. Notice that %K in the Slow Stochastic Oscillator
 * equals %D in the Fast Stochastic Oscillator (chart 2).
 * 
 * Fast Stochastic Oscillator:
 * 
 * Fast %K = %K basic calculation
 * 
 * Fast %D = 3-period SMA of Fast %K = Slow %K;
 * 
 * Slow Stochastic Oscillator:
 * 
 * Slow %K = Fast %K smoothed with 3-period SMA = Fast %D
 * 
 * Slow %D = 3-period SMA of Slow %K
 * 
 * </br> The Full Stochastic Oscillator is a fully customizable version of the
 * Slow Stochastic Oscillator. Users can set the look-back period, the number of
 * periods to slow %K and the number of periods for the %D moving average. The
 * default parameters were used in these examples: Fast Stochastic Oscillator
 * (14,3), Slow Stochastic Oscillator (14,3) and Full Stochastic Oscillator
 * (14,3,3).
 * 
 * Full Stochastic Oscillator:
 * 
 * Full %K = Fast %K smoothed with X-period SMA
 * 
 * Full %D = X-period SMA of Full %K;
 * 
 * Long positions are typically taken when the %K stochastic rises above 30 and
 * accompanied by a cross above the %D stochastic. Short positions are typically
 * taken when the %K stochastic falls below 80 and is accompanied by a cross
 * below the %D stochastic
 */
public class StochasticsEntrySignalGenerator implements EntrySignalGenerator {

	@Override
	public Signal generateSignal(IHStatistics stat) {
		// Core core = new Core();
		// core.stoch(startIdx, endIdx, inHigh, inLow, inClose,
		// optInFastK_Period, optInSlowK_Period, optInSlowK_MAType,
		// optInSlowD_Period, optInSlowD_MAType, outBegIdx, outNBElement,
		// outSlowK, outSlowD)
		return null;
	}
}
