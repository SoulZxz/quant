package com.ricequant.strategy.support.mock;

import com.ricequant.strategy.def.mock.ParamDouble;

public class DummyParamDouble implements ParamDouble {

	float percent;

	@Override
	public void percent(float percent) {
		this.percent = percent;
	}

}
