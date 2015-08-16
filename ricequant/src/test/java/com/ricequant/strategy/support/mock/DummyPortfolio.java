package com.ricequant.strategy.support.mock;

import com.ricequant.strategy.def.IHPortfolio;

public class DummyPortfolio implements IHPortfolio {

	private double profitAndLoss;

	public DummyPortfolio(double profitAndLoss) {
		super();
		this.profitAndLoss = profitAndLoss;
	}

	@Override
	public double getInitialCash() {
		throw new IllegalArgumentException("unimplemented");
	}

	@Override
	public double getAvailableCash() {
		throw new IllegalArgumentException("unimplemented");
	}

	@Override
	public double getTotalReturn() {
		throw new IllegalArgumentException("unimplemented");
	}

	@Override
	public double getDailyReturn() {
		throw new IllegalArgumentException("unimplemented");
	}

	@Override
	public double getMarketValue() {
		throw new IllegalArgumentException("unimplemented");
	}

	@Override
	public double getPortfolioValue() {
		throw new IllegalArgumentException("unimplemented");
	}

	@Override
	public double getProfitAndLoss() {
		return profitAndLoss;
	}

	@Override
	public double getAnnualizedAvgReturns() {
		throw new IllegalArgumentException("unimplemented");
	}

	@Override
	public double getDividendReceivable() {
		throw new IllegalArgumentException("unimplemented");
	}

}
