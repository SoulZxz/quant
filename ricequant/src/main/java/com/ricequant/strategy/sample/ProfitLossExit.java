package com.ricequant.strategy.sample;

import com.ricequant.strategy.def.IHInfoPacks;
import com.ricequant.strategy.def.IHInformer;
import com.ricequant.strategy.def.IHInitializers;
import com.ricequant.strategy.def.IHPortfolio;
import com.ricequant.strategy.def.IHStatistics;
import com.ricequant.strategy.def.IHStrategy;
import com.ricequant.strategy.def.IHTransactionFactory;

public class ProfitLossExit implements IHStrategy {

	private double profitTarget = 0.05;

	private double lossTrigger = -0.05;

	private double highestUnclosedProfitHeld;

	private double currentUnclosedProfitHeld;

	private double unclosedPositionInitValue;

	private String stockCode = "000528.XSHE";

	private IHInformer theInformer;

	private boolean hasPosition = false;

	private boolean allowShortSell = false;

	@Override
	public void init(IHInformer informer, IHInitializers initializers) {
		theInformer = informer;

		String stockCode = "000528.XSHE";

		initializers.instruments((universe) -> {
			return universe.add(stockCode);
		});

		// 允许卖空
		initializers.shortsell().allow();

		initializers.events().statistics((stats, info, trans) -> {
			stats.each((stat) -> {
				ProfitLossExit.this.eaWork(stats.get(stockCode), info, trans);
			});
		});
	}

	public void eaWork(IHStatistics stat, IHInfoPacks info, IHTransactionFactory trans) {
		double signal = this.generateSignal(stat, info.portfolio());
		if (!hasPosition) {
			entry(trans, info, stat, stockCode, 1);
			hasPosition = true;
			theInformer.info("buy");
		} else {
			if (signal != 0) {
				exit(trans, info, stockCode);
				hasPosition = false;
				theInformer.info("sell");
			}
		}
	}

	public double generateSignal(IHStatistics stat, IHPortfolio portfolio) {
		currentUnclosedProfitHeld = currentUnclosedProfitHeld + portfolio.getProfitAndLoss();
		highestUnclosedProfitHeld = Math.max(highestUnclosedProfitHeld, currentUnclosedProfitHeld);

		double profitAndLossRate = currentUnclosedProfitHeld / unclosedPositionInitValue;
		double highestProfitRate = highestUnclosedProfitHeld / unclosedPositionInitValue;

		theInformer.info("currentUnclosedProfitHeld " + currentUnclosedProfitHeld
				+ " unclosedPositionInitValue " + unclosedPositionInitValue
				+ " highestUnclosedProfitHeld " + highestUnclosedProfitHeld);

		// 达到盈利点, 发止盈信号; 从最高盈利点发生drawdown达到lossTrigger, 止损或止浮盈
		if (profitAndLossRate <= profitTarget
				&& profitAndLossRate - highestProfitRate >= lossTrigger) {
			return 0;
		} else {
			if (profitAndLossRate > profitTarget) {
				return 1;
			} else {
				return -1;
			}
		}
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
		} else {
			// 平掉多头寸
			trans.sell(stockCode).shares(nonClosed).commit();
			unclosedPositionInitValue = 0;
			currentUnclosedProfitHeld = 0;
			highestUnclosedProfitHeld = 0;
		}
	}
}
