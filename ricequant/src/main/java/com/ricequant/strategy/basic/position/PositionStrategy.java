package com.ricequant.strategy.basic.position;

import com.ricequant.strategy.def.IHInfoPacks;
import com.ricequant.strategy.def.IHPosition;
import com.ricequant.strategy.def.IHStatistics;
import com.ricequant.strategy.def.IHTransactionFactory;

public class PositionStrategy {

	private static final int PST_PERCENT = 1;

	private double highestUnclosedProfitHeld;

	private double currentUnclosedProfitHeld;

	private double unclosedPositionInitValue;

	private boolean allowShortSell;

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

	public void buy(IHTransactionFactory trans, IHInfoPacks info, String stockCode,
			int strategyType, double param) {
		switch (strategyType) {
		case PST_PERCENT:
			buyPercent(trans, info, stockCode, param);
			break;
		default:
			throw new RuntimeException("unsupported strategyType " + strategyType);
		}
	}

	public void sell(IHTransactionFactory trans, IHInfoPacks info, String stockCode,
			int strategyType, double param) {
		switch (strategyType) {
		case PST_PERCENT:
			sellPercent(trans, info, stockCode, param);
			break;
		default:
			throw new RuntimeException("unsupported strategyType " + strategyType);
		}
	}

	public void buyPercent(IHTransactionFactory trans, IHInfoPacks info, String stockCode,
			double percent) {
		trans.buy(stockCode).percent(percent).commit();
	}

	public void sellPercent(IHTransactionFactory trans, IHInfoPacks info, String stockCode,
			double percent) {
		IHPosition position = info.position(stockCode);
		if (allowShortSell || position.getNonClosedTradeQuantity() != 0) {
			trans.sell(stockCode).percent(percent).commit();
		}
	}
}
