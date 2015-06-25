package com.ricequant.strategy.def;

import java.util.function.Predicate;

public interface IHInstrumentsPicker {

	/**
	 * 这个方法将“idOrSymbol”所代表的股票加入“universe”。参看约定中的idOrSymbol子项
	 * 
	 * @param idOrSymbol
	 * @return
	 */
	IHInstrumentsPicker add(String... idOrSymbol);

	/**
	 * 这个方法将所有可用的股票加入“universe”。通常这一步是为了其他的某些操作而做准备，
	 * 单独使用这个功能几乎必然导致策略运行超时或因内存超标而被强行终止
	 * 
	 * @return
	 */
	IHInstrumentsPicker all();

	/**
	 * 这是一个与 IHInstrumentsPicker.add 相反的方法。 它将idOrSymbol所表示的股票从“universe”中删除
	 * 
	 * @param idOrSymbol
	 * @return
	 */
	IHInstrumentsPicker remove(String... idOrSymbol);

	/**
	 * 这个方法会遍历当前的“universe”，并交给传入的“filter”。依据“filter”的返回值（Predicate是Java
	 * 8的内建函数，它会返回一个布尔值）来决定是否保留该股票。true为保留，false则舍弃。
	 * 
	 * @param filter
	 * @return
	 */
	IHInstrumentsPicker filter(Predicate<IHInstrument> filter);
}
