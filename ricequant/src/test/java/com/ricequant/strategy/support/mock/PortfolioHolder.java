package com.ricequant.strategy.support.mock;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.ricequant.strategy.def.IHStatisticsHistory;
import com.ricequant.strategy.support.HistoryDataProvider;

public class PortfolioHolder {

	private static Map<String, DummyPosition> positions = new HashMap<String, DummyPosition>();

	private static double currentCash = 100000;

	public static DummyPosition position(String stockCode) {
		DummyPosition currentPosition = positions.get(stockCode);
		if (currentPosition == null) {
			currentPosition = new DummyPosition();
			currentPosition.stockCode = stockCode;
			positions.put(stockCode, currentPosition);
		}
		return currentPosition;
	}

	public static double profitAndLoss(int day) {
		double profitAndLoss = 0;
		for (Entry<String, DummyPosition> position : positions.entrySet()) {
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
		System.out.println("tradePercent day " + day + " stockCode " + stockCode
				+ " tradeDirection " + tradeDirection + " percent " + percent);
		IHStatisticsHistory history = HistoryDataProvider.getData("data/pool/" + stockCode,
				day + 1, day + 2);

		DummyPosition currentPosition = positions.get(stockCode);
		if (currentPosition == null) {
			currentPosition = new DummyPosition();
			currentPosition.stockCode = stockCode;
			positions.put(stockCode, currentPosition);
		}

		System.out.println("tradePercent currentPosition " + currentPosition + " currentCash "
				+ currentCash);

		// 第二天open
		double tradePrice = history.getOpeningPrice()[0];
		double currentPositionValue = Math.abs(currentPosition.getNonClosedTradeQuantity())
				* tradePrice;
		double currentPortfolioValue = currentPositionValue + currentCash;
		double targetPositionValue = currentPortfolioValue * percent / 100;

		double positionValueGap = targetPositionValue - currentPositionValue;
		double positionChange = ((int) (positionValueGap / tradePrice)) / 100 * 100;

		System.out.println("tradePercent positionValueGap " + positionValueGap + " positionChange "
				+ positionChange);

		currentCash = currentCash - tradePrice * positionChange;
		double newPosition = currentPosition.getNonClosedTradeQuantity() + tradeDirection
				* positionChange;
		currentPosition.setNonClosedTradeQuantity(newPosition);
		System.out.println("tradePercent after trade currentPosition " + currentPosition
				+ " currentCash " + currentCash);
	}

	public static void tradeShares(int day, String stockCode, int tradeDirection, double shares) {
		System.out.println("tradeShares day " + day + " stockCode " + stockCode
				+ " tradeDirection " + tradeDirection + " shares " + shares);
		if (shares < 0) {
			throw new IllegalArgumentException("trade shares less than 0");
		}

		IHStatisticsHistory history = HistoryDataProvider.getData("data/pool/" + stockCode,
				day + 1, day + 2);

		DummyPosition currentPosition = positions.get(stockCode);
		if (currentPosition == null) {
			currentPosition = new DummyPosition();
			currentPosition.stockCode = stockCode;
			positions.put(stockCode, currentPosition);
		}

		System.out.println("tradeShares currentPosition " + currentPosition + " currentCash "
				+ currentCash);

		// 第二天open
		double tradePrice = history.getOpeningPrice()[0];
		double positionChange = 0;

		if (tradeDirection > 0) {
			double maxPositionChange = ((int) (currentCash / tradePrice)) / 100 * 100;
			positionChange = Math.min(shares, maxPositionChange);
		} else {
			positionChange = Math.min(shares, currentPosition.getNonClosedTradeQuantity());
		}

		currentCash = currentCash - tradeDirection * tradePrice * positionChange;
		double newPosition = currentPosition.getNonClosedTradeQuantity() + tradeDirection
				* positionChange;
		currentPosition.setNonClosedTradeQuantity(newPosition);
		System.out.println("tradeShares after trade currentPosition " + currentPosition
				+ " currentCash " + currentCash);
	}

	public static void status() {
		System.out.println("current cash " + currentCash);
		System.out.println("current positions " + positions);
	}
}
