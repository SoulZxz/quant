package com.ricequant.strategy.basic.oscillator.stochastics;

public class SlowStochastic {

	private double k;

	private double d;

	public SlowStochastic(double k, double d) {
		super();
		this.k = k;
		this.d = d;
	}

	public double getK() {
		return k;
	}

	public void setK(double k) {
		this.k = k;
	}

	public double getD() {
		return d;
	}

	public void setD(double d) {
		this.d = d;
	}

}
