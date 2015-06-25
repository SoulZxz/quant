package com.ricequant.strategy.def;

public interface IHPosition {

	/**
	 * 获得总共买入成交的股数。例如：“如果您的投资组合没有任何000001.XSHE的买单成交，那么这个方法将会返回0”。
	 * 
	 * @return
	 */
	double getBoughtQuantity();

	/**
	 * 获得总共的卖出成交的股数。例如：“如果您的投资组合有000001.XSHE200股数的卖出成交记录，和100股数的买入成交记录，
	 * 那么这个方法将会反馈200”。
	 * 
	 * @return
	 */
	double getSoldQuantity();

	/**
	 * 获得已买成交的总值。它等于每一笔的买入成交的“成交价 * 成交股数”的和，总为正值。
	 * 
	 * @return
	 */
	double getBoughtValue();

	/**
	 * 获得已卖成交的总值。和上面类似，它等于每一笔的卖出成交的“成交价 * 成交股数”的和，总为正值。
	 * 
	 * @return
	 */
	double getSoldValue();

	/**
	 * 获得总未平仓的交易股数，e.g.：“你的投资组合拥有400股买入成交和100股卖出成交的000001.XSHE，
	 * 那么这个方法将会返回300告诉你该持有股还有300股未被平仓”。
	 * 
	 * @return
	 */
	double getNonClosedTradeQuantity();

	/**
	 * 获得该持仓的所有的落单数目。
	 * 
	 * @return
	 */
	double getTotalOrders();

	/**
	 * 获得该持仓的所有成交单数目。
	 * 
	 * @return
	 */
	double getTotalTrades();

}
