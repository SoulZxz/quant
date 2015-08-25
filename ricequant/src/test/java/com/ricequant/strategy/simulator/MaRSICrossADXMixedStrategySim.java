package com.ricequant.strategy.simulator;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import com.ricequant.strategy.def.IHStrategy;
import com.ricequant.strategy.my.MaRSICrossADXMixedStrategy;

public class MaRSICrossADXMixedStrategySim extends BaseStrategySim {

	public MaRSICrossADXMixedStrategySim() {
		startDay = 150;
		excludes = new String[] { "core", "theInformer", "stockCode", "currentUnclosedProfitHeld",
				"highestUnclosedProfitHeld", "unclosedPositionInitValue", "MA_TYPE_EMA",
				"MA_TYPE_SMA", "MA_TYPE_WMA" };
		includes = new String[] { "stockCode" };
	}

	protected IHStrategy createStrategy(String stockCode) {
		MaRSICrossADXMixedStrategy strategy = new MaRSICrossADXMixedStrategy();
		List<String> stockCodeList = new ArrayList<String>();
		stockCodeList.add(stockCode);
		strategy.setStockCode(stockCodeList);
		return strategy;
	}

	@Test
	public void testRun() {
		runAndReport();
	}

}
