package com.ricequant.strategy.support.mock;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.ricequant.strategy.def.IHStatisticsHistory;
import com.ricequant.strategy.support.HistoryDataProvider;

public class PortfolioHolder {

	private static Map<String, DummyPosition> positions = new HashMap<String, DummyPosition>();

	private static double currentCash = 100000;

	public static Map<String, DummyPosition> getPositions() {
		return positions;
	}

	public static double profitAndLoss(int day) {
		double profitAndLoss = 0;
		for (Entry<String, DummyPosition> position : PortfolioHolder.getPositions().entrySet()) {
			IHStatisticsHistory history = HistoryDataProvider.getData(position.getKey(), day - 1,
					day + 1);
			double[] closings = history.getClosingPrice();
			double deltaValue = (closings[1] - closings[0])
					* position.getValue().getNonClosedTradeQuantity();
			profitAndLoss += deltaValue;
		}
		return profitAndLoss;
	}

	public static void tradePercent(int day, String stockCode, int tradeDirection, double percent) {
		IHStatisticsHistory history = HistoryDataProvider.getData(stockCode, day + 1, day + 2);

		DummyPosition currentPosition = positions.get(stockCode);
		if (currentPosition == null) {
			currentPosition = new DummyPosition();
			positions.put(stockCode, currentPosition);
		}

		// 第二天open
		double tradePrice = history.getOpeningPrice()[0];
		double currentPositionValue = Math.abs(currentPosition.getNonClosedTradeQuantity())
				* tradePrice;
		double currentPortfolioValue = currentPositionValue + currentCash;
		double targetPositionValue = currentPortfolioValue * percent;

		double positionValueGap = targetPositionValue - currentPositionValue;
		double positionChange = ((int) (positionValueGap / tradePrice)) / 100 * 100;

		currentCash = currentCash - tradePrice * positionChange;
		double newPosition = currentPosition.getNonClosedTradeQuantity() + tradeDirection
				* positionChange;
		currentPosition.setNonClosedTradeQuantity(newPosition);
	}

	public static void tradeShares(int day, String stockCode, int tradeDirection, double shares) {
		if (shares < 0) {
			throw new IllegalArgumentException("trade shares less than 0");
		}

		IHStatisticsHistory history = HistoryDataProvider.getData(stockCode, day + 1, day + 2);

		DummyPosition currentPosition = positions.get(stockCode);
		if (currentPosition == null) {
			currentPosition = new DummyPosition();
			positions.put(stockCode, currentPosition);
		}

		// 第二天open
		double tradePrice = history.getOpeningPrice()[0];
		double maxPositionChange = ((int) (currentCash / tradePrice)) / 100 * 100;
		double positionChange = Math.min(shares, maxPositionChange);

		currentCash = currentCash - tradePrice * positionChange;
		double newPosition = currentPosition.getNonClosedTradeQuantity() + tradeDirection
				* positionChange;
		currentPosition.setNonClosedTradeQuantity(newPosition);
	}

	public static void status() {
		System.out.println("current cash " + currentCash);
	}
}
