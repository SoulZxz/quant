package com.ricequant.strategy.support.mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ricequant.strategy.def.IHStatisticsHistory;
import com.ricequant.strategy.support.HistoryDataProvider;

public class PortfolioHolder {

	private Logger log = LogManager.getLogger(PortfolioHolder.class);

	private List<TransactionDetail> transactionDetails = new ArrayList<TransactionDetail>();

	private Map<String, DummyPosition> positions = new HashMap<String, DummyPosition>();

	private double currentCash = 100000;

	private double shortSellAvailableQuota = currentCash;

	public DummyPosition position(String stockCode) {
		DummyPosition currentPosition = positions.get(stockCode);
		if (currentPosition == null) {
			currentPosition = new DummyPosition();
			currentPosition.stockCode = stockCode;
			positions.put(stockCode, currentPosition);
		}
		return currentPosition;
	}

	public double profitAndLoss(int day) {
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

	public void tradePercent(int day, String stockCode, int tradeDirection, double percent) {
		log.debug("tradePercent day " + day + " stockCode " + stockCode + " tradeDirection "
				+ tradeDirection + " percent " + percent);

		DummyPosition currentPosition = this.position(stockCode);
		double nonClosed = currentPosition.getNonClosedTradeQuantity();

		log.debug("tradePercent currentPosition " + currentPosition + " currentCash " + currentCash
				+ " shortSellAvailableQuota " + shortSellAvailableQuota);

		// 第二天open
		double tradePrice = this.getTradePrice(tradeDirection, day, stockCode);
		double currentPositionValue = nonClosed * tradePrice;
		double currentPortfolioValue = currentPositionValue + currentCash;
		double targetPositionValue = currentPortfolioValue * percent / 100;

		double positionValueGap = targetPositionValue - Math.abs(currentPositionValue);
		double positionChange = ((int) (positionValueGap / tradePrice)) / 100 * 100;

		log.debug("tradeDirection " + tradeDirection + " at " + tradePrice);
		log.debug("tradePercent positionValueGap " + positionValueGap + " positionChange "
				+ positionChange);

		currentCash = currentCash - tradeDirection * tradePrice * positionChange;
		if ((nonClosed == 0 && tradeDirection < 0) || (nonClosed < 0 && tradeDirection > 0)) {
			shortSellAvailableQuota = shortSellAvailableQuota + tradeDirection * tradePrice
					* positionChange;
		}
		double newPosition = nonClosed + tradeDirection * positionChange;
		currentPosition.setNonClosedTradeQuantity(newPosition);
		log.debug("tradePercent after trade currentPosition " + currentPosition + " currentCash "
				+ currentCash + " shortSellAvailableQuota " + shortSellAvailableQuota);

		logTransactionDetail(day, tradeDirection);
	}

	public void tradeShares(int day, String stockCode, int tradeDirection, double shares) {
		log.debug("tradeShares day " + day + " stockCode " + stockCode + " tradeDirection "
				+ tradeDirection + " shares " + shares);
		if (shares < 0) {
			throw new IllegalArgumentException("trade shares less than 0");
		}

		DummyPosition currentPosition = this.position(stockCode);
		double nonClosed = currentPosition.getNonClosedTradeQuantity();

		log.debug("tradeShares currentPosition " + currentPosition + " currentCash " + currentCash
				+ " shortSellAvailableQuota " + shortSellAvailableQuota);

		// 第二天open
		double tradePrice = this.getTradePrice(tradeDirection, day, stockCode);
		double positionChange = 0;

		log.debug("tradeDirection " + tradeDirection + " at " + tradePrice);

		if (tradeDirection > 0) {
			double maxPositionChange = ((int) (currentCash / tradePrice)) / 100 * 100;
			positionChange = Math.min(shares, maxPositionChange);
		} else {
			positionChange = Math.min(shares, nonClosed);
		}

		currentCash = currentCash - tradeDirection * tradePrice * positionChange;
		if ((nonClosed == 0 && tradeDirection < 0) || (nonClosed < 0 && tradeDirection > 0)) {
			shortSellAvailableQuota = shortSellAvailableQuota + tradeDirection * tradePrice
					* positionChange;
		}
		double newPosition = nonClosed + tradeDirection * positionChange;
		currentPosition.setNonClosedTradeQuantity(newPosition);
		log.debug("tradeShares after trade currentPosition " + currentPosition + " currentCash "
				+ currentCash + " shortSellAvailableQuota " + shortSellAvailableQuota);

		logTransactionDetail(day, tradeDirection);
	}

	public void status(int day) {
		log.info("current cash " + currentCash + "current positions " + positions
				+ " currentPortfolioValue " + this.currentPortfolioValue(day));
		log.info("transactionDetails " + transactionDetails);
	}

	public double currentPortfolioValue(int day) {
		double positionValue = 0;

		for (DummyPosition position : positions.values()) {
			double tradePrice = this.getTradePrice(1, day, position.stockCode);
			positionValue += tradePrice * position.getNonClosedTradeQuantity();
		}
		return positionValue + currentCash;
	}

	private double getTradePrice(int tradeDirection, int day, String stockCode) {
		int maxDay = HistoryDataProvider.getData("data/pool/" + stockCode).getClosingPrice().length;
		if (day >= maxDay) {
			IHStatisticsHistory history = HistoryDataProvider.getData("data/pool/" + stockCode,
					maxDay - 1, maxDay);
			return history.getClosingPrice()[0];
		} else {
			// buy按照当日closing, sell按照第二天open
			IHStatisticsHistory history = HistoryDataProvider.getData("data/pool/" + stockCode,
					day - 1, day + 1);
			if (tradeDirection > 0) {
				return history.getClosingPrice()[0];
			} else {
				return history.getOpeningPrice()[1];
			}
		}
	}

	private TransactionDetail getLastTransactionDetail() {
		if (transactionDetails.isEmpty()) {
			return null;
		} else {
			return transactionDetails.get(transactionDetails.size() - 1);
		}
	}

	private void logTransactionDetail(int day, int tradeDirection) {
		TransactionDetail detail = getLastTransactionDetail();
		if (detail == null) {
			detail = new TransactionDetail();
			detail.setEntryDay(day);
			detail.setEntryValue(currentPortfolioValue(day));
			detail.setTradeType(tradeDirection > 0 ? "Long" : "Short");
			transactionDetails.add(detail);
		} else {
			detail.setExitDay(day);
			detail.setExitValue(currentPortfolioValue(day));
			detail.setProfit(detail.getExitValue() - detail.getEntryValue());
		}
	}
}
