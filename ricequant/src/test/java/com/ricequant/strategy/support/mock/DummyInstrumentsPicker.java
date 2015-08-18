package com.ricequant.strategy.support.mock;

import java.util.function.Predicate;

import com.ricequant.strategy.def.IHInstrument;
import com.ricequant.strategy.def.IHInstrumentsPicker;

public class DummyInstrumentsPicker implements IHInstrumentsPicker {

	private RunnerContext runnerContext;

	public DummyInstrumentsPicker(RunnerContext runnerContext) {
		super();
		this.runnerContext = runnerContext;
	}

	@Override
	public IHInstrumentsPicker add(String... idOrSymbol) {
		for (String id : idOrSymbol) {
			runnerContext.getStockPool().addStockCode(id);
		}
		return this;
	}

	@Override
	public IHInstrumentsPicker all() {
		throw new IllegalArgumentException("unimplemented");
	}

	@Override
	public IHInstrumentsPicker remove(String... idOrSymbol) {
		throw new IllegalArgumentException("unimplemented");
	}

	@Override
	public IHInstrumentsPicker filter(Predicate<IHInstrument> filter) {
		throw new IllegalArgumentException("unimplemented");
	}

}
