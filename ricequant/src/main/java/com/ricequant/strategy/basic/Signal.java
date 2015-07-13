package com.ricequant.strategy.basic;

public class Signal {

	/**
	 * 信号强度 大于0 买入, 小于0卖出, 等于0什么也不做
	 */
	private double strength;

	public Signal(double strength) {
		super();
		this.strength = strength;
	}

	public double getStrength() {
		return strength;
	}

	public void setStrength(double strength) {
		this.strength = strength;
	}

}
