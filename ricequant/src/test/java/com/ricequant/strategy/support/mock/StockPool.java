package com.ricequant.strategy.support.mock;

import java.util.ArrayList;
import java.util.List;

public class StockPool {

	private static List<String> stockCodes = new ArrayList<String>();

	public static void addStockCode(String stockCode) {
		stockCodes.add(stockCode);
	}

	public static List<String> getStockCodes() {
		return stockCodes;
	}

}
