package com.ricequant.strategy.sample;

import com.ricequant.strategy.def.HPeriod;
import com.ricequant.strategy.def.IHInformer;
import com.ricequant.strategy.def.IHInitializers;
import com.ricequant.strategy.def.IHOrder;
import com.ricequant.strategy.def.IHStrategy;
import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

public class SimpleMAStrategy implements IHStrategy {
	// 需要用这个去做所有的talib的调用
	Core iTalibCore;

	int count = 0;

	// 记录MA(10)与MA(60)的差值
	static double delta = 0;

	@Override
	public void init(IHInformer informer, IHInitializers initializers) {

		// 选择计算多少天的平均值
		int shortPeriod = 10;
		int longPeriod = 60;

		// 初始化部分
		double[] shortOut = new double[shortPeriod];
		double[] longOut = new double[longPeriod];
		MInteger shortBegin = new MInteger();
		MInteger shortLength = new MInteger();
		MInteger longBegin = new MInteger();
		MInteger longLength = new MInteger();

		// 初始化TalibCore，需要用这个去做所有的talib的调用
		iTalibCore = new Core();

		// 选择股票
		String stockCode = "000001.XSHE";
		// 加入股票的数据
		initializers.instruments((universe) -> universe.add(stockCode));

		initializers.events().statistics((stats, info, trans) -> {

			count++;
			// 储存进去对应的数组
				double[] closePxShortIn = stats.get(stockCode)
						.history(shortPeriod, HPeriod.Day).getClosingPrice();
				double[] closePxLongIn = stats.get(stockCode)
						.history(longPeriod, HPeriod.Day).getClosingPrice();

				if (count >= longPeriod) {

					// 计算股票的MA(10)
					RetCode shortRetCode = iTalibCore.sma(0, shortPeriod - 1,
							closePxShortIn, shortPeriod, shortBegin,
							shortLength, shortOut);
					// 计算股票的MA(60)
					RetCode longRetCode = iTalibCore.sma(0, longPeriod - 1,
							closePxLongIn, longPeriod, longBegin, longLength,
							longOut);
					if ((shortRetCode == RetCode.Success)
							&& (longRetCode == RetCode.Success)) {
						// 计算当天的MA(10)与MA(60)的差值
						double preDelta = delta;
						delta = shortOut[shortLength.value - 1]
								- longOut[longLength.value - 1];

						// 用户当前持有的股数
						double curPosition = info.position(stockCode)
								.getNonClosedTradeQuantity();
						// 用户用当前的资金能买到的股数
						double quantity = info.portfolio().getAvailableCash()
								/ stats.get(stockCode).getLastPrice();

						informer.plot("MA10", shortOut[0]);
						informer.plot("MA60", longOut[0]);
						// MA(10)与MA(60)值出现交叉, MA(10)处于下降趋势, MA(60)处于上升趋势
						if ((preDelta > 0) && delta < 0) {
							if (curPosition > 0) {
								IHOrder sellOrder = trans.sell(stockCode)
										.shares(curPosition).commit();
							}
						}
						// MA(10)与MA(60)值出现交叉, MA(10)处于上升趋势, MA(60)处于下降趋势
						if ((preDelta < 0) && delta > 0) {
							IHOrder buyOrder = trans.buy(stockCode)
									.shares(quantity).commit();
						}

					} else {
						informer.error("ERROR");
					}
				}
			});
	}
}
