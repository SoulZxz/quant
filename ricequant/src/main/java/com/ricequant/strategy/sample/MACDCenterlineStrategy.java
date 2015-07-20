package com.ricequant.strategy.sample;

import java.util.ArrayList;
import java.util.List;

import com.ricequant.strategy.def.HPeriod;
import com.ricequant.strategy.def.IHInformer;
import com.ricequant.strategy.def.IHInitializers;
import com.ricequant.strategy.def.IHStatisticsHistory;
import com.ricequant.strategy.def.IHStrategy;
import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;

public class MACDCenterlineStrategy implements IHStrategy {

	int day = 0;

	@Override
	public void init(IHInformer informer, IHInitializers initializers) {

		int period = 35;
		int fastPeriod = 12;
		int slowPeriod = 26;
		int signalPeriod = 9;

		String stockCode = "000024.XSHE";

		initializers.instruments((universe) -> {
			return universe.add(stockCode);
		});

		// 允许卖空
		// initializers.shortsell().allow();

		initializers.events().statistics(
				(stats, info, trans) -> {
					day++;
					stats.each((stat) -> {
						IHStatisticsHistory history = stat.history(period, HPeriod.Day);
						double[] close = history.getClosingPrice();

						List<Double[]> result = MACDCenterlineStrategy.this.compute(close,
								fastPeriod, slowPeriod, signalPeriod, informer);

						Double[] current = result.get(result.size() - 1);
						Double[] yesterday = result.get(result.size() - 2);
						double currentMACD = current[0];
						double yesterdayMACD = yesterday[0];

						double allCash = info.portfolio().getAvailableCash();
						if (currentMACD > 0 && yesterdayMACD < 0) {
							informer.info("allCash " + allCash);
							trans.buy(stockCode).percent(100).commit();
							informer.info("day " + day + " buy "
									+ info.position(stockCode).getNonClosedTradeQuantity());
						}
						else if (currentMACD < 0 && yesterdayMACD > 0) {
							informer.info("day " + day + " sell "
									+ info.position(stockCode).getNonClosedTradeQuantity());
							trans.sell(stockCode)
									.shares(info.position(stockCode).getNonClosedTradeQuantity())
									.commit();
						} else {

						}
					});
				});
	}

	public List<Double[]> compute(double[] close, int fastPeriod, int slowPeriod, int signalPeriod,
			IHInformer informer) {
		MInteger begin = new MInteger();
		MInteger length = new MInteger();
		int resultLegnth = close.length - (slowPeriod - 1) - (signalPeriod - 1);
		double[] values = new double[resultLegnth];
		double[] signals = new double[resultLegnth];
		double[] histograms = new double[resultLegnth];
		List<Double[]> result = new ArrayList<Double[]>();

		Core core = new Core();
		core.macd(0, close.length - 1, close, fastPeriod, slowPeriod, signalPeriod, begin, length,
				values, signals, histograms);

		for (int i = 0; i < resultLegnth; i++) {
			result.add(new Double[] { values[i], signals[i], histograms[i] });
		}

		return result;
	}
}