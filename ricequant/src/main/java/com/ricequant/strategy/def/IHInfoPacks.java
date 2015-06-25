package com.ricequant.strategy.def;

/**
 * 
 * 在事件处理其中，访问除了关于该事件的信息之外的其他信息是至关重要的。例如当获得某一个股票的行情时，您可能想知道另一个股票的行情，
 * 或者想知道自己持仓中剩余的现金数量等。这些信息都可以在这个IHInfoPacks找到。
 * 
 * 在每日数据回测中，我们并不支持查看自己每一笔委托及成交的功能，因为它们会被强制转化为市场单，而且会被立即成交，
 * 相关的信息会被反映在IHPosition及IHPortfolio里。
 * 
 * @author Administrator
 *
 */
public interface IHInfoPacks {

	/**
	 * 返回了一个IHInstrument对象
	 * 
	 * @param idOrSymbol
	 * @return
	 */
	IHInstrument instrument(String idOrSymbol);

	/**
	 * 这个结构包含了策略投资组合的信息，现在我们仅支持每日历史数据回测，因此它代表的是改天结束时候的投资组合情况
	 * 
	 * @return
	 */
	IHPortfolio portfolio();

	/**
	 * 这个结构包含了您的投资策略组合的每只股票的持仓情况
	 * @param instrument
	 * @return
	 */
	IHPosition position(IHInstrument instrument);

	/**
	 * 这个结构包含了您的投资策略组合的每只股票的持仓情况
	 * @param idOrSymbol
	 * @return
	 */
	IHPosition position(String idOrSymbol);
	
	IHRuntime runtime();
}
