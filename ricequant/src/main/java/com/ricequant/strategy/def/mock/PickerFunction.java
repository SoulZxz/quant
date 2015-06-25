package com.ricequant.strategy.def.mock;

import com.ricequant.strategy.def.IHInstrumentsPicker;

public interface PickerFunction {

	IHInstrumentsPicker pick(IHInstrumentsPicker universe);

}
