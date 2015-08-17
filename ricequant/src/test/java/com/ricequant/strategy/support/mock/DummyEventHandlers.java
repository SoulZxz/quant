package com.ricequant.strategy.support.mock;

import com.ricequant.strategy.def.IHStatisticsUpdateHandler;
import com.ricequant.strategy.def.mock.EventHandlers;

public class DummyEventHandlers implements EventHandlers {

	private IHStatisticsUpdateHandler handler;

	@Override
	public void statistics(IHStatisticsUpdateHandler handler) {
		this.handler = handler;
		System.out.println("handler " + handler.getClass());
	}

	@Override
	public IHStatisticsUpdateHandler getUpdateHandler() {
		return handler;
	}

}
