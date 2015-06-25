package com.ricequant.strategy.sample;

import com.ricequant.strategy.def.HPeriod;
import com.ricequant.strategy.def.IHInformer;
import com.ricequant.strategy.def.IHInitializers;
import com.ricequant.strategy.def.IHOrder;
import com.ricequant.strategy.def.IHStrategy;
import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

public class MACDStrategy implements IHStrategy {

	// 需要用这个去做所有的talib的调用
	Core iTalibCore;

	int count = 0;

	// 记录MA(short)与MA(long)的差值
	static double delta = 0;

	@Override
	public void init(IHInformer informer, IHInitializers initializers) {

		// 选择计算多少天的平均值
		int shortPeriod = 12;
		int longPeriod = 26;
		int smoothingPeriod = 9;
		int observePeriod = 100;

		// 初始化部分
		double[] shortOut = new double[shortPeriod];
		double[] longOut = new double[longPeriod];
		double[] shortemaOut = new double[observePeriod];
		double[] longemaOut = new double[observePeriod];
		double[] MACDOut = new double[observePeriod];
		double[] MACDSignal = new double[observePeriod];
		double[] MACDHist = new double[observePeriod];
		MInteger shortBegin = new MInteger();
		MInteger shortLength = new MInteger();
		MInteger longBegin = new MInteger();
		MInteger longLength = new MInteger();
		MInteger shortemaBegin = new MInteger();
		MInteger shortemaLength = new MInteger();
		MInteger longemaBegin = new MInteger();
		MInteger longemaLength = new MInteger();
		MInteger MACDBegin = new MInteger();
		MInteger MACDLength = new MInteger();

		// 默认已经做了slippage - 0.246% 和commission - 0.08% + 印花税0.1% 的交易费计算

		// 初始化TalibCore，需要用这个去做所有的talib的调用
		iTalibCore = new Core();

		// 选择股票
		String stockId = "000001.XSHE";

		// 加入股票的数据
		initializers.instruments((instruments) -> instruments.add(stockId));

		initializers.events().statistics((stats, info, trans) -> {

			count++;
			// 储存进去对应的数组
				double[] closePxShortIn = stats.get(stockId)
						.history(shortPeriod, HPeriod.Day).getClosingPrice();
				double[] closePxLongIn = stats.get(stockId)
						.history(longPeriod, HPeriod.Day).getClosingPrice();
				double[] closePxemaShortIn = stats.get(stockId)
						.history(observePeriod, HPeriod.Day).getClosingPrice();
				double[] closePxemaLongIn = stats.get(stockId)
						.history(observePeriod, HPeriod.Day).getClosingPrice();

				if (count > observePeriod) {
					RetCode shortRetCode = iTalibCore.sma(0, shortPeriod - 1,
							closePxShortIn, shortPeriod, shortBegin,
							shortLength, shortOut);

					RetCode longRetCode = iTalibCore.sma(0, longPeriod - 1,
							closePxLongIn, longPeriod, longBegin, longLength,
							longOut);

					RetCode shortemaRetCode = iTalibCore.ema(0,
							observePeriod - 1, closePxemaShortIn, shortPeriod,
							shortemaBegin, shortemaLength, shortemaOut);

					RetCode longemaRetCode = iTalibCore.ema(0,
							observePeriod - 1, closePxemaLongIn, longPeriod,
							longemaBegin, longemaLength, longemaOut);

					RetCode MACDRetCode = iTalibCore.macd(0, observePeriod - 1,
							closePxemaLongIn, shortPeriod, longPeriod,
							smoothingPeriod, MACDBegin, MACDLength, MACDOut,
							MACDSignal, MACDHist);

					if ((shortRetCode == RetCode.Success)
							&& (longRetCode == RetCode.Success)) {
						// 画自定义时间序列图
						informer.plot("MACD", MACDOut[MACDLength.value - 1]);
						informer.plot("MACDSignal",
								MACDSignal[MACDLength.value - 1]);
						informer.plot("Zeroline", 0);

						double preDelta = delta;
						// MACD golden cross
						// delta = MACDOut[MACDLength.value - 1] -
						// MACDSignal[MACDLength.value - 1];

						// simple moving average golden cross
						// delta = shortOut[shortLength.value-1] -
						// longOut[longLength.value-1];

						// EMA golden cross
						// delta = shortemaOut[shortemaLength.value - 1]-
						// longemaOut[longthemaLength.value-1];

						// MACD zero line cross
						delta = MACDOut[MACDLength.value - 1] - 0;

						// 用户当前持有的股数
						double curPosition = info.position(stockId)
								.getNonClosedTradeQuantity();
						// 用户用当前的资金能买到的股数
						double quantity = info.portfolio().getAvailableCash()
								/ stats.get(stockId).getLastPrice();

						// MA(short)与MA(long)值出现交叉, MA(short)处于下降趋势,
						// MA(long)处于上升趋势
						if ((preDelta > 0) && delta < 0) {
							if (curPosition > 0) {
								IHOrder sellOrder = trans.sell(stockId)
										.shares(curPosition).commit();
							}
						}
						// MA(short)与MA(long)值出现交叉, MA(short)处于上升趋势,
						// MA(long)处于下降趋势
						if ((preDelta < 0) && delta > 0) {
							IHOrder buyOrder = trans.buy(stockId)
									.shares(quantity).commit();
						}

					} else {
						informer.error("ERROR");
					}
				}
			});
	}
}
