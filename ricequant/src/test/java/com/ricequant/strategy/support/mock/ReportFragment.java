package com.ricequant.strategy.support.mock;

import java.util.List;

public class ReportFragment {

	private List<TransactionDetail> details;

	private int startDay;

	private int endDay;

	private int days;

	private int trades;

	private int wins;

	private int loses;

	private double winningRate;

	private double losingRate;

	private double profitMean;

	private double profitStd;

	private double profitMedian;

	private double lossMean;

	private double lossStd;

	private double lossMedian;

	private double grossMean;

	private double grossStd;

	private double grossMedian;

	private double totalProfit;

	public List<TransactionDetail> getDetails() {
		return details;
	}

	public void setDetails(List<TransactionDetail> details) {
		this.details = details;
	}

	public int getStartDay() {
		return startDay;
	}

	public void setStartDay(int startDay) {
		this.startDay = startDay;
	}

	public int getEndDay() {
		return endDay;
	}

	public void setEndDay(int endDay) {
		this.endDay = endDay;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public int getTrades() {
		return trades;
	}

	public void setTrades(int trades) {
		this.trades = trades;
	}

	public int getWins() {
		return wins;
	}

	public void setWins(int wins) {
		this.wins = wins;
	}

	public int getLoses() {
		return loses;
	}

	public void setLoses(int loses) {
		this.loses = loses;
	}

	public double getWinningRate() {
		return winningRate;
	}

	public void setWinningRate(double winningRate) {
		this.winningRate = winningRate;
	}

	public double getLosingRate() {
		return losingRate;
	}

	public void setLosingRate(double losingRate) {
		this.losingRate = losingRate;
	}

	public double getProfitMean() {
		return profitMean;
	}

	public void setProfitMean(double profitMean) {
		this.profitMean = profitMean;
	}

	public double getProfitStd() {
		return profitStd;
	}

	public void setProfitStd(double profitStd) {
		this.profitStd = profitStd;
	}

	public double getProfitMedian() {
		return profitMedian;
	}

	public void setProfitMedian(double profitMedian) {
		this.profitMedian = profitMedian;
	}

	public double getLossMean() {
		return lossMean;
	}

	public void setLossMean(double lossMean) {
		this.lossMean = lossMean;
	}

	public double getLossStd() {
		return lossStd;
	}

	public void setLossStd(double lossStd) {
		this.lossStd = lossStd;
	}

	public double getLossMedian() {
		return lossMedian;
	}

	public void setLossMedian(double lossMedian) {
		this.lossMedian = lossMedian;
	}

	public double getGrossMean() {
		return grossMean;
	}

	public void setGrossMean(double grossMean) {
		this.grossMean = grossMean;
	}

	public double getGrossStd() {
		return grossStd;
	}

	public void setGrossStd(double grossStd) {
		this.grossStd = grossStd;
	}

	public double getGrossMedian() {
		return grossMedian;
	}

	public void setGrossMedian(double grossMedian) {
		this.grossMedian = grossMedian;
	}

	public double getTotalProfit() {
		return totalProfit;
	}

	public void setTotalProfit(double totalProfit) {
		this.totalProfit = totalProfit;
	}

	@Override
	public String toString() {
		return "ReportFragment [details=" + details + ", startDay=" + startDay + ", endDay="
				+ endDay + ", days=" + days + ", trades=" + trades + ", wins=" + wins + ", loses="
				+ loses + ", winningRate=" + winningRate + ", losingRate=" + losingRate
				+ ", profitMean=" + profitMean + ", profitStd=" + profitStd + ", profitMedian="
				+ profitMedian + ", lossMean=" + lossMean + ", lossStd=" + lossStd
				+ ", lossMedian=" + lossMedian + ", grossMean=" + grossMean + ", grossStd="
				+ grossStd + ", grossMedian=" + grossMedian + ", totalProfit=" + totalProfit + "]";
	}

}
