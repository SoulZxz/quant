package com.ricequant.strategy.support;

import java.util.Arrays;

import com.ricequant.strategy.def.IHStatisticsHistory;

public class DummyHistoryData implements IHStatisticsHistory {

	private double[] lastPrice;

	private double[] highPrice;

	private double[] lowPrice;

	private double[] openingPrice;

	private double[] closingPrice;

	public double[] getLastPrice() {
		return lastPrice;
	}

	public void setLastPrice(double[] lastPrice) {
		this.lastPrice = lastPrice;
	}

	public double[] getHighPrice() {
		return highPrice;
	}

	public void setHighPrice(double[] highPrice) {
		this.highPrice = highPrice;
	}

	public double[] getLowPrice() {
		return lowPrice;
	}

	public void setLowPrice(double[] lowPrice) {
		this.lowPrice = lowPrice;
	}

	public double[] getOpeningPrice() {
		return openingPrice;
	}

	public void setOpeningPrice(double[] openingPrice) {
		this.openingPrice = openingPrice;
	}

	public double[] getClosingPrice() {
		return closingPrice;
	}

	public void setClosingPrice(double[] closingPrice) {
		this.closingPrice = closingPrice;
	}

	@Override
	public String toString() {
		return "DummyHistoryData [highPrice=" + Arrays.toString(highPrice)
				+ ", lowPrice=" + Arrays.toString(lowPrice) + ", openingPrice="
				+ Arrays.toString(openingPrice) + ", closingPrice="
				+ Arrays.toString(closingPrice) + "]";
	}

}
