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

public class ADXInspector implements IHStrategy {

	private IHInformer theInformer;

	private Core core = new Core();

	/** adx **/
	private int adxSmoothingPeriod = 150;

	private int adxPeriod = 14;

	private int adxTrendFormingLookbackPeriod = 5;

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
				ADXInspector.this.eaWork(stats.get(stockCode), info, trans);
			});
		});
	}

	public void eaWork(IHStatistics stat, IHInfoPacks info, IHTransactionFactory trans) {
		IHStatisticsHistory history = stat.history(adxSmoothingPeriod, HPeriod.Day);
		double[] adx = this.computeADX(history.getClosingPrice(), history.getHighPrice(),
				history.getLowPrice(), adxPeriod);

		double[] subset = Arrays.copyOfRange(adx, adx.length - adxTrendFormingLookbackPeriod,
				adx.length);
		theInformer.info("adx " + Arrays.toString(subset));

		theInformer.plot("CLOSING", stat.getClosingPrice());
	}

	public double[] computeADX(double[] close, double[] high, double[] low, int period) {
		MInteger begin = new MInteger();
		MInteger length = new MInteger();
		double[] out = new double[close.length - period * 2 + 1];

		core.adx(0, close.length - 1, high, low, close, period, begin, length, out);
		return out;
	}
}
