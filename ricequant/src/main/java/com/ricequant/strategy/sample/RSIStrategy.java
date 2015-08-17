package com.ricequant.strategy.sample;

import com.ricequant.strategy.def.HPeriod;
import com.ricequant.strategy.def.IHInfoPacks;
import com.ricequant.strategy.def.IHInformer;
import com.ricequant.strategy.def.IHInitializers;
import com.ricequant.strategy.def.IHStatistics;
import com.ricequant.strategy.def.IHStrategy;
import com.ricequant.strategy.def.IHTransactionFactory;
import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;

public class RSIStrategy implements IHStrategy {

	private IHInformer theInformer;

	private Core core = new Core();

	/** rsi **/
	private double lowRSI = 30;

	private double highRSI = 70;

	// 建议250
	private int dataSetSize = 40;

	// 建议14
	private int rsiPeriod = 14;

	/** exit **/
	private double highestUnclosedProfitHeld;

	private double currentUnclosedProfitHeld;

	private double unclosedPositionInitValue;

	/** other **/
	private String stockCode = "000528.XSHE";

	private boolean allowShortSell = false;

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
				RSIStrategy.this.eaWork(stats.get(stockCode), info, trans);
			});
		});
	}

	public void eaWork(IHStatistics stat, IHInfoPacks info, IHTransactionFactory trans) {
		double rsiSignal = generateRSISignal(stat);
		if (rsiSignal != 0) {
			int direction = rsiSignal > 0 ? 1 : -1;
			theInformer.info("rsi trade " + direction);
			this.entry(trans, info, stat, stockCode, direction);
		}

		theInformer.plot("CLOSING", stat.getClosingPrice());
	}

	public double generateRSISignal(IHStatistics stat) {
		double[] close = stat.history(dataSetSize, HPeriod.Day).getClosingPrice();

		double[] result = computeRSI(close, rsiPeriod);

		double currentRSI = result[result.length - 1];

		if (currentRSI > highRSI) {
			return -1;
		} else if (currentRSI < lowRSI) {
			return 1;
		} else {
			return 0;
		}
	}

	public double[] computeRSI(double[] input, int period) {
		MInteger begin = new MInteger();
		MInteger length = new MInteger();
		double[] out = new double[input.length - period];

		core.rsi(0, input.length - 1, input, period, begin, length, out);
		return out;
	}

	public void entry(IHTransactionFactory trans, IHInfoPacks info, IHStatistics stat,
			String stockCode, int direction) {
		int nonClosed = (int) info.position(stockCode).getNonClosedTradeQuantity();
		int directionalNonClosed = direction * nonClosed;

		// 如果有反方向的头寸, 先平掉
		if (directionalNonClosed < 0) {
			if (nonClosed < 0) {
				// 平掉空头寸
				trans.buy(stockCode).shares(-nonClosed).commit();
				// 开多头寸
				trans.buy(stockCode).percent(100).commit();
				unclosedPositionInitValue = info.position(stockCode).getNonClosedTradeQuantity()
						* stat.getLastPrice();
				currentUnclosedProfitHeld = 0;
				highestUnclosedProfitHeld = 0;
			} else {
				// 平掉多头寸
				trans.sell(stockCode).shares(nonClosed).commit();
				// 开空头寸
				if (allowShortSell) {
					trans.sell(stockCode).percent(100).commit();
					unclosedPositionInitValue = -info.position(stockCode)
							.getNonClosedTradeQuantity() * stat.getLastPrice();
					currentUnclosedProfitHeld = 0;
					highestUnclosedProfitHeld = 0;
				}
			}
		}
		// 如果没持有头寸, 开
		else if (directionalNonClosed == 0) {
			if (direction > 0) {
				// 开多头寸
				trans.buy(stockCode).percent(100).commit();
				unclosedPositionInitValue = info.position(stockCode).getNonClosedTradeQuantity()
						* stat.getLastPrice();
				currentUnclosedProfitHeld = 0;
				highestUnclosedProfitHeld = 0;
			} else if (direction < 0) {
				// 开空头寸
				if (allowShortSell) {
					trans.sell(stockCode).percent(100).commit();
					unclosedPositionInitValue = -info.position(stockCode)
							.getNonClosedTradeQuantity() * stat.getLastPrice();
					currentUnclosedProfitHeld = 0;
					highestUnclosedProfitHeld = 0;
				}
			}
		}
	}

	public void exit(IHTransactionFactory trans, IHInfoPacks info, String stockCode) {
		int nonClosed = (int) info.position(stockCode).getNonClosedTradeQuantity();
		if (nonClosed < 0) {
			// 平掉空头寸
			trans.buy(stockCode).shares(-nonClosed).commit();
			unclosedPositionInitValue = 0;
			currentUnclosedProfitHeld = 0;
			highestUnclosedProfitHeld = 0;
		} else if (nonClosed > 0) {
			// 平掉多头寸
			trans.sell(stockCode).shares(nonClosed).commit();
			unclosedPositionInitValue = 0;
			currentUnclosedProfitHeld = 0;
			highestUnclosedProfitHeld = 0;
		}
	}

}
