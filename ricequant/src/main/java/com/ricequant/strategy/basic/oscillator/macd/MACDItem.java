package com.ricequant.strategy.basic.oscillator.macd;

public class MACDItem {

	private double value;

	private double signal;

	private double histogram;

	public MACDItem(double value, double signal, double histogram) {
		super();
		this.value = value;
		this.signal = signal;
		this.histogram = histogram;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public double getSignal() {
		return signal;
	}

	public void setSignal(double signal) {
		this.signal = signal;
	}

	public double getHistogram() {
		return histogram;
	}

	public void setHistogram(double histogram) {
		this.histogram = histogram;
	}

}
