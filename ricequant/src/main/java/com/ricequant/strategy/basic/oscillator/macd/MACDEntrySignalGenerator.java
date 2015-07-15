package com.ricequant.strategy.basic.oscillator.macd;

import com.ricequant.strategy.basic.EntrySignalGenerator;
import com.ricequant.strategy.basic.Signal;
import com.ricequant.strategy.def.IHStatistics;

/**
 * Interpretation
 * 
 * As with MACD, the MACD-Histogram is also designed to identify convergence,
 * divergence and crossovers. The MACD-Histogram, however, is measuring the
 * distance between MACD and its signal line. The histogram is positive when
 * MACD is above its signal line. Positive values increase as MACD diverges
 * further from its signal line (to the upside). Positive values decrease as
 * MACD and its signal line converge. The MACD-Histogram crosses the zero line
 * as MACD crosses below its signal line. The indicator is negative when MACD is
 * below its signal line. Negative values increase as MACD diverges further from
 * its signal line (to the downside). Conversely, negative values decrease as
 * MACD converges on its signal line.
 */
public class MACDEntrySignalGenerator implements EntrySignalGenerator {

	@Override
	public Signal generateSignal(IHStatistics stat) {
		// TODO Auto-generated method stub
		return null;
	}

}
