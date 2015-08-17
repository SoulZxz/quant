package com.ricequant.strategy.support.mock;

import com.ricequant.strategy.def.IHPosition;

public class DummyPosition implements IHPosition {

	String stockCode;

	private double nonClosedTradeQuantity;

	@Override
	public double getBoughtQuantity() {
		throw new IllegalArgumentException("unimplemented");
	}

	@Override
	public double getSoldQuantity() {
		throw new IllegalArgumentException("unimplemented");
	}

	@Override
	public double getBoughtValue() {
		throw new IllegalArgumentException("unimplemented");
	}

	@Override
	public double getSoldValue() {
		throw new IllegalArgumentException("unimplemented");
	}

	@Override
	public double getNonClosedTradeQuantity() {
		return nonClosedTradeQuantity;
	}

	@Override
	public double getTotalOrders() {
		throw new IllegalArgumentException("unimplemented");
	}

	@Override
	public double getTotalTrades() {
		throw new IllegalArgumentException("unimplemented");
	}

	public void setNonClosedTradeQuantity(double nonClosedTradeQuantity) {
		this.nonClosedTradeQuantity = nonClosedTradeQuantity;
	}

	@Override
	public String toString() {
		return "DummyPosition [stockCode=" + stockCode + ", nonClosedTradeQuantity="
				+ nonClosedTradeQuantity + "]";
	}

}
