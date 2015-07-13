package com.ricequant.strategy.basic.trend.cb;

public class ChannelItem {

	private double highest;

	private double lowest;

	public ChannelItem(double highest, double lowest) {
		super();
		this.highest = highest;
		this.lowest = lowest;
	}

	public double getHighest() {
		return highest;
	}

	public void setHighest(double highest) {
		this.highest = highest;
	}

	public double getLowest() {
		return lowest;
	}

	public void setLowest(double lowest) {
		this.lowest = lowest;
	}

}
