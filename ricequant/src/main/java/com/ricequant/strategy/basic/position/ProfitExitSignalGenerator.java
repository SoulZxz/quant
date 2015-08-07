package com.ricequant.strategy.basic.position;

import com.ricequant.strategy.basic.ExitSignalGenerator;
import com.ricequant.strategy.def.IHPortfolio;
import com.ricequant.strategy.def.IHStatistics;

public class ProfitExitSignalGenerator implements ExitSignalGenerator {

	private double profitTarget;

	private double lossTrigger;

	private double highestUnclosedProfitHeld;

	private double currentUnclosedProfitHeld;

	private double unclosedPositionInitValue;

	public ProfitExitSignalGenerator(double profitTarget, double lossTrigger) {
		super();
		this.profitTarget = profitTarget;
		this.lossTrigger = lossTrigger;
	}

	@Override
	public double generateSignal(IHStatistics stat, IHPortfolio portfolio) {
		currentUnclosedProfitHeld = currentUnclosedProfitHeld + portfolio.getProfitAndLoss();
		highestUnclosedProfitHeld = Math.max(highestUnclosedProfitHeld, currentUnclosedProfitHeld);

		double profitAndLossRate = currentUnclosedProfitHeld / unclosedPositionInitValue;
		double highestProfitRate = highestUnclosedProfitHeld / unclosedPositionInitValue;

		// 达到盈利点, 发止盈信号; 从最高盈利点发生drawdown达到lossTrigger, 止损或止浮盈
		if (profitAndLossRate <= profitTarget
				&& profitAndLossRate - highestProfitRate >= lossTrigger) {
			return 0;
		} else {
			if (profitAndLossRate > profitTarget) {
				return 1;
			} else {
				return -1;
			}
		}
	}

}
