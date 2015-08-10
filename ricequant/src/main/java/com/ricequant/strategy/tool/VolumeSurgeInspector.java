package com.ricequant.strategy.tool;

import java.util.Arrays;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import com.ricequant.strategy.def.HPeriod;
import com.ricequant.strategy.def.IHInfoPacks;
import com.ricequant.strategy.def.IHInformer;
import com.ricequant.strategy.def.IHInitializers;
import com.ricequant.strategy.def.IHStatistics;
import com.ricequant.strategy.def.IHStatisticsHistory;
import com.ricequant.strategy.def.IHStrategy;
import com.ricequant.strategy.def.IHTransactionFactory;

public class VolumeSurgeInspector implements IHStrategy {

	private IHInformer theInformer;

	/** volatility **/
	private int volumeVolatitilyPeriod = 40;

	private double volatilityTH = 1;

	private int volumeVolatitilyLookbackPeriod = 5;

	/** other **/
	private String stockCode = "000528.XSHE";

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
				VolumeSurgeInspector.this.eaWork(stats.get(stockCode), info, trans);
			});
		});
	}

	public void eaWork(IHStatistics stat, IHInfoPacks info, IHTransactionFactory trans) {
		this.lookForVolumeVolatilityDisturbance(stat, info);

		theInformer.plot("CLOSING", stat.getClosingPrice());
	}

	public void lookForVolumeVolatilityDisturbance(IHStatistics stat, IHInfoPacks info) {
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
}
