package com.ricequant.strategy.support.mock;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ricequant.strategy.def.IHStrategy;

public class ReportBuffer {

	private IHStrategy strategy;

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
}
