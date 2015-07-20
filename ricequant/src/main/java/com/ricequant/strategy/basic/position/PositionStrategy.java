package com.ricequant.strategy.basic.position;

import com.ricequant.strategy.def.IHInfoPacks;
import com.ricequant.strategy.def.IHPosition;
import com.ricequant.strategy.def.IHTransactionFactory;

public class PositionStrategy {

	private static final int PST_PERCENT = 1;

	private boolean allowShortSell;

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
