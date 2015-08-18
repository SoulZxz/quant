package com.ricequant.strategy.support.mock;

import com.ricequant.strategy.def.IHOrderBuilderBase;
import com.ricequant.strategy.def.IHOrderQuantityPicker;

public class DummyOrderQuantityPicker implements IHOrderQuantityPicker {

	private RunnerContext runnerContext;

	private int day;

	private String stockCode;

	// buy 1; sell -1
	private int tradeDirection;

	public DummyOrderQuantityPicker(RunnerContext runnerContext, int day, String stockCode,
			int tradeDirection) {
		super();
		this.runnerContext = runnerContext;
		this.day = day;
		this.stockCode = stockCode;
		this.tradeDirection = tradeDirection;
	}

	@Override
	public IHOrderBuilderBase shares(double numShares) {
		return new DummyOrderBuilderBase(runnerContext, day, stockCode, tradeDirection, 0,
				numShares);
	}

	@Override
	public IHOrderBuilderBase lots(double numLots) {
		throw new IllegalArgumentException("unimplemented");
	}

	@Override
	public IHOrderBuilderBase percent(double percent) {
		return new DummyOrderBuilderBase(runnerContext, day, stockCode, tradeDirection, percent, 0);
	}

}
