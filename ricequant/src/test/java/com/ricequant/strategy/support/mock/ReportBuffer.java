package com.ricequant.strategy.support.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class ReportBuffer {

	private String strategyName;

	private Map<String, Object> strategyParams;

	private int startDay;

	private int endDay;

	private Map<String, List<TransactionDetail>> strategyInstTxMap = new TreeMap<String, List<TransactionDetail>>();

	private Map<String, ReportFragment> frags;

	public String getStrategyName() {
		return strategyName;
	}

	public void setStrategyName(String strategyName) {
		this.strategyName = strategyName;
	}

	public Map<String, Object> getStrategyParams() {
		return strategyParams;
	}

	public void setStrategyParams(Map<String, Object> strategyParams) {
		this.strategyParams = strategyParams;
	}

	public void addStrategyInstTx(String id, List<TransactionDetail> details) {
		strategyInstTxMap.put(id, details);
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

	public Map<String, List<TransactionDetail>> getStrategyInstTxMap() {
		return strategyInstTxMap;
	}

	public Map<String, ReportFragment> createReportFragments() {
		frags = new TreeMap<String, ReportFragment>();
		List<TransactionDetail> total = new ArrayList<TransactionDetail>();

		for (Entry<String, List<TransactionDetail>> strategyInstTx : strategyInstTxMap.entrySet()) {
			total.addAll(strategyInstTx.getValue());
			ReportFragment frag = this.createReportFragment(strategyInstTx.getValue());

			frags.put(strategyInstTx.getKey(), frag);
		}

		ReportFragment totalFrag = this.createReportFragment(total);
		frags.put("total", totalFrag);

		return frags;
	}

	public Map<String, ReportFragment> getFrags() {
		return frags;
	}

	private ReportFragment createReportFragment(List<TransactionDetail> details) {
		int wins = 0;
		int loses = 0;

		DescriptiveStatistics profitStats = new DescriptiveStatistics();
		DescriptiveStatistics lossStats = new DescriptiveStatistics();
		DescriptiveStatistics grossStats = new DescriptiveStatistics();

		int i = 0;
		double portfolioStartValue = 0;
		double portfolioEndValue = 0;

		for (TransactionDetail detail : details) {
			if (i == 0) {
				portfolioStartValue = detail.getEntryValue();
			}
			if (i == details.size() - 1) {
				portfolioEndValue = detail.getExitValue();
			}

			double profit = detail.getProfit();
			double entryValue = detail.getEntryValue();
			if (profit >= 0) {
				wins++;
				profitStats.addValue(profit / entryValue);
			} else {
				loses++;
				lossStats.addValue(profit / entryValue);
			}
			grossStats.addValue(profit / entryValue);

			i++;
		}

		ReportFragment frag = new ReportFragment();
		frag.setDetails(details);
		frag.setStartDay(startDay);
		frag.setEndDay(endDay);
		frag.setDays(endDay - startDay);
		frag.setTrades(details.size());
		frag.setWins(wins);
		frag.setLoses(loses);
		frag.setWinningRate(1.0 * wins / frag.getTrades());
		frag.setLosingRate(1.0 * loses / frag.getTrades());
		frag.setProfitMean(profitStats.getMean());
		frag.setProfitStd(profitStats.getStandardDeviation());
		frag.setProfitMedian(profitStats.getPercentile(50));
		frag.setLossMean(lossStats.getMean());
		frag.setLossStd(lossStats.getStandardDeviation());
		frag.setLossMedian(lossStats.getPercentile(50));
		frag.setGrossMean(grossStats.getMean());
		frag.setGrossStd(grossStats.getStandardDeviation());
		frag.setGrossMedian(grossStats.getPercentile(50));
		frag.setTotalProfit((portfolioEndValue - portfolioStartValue) / portfolioStartValue);

		return frag;
	}
}
