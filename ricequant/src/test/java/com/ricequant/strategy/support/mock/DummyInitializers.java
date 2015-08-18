package com.ricequant.strategy.support.mock;

import com.ricequant.strategy.def.IHInitializers;
import com.ricequant.strategy.def.mock.EventHandlers;
import com.ricequant.strategy.def.mock.ParamBoolean;
import com.ricequant.strategy.def.mock.ParamDouble;
import com.ricequant.strategy.def.mock.PickerFunction;

public class DummyInitializers implements IHInitializers {

	private RunnerContext runnerContext;

	private ParamDouble slippage = new DummyParamDouble();

	private ParamDouble commission = new DummyParamDouble();

	private ParamBoolean shortsell = new DummyParamBoolean();

	private EventHandlers events = new DummyEventHandlers();

	@Override
	public ParamDouble slippage() {
		return slippage;
	}

	@Override
	public ParamDouble commission() {
		return commission;
	}

	@Override
	public ParamBoolean shortsell() {
		return shortsell;
	}

	@Override
	public EventHandlers events() {
		return events;
	}

	@Override
	public void instruments(PickerFunction pickerFactory) {
		pickerFactory.pick(new DummyInstrumentsPicker(runnerContext));
	}

	public void setRunnerContext(RunnerContext runnerContext) {
		this.runnerContext = runnerContext;
	}

}
