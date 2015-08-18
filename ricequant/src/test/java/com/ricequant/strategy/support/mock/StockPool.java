package com.ricequant.strategy.support.mock;

import java.util.ArrayList;
import java.util.List;

public class StockPool {

	private List<String> stockCodes = new ArrayList<String>();

	public void addStockCode(String stockCode) {
		stockCodes.add(stockCode);
	}

	public List<String> getStockCodes() {
		return stockCodes;
	}

}
