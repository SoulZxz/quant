package com.ricequant.strategy.sample;

import com.ricequant.strategy.def.HPeriod;
import com.ricequant.strategy.def.IHInformer;
import com.ricequant.strategy.def.IHInitializers;
import com.ricequant.strategy.def.IHOrder;
import com.ricequant.strategy.def.IHStrategy;
import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

public class MultiRSIStrategy implements IHStrategy {

	Core iTalibCore;

	int count = 0;

	int RSI_low = 35;

	int RSI_high = 75;

	@Override
	public void init(IHInformer informer, IHInitializers initializers) {

		// 选择计算多少天的RSI
		int Period = 14;

		// 选择股票
		String stockId1 = "000001.XSHE";
		String stockId2 = "000024.XSHE";
		String stockId3 = "000068.XSHE";

		// 初始化部分

		MInteger Begin = new MInteger();
		MInteger Length = new MInteger();

		// 初始化TalibCore，需要用这个去做所有的talib的调用
		iTalibCore = new Core();

		// 加入股票的数据
		initializers.instruments((universe) -> universe.add(stockId1, stockId2,
				stockId3));

		initializers.events().statistics(
				(stats, info, trans) -> {

					count++;

					stats.each((stat) -> {

						String stockId = stat.getInstrument().getOrderBookID();

						// 储存进去对应的数组
						double[] closePxIn = stats.get(stockId)
								.history(Period + 1, HPeriod.Day)
								.getClosingPrice();
						double[] RSIOut = new double[Period + 1];

						if (count > Period) {

							RetCode RSICode = iTalibCore.rsi(0, Period,
									closePxIn, Period, Begin, Length, RSIOut);
							if ((RSICode == RetCode.Success)
									&& (Length.value >= 1)) {
								double RSI = RSIOut[Length.value - 1];

								double quantity = 800;
								double curPosition = info.position(stockId)
										.getNonClosedTradeQuantity();

								if (RSI > RSI_high) {
									if (curPosition > 0) {
										IHOrder sellOrder = trans.sell(stockId)
												.shares(curPosition).commit();
									}
								}
								if (RSI < RSI_low) {
									IHOrder buyOrder = trans.buy(stockId)
											.shares(quantity).commit();
								}
							}

							else {
								informer.error("ERROR");
							}

						}
					});

				});
	}

}
