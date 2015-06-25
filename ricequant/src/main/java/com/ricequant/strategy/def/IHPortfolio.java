package com.ricequant.strategy.def;

public interface IHPortfolio {

	/**
	 * 获得投资组合的初始资金
	 * 
	 * @return
	 */
	double getInitialCash();

	/**
	 * 获得投资组合的剩余资金
	 * 
	 * @return
	 */
	double getAvailableCash();

	/**
	 * 获得投资组合的总收益
	 * 
	 * @return
	 */
	double getTotalReturn();

	/**
	 * 获得投资组合的目前日期的单日收益
	 * 
	 * @return
	 */
	double getDailyReturn();

	/**
	 * 获得投资组合的当前市场价值
	 * 
	 * @return
	 */
	double getMarketValue();

	/**
	 * 获得投资组合的当前总价值，包含市场价值和剩余资金
	 * 
	 * @return
	 */
	double getPortfolioValue();

	/**
	 * 获得投资组合的目前日期的金钱盈亏
	 * 
	 * @return
	 */
	double getProfitAndLoss();

	/**
	 * 获得投资组合的目前的年化收益率
	 * 
	 * @return
	 */

	double getAnnualizedAvgReturns();

	/**
	 * 获得投资组合的应收股息分红
	 * 
	 * @return
	 */
	double getDividendReceivable();

}
