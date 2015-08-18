package com.ricequant.strategy.support.mock;

public class TransactionDetail {

	// Long or Short
	private String tradeType;

	private double entryValue;

	private Double exitValue;

	private Double profit;

	private int entryDay;

	private Integer exitDay;

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public double getEntryValue() {
		return entryValue;
	}

	public void setEntryValue(double entryValue) {
		this.entryValue = entryValue;
	}

	public Double getExitValue() {
		return exitValue;
	}

	public void setExitValue(Double exitValue) {
		this.exitValue = exitValue;
	}

	public Double getProfit() {
		return profit;
	}

	public void setProfit(Double profit) {
		this.profit = profit;
	}

	public int getEntryDay() {
		return entryDay;
	}

	public void setEntryDay(int entryDay) {
		this.entryDay = entryDay;
	}

	public Integer getExitDay() {
		return exitDay;
	}

	public void setExitDay(Integer exitDay) {
		this.exitDay = exitDay;
	}

	@Override
	public String toString() {
		return "TransactionDetail [tradeType=" + tradeType + ", entryValue=" + entryValue
				+ ", exitValue=" + exitValue + ", profit=" + profit + ", entryDay=" + entryDay
				+ ", exitDay=" + exitDay + "]";
	}

}
