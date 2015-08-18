package com.ricequant.strategy.support.mock;

import com.ricequant.strategy.def.IHInfoPacks;
import com.ricequant.strategy.def.IHInstrument;
import com.ricequant.strategy.def.IHPortfolio;
import com.ricequant.strategy.def.IHPosition;
import com.ricequant.strategy.def.IHRuntime;

public class DummyInfoPacks implements IHInfoPacks {

	private int day;

	private RunnerContext runnerContext;

	public DummyInfoPacks(RunnerContext runnerContext) {
		super();
		this.runnerContext = runnerContext;
	}

	@Override
	public IHInstrument instrument(String idOrSymbol) {
		throw new IllegalArgumentException("unimplemented");
	}

	@Override
	public IHPortfolio portfolio() {
		double profitAndLoss = runnerContext.getPortfolioHolder().profitAndLoss(day);
		return new DummyPortfolio(profitAndLoss);
	}

	@Override
	public IHPosition position(IHInstrument instrument) {
		throw new IllegalArgumentException("unimplemented");
	}

	@Override
	public IHPosition position(String idOrSymbol) {
		return runnerContext.getPortfolioHolder().position(idOrSymbol);
	}

	@Override
	public IHRuntime runtime() {
		throw new IllegalArgumentException("unimplemented");
	}

	public void setDay(int day) {
		this.day = day;
	}

}
