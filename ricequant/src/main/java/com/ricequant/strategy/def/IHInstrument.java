package com.ricequant.strategy.def;

public interface IHInstrument {

	/**
	 * 返回该股票的唯一标识符，参看几个约定
	 * 
	 * @return
	 */
	String getOrderBookID();

	/**
	 * 返回股票名称。参看几个约定。
	 * 
	 * @return
	 */
	String getSymbol();

	/**
	 * 返回股票名称的简称。在中国市场，这个字段绝大部分时候是股票名称的拼音首字母。例如“上海石化” 的简称是“SHSH”，“连云港”则简称“LYG”。
	 * 
	 * @return
	 */
	String getAbbrevSymbol();

	/**
	 * 返回股票每手的数量。在上海及深圳证券交易所，这个数总是100。
	 * 
	 * @return
	 */
	double getRoundLot();
}
