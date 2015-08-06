package com.ricequant.strategy.basic.position;

import com.ricequant.strategy.basic.ExitSignalGenerator;
import com.ricequant.strategy.def.IHPortfolio;
import com.ricequant.strategy.def.IHStatistics;

public class ProfitExitSignalGenerator implements ExitSignalGenerator {

	private double profitTarget;

	private double lossTrigger;

	public ProfitExitSignalGenerator(double profitTarget, double lossTrigger) {
		super();
		this.profitTarget = profitTarget;
		this.lossTrigger = lossTrigger;
	}

	@Override
	public double generateSignal(IHStatistics stat, IHPortfolio portfolio) {
		if (portfolio.getProfitAndLoss() <= profitTarget
				&& portfolio.getProfitAndLoss() >= lossTrigger) {
			return 0;
		} else {
			// 达到盈利点, 发止盈信号
			if (portfolio.getProfitAndLoss() > profitTarget) {
				return 1;
			} else {
				return -1;
			}
		}
	}

}
