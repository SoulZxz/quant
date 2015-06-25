package com.ricequant.strategy.def;

import com.ricequant.strategy.def.mock.EventHandlers;
import com.ricequant.strategy.def.mock.PickerFunction;
import com.ricequant.strategy.def.mock.ParamBoolean;
import com.ricequant.strategy.def.mock.ParamDouble;

public interface IHInitializers {

	/**
	 * 价差滑点设置，Param.peercent(0.246)表示0.246%
	 * 
	 * @return
	 */
	ParamDouble slippage();

	/**
	 * 券商手续费，单位%
	 * 
	 * @return
	 */
	ParamDouble commission();

	/**
	 * 超卖设置
	 * 
	 * @return
	 */
	ParamBoolean shortsell();

	EventHandlers events();

	void instruments(PickerFunction pickerFactory);
}
