package com.ricequant.strategy.support.mock;

import com.ricequant.strategy.def.mock.ParamBoolean;

public class DummyParamBoolean implements ParamBoolean {

	boolean allow;

	@Override
	public void allow() {
		allow = true;
	}

	@Override
	public void deny() {
		allow = false;
	}

}
