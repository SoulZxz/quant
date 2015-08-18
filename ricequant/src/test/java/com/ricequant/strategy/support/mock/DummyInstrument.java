package com.ricequant.strategy.support.mock;

import com.ricequant.strategy.def.IHInstrument;

public class DummyInstrument implements IHInstrument {

	private String stockCode;

	public DummyInstrument(String stockCode) {
		super();
		this.stockCode = stockCode;
	}

	@Override
	public String getOrderBookID() {
		throw new IllegalArgumentException("unimplemented");
	}

	@Override
	public String getSymbol() {
		return stockCode;
	}

	@Override
	public String getAbbrevSymbol() {
		throw new IllegalArgumentException("unimplemented");
	}

	@Override
	public double getRoundLot() {
		throw new IllegalArgumentException("unimplemented");
	}

}
