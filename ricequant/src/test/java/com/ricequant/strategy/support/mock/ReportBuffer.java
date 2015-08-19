package com.ricequant.strategy.support.mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.ricequant.strategy.def.IHStrategy;

public class ReportBuffer {

	private IHStrategy strategy;

	private int startDay;

	private int endDay;

	private Map<String, List<TransactionDetail>> strategyInstTxMap = new HashMap<String, List<TransactionDetail>>();

	public IHStrategy getStrategy() {
		return strategy;
	}

	public void setStrategy(IHStrategy strategy) {
		this.strategy = strategy;
	}

	public void addStrategyInstTx(String id, List<TransactionDetail> details) {
		strategyInstTxMap.put(id, details);
	}

	public Map<String, ReportFragment> createReportFragments() {
		Map<String, ReportFragment> frags = new HashMap<String, ReportFragment>();
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
			} else if (i == details.size() - 1) {
				portfolioEndValue = detail.getExitValue();
			}

			double profit = detail.getProfit();
			if (profit >= 0) {
				wins++;
				profitStats.addValue(profit);
			} else {
				loses++;
				lossStats.addValue(profit);
			}
			grossStats.addValue(profit);

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
