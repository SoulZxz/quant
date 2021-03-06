package com.ricequant.strategy.support.mock;

import com.ricequant.strategy.def.IHOrderQuantityPicker;
import com.ricequant.strategy.def.IHTransactionFactory;

public class DummyTransactionFactory implements IHTransactionFactory {

	private int day;

	private RunnerContext runnerContext;

	public DummyTransactionFactory(RunnerContext runnerContext) {
		super();
		this.runnerContext = runnerContext;
	}

	@Override
	public IHOrderQuantityPicker buy(String idOrSymbol) {
		return new DummyOrderQuantityPicker(runnerContext, day, idOrSymbol, 1);
	}

	@Override
	public IHOrderQuantityPicker sell(String idOrSymbol) {
		return new DummyOrderQuantityPicker(runnerContext, day, idOrSymbol, -1);
	}

	public void setDay(int day) {
		this.day = day;
	}

}
