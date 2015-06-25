package com.ricequant.strategy.def;

public interface IHInformer {

	void debug(Object o);

	void debug(String s);

	void debug(String s, Object... objects);

	void error(Object o);

	void error(String s);

	void error(String s, Object... objects);

	void info(Object o);

	void info(String s);

	void info(String s, Object... objects);

	void warn(Object o);

	void warn(String s);

	void warn(String s, Object... objects);

	/**
	 * 画自定义曲线
	 * 
	 * @param seriesName
	 * @param value
	 * @return
	 */
	IHInformer plot(String seriesName, double value);
}
