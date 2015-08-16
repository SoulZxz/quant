package com.ricequant.strategy.support.mock;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.ricequant.strategy.def.IHInfoPacks;
import com.ricequant.strategy.def.IHInstrument;
import com.ricequant.strategy.def.IHPortfolio;
import com.ricequant.strategy.def.IHPosition;
import com.ricequant.strategy.def.IHRuntime;
import com.ricequant.strategy.def.IHStatisticsHistory;
import com.ricequant.strategy.support.HistoryDataProvider;

public class DummyInfoPacks implements IHInfoPacks {

	private int day;

	private Map<String, DummyPosition> positions = new HashMap<String, DummyPosition>();

	@Override
	public IHInstrument instrument(String idOrSymbol) {
		throw new IllegalArgumentException("unimplemented");
	}

	@Override
	public IHPortfolio portfolio() {
		double profitAndLoss = 0;

		for (Entry<String, DummyPosition> position : positions.entrySet()) {
			IHStatisticsHistory history = HistoryDataProvider.getData(position.getKey(), day - 1,
					day + 1);
			double[] closings = history.getClosingPrice();
			double deltaValue = (closings[1] - closings[0])
					* position.getValue().getNonClosedTradeQuantity();
			profitAndLoss += deltaValue;
		}

		return new DummyPortfolio(profitAndLoss);
	}

	@Override
	public IHPosition position(IHInstrument instrument) {
		throw new IllegalArgumentException("unimplemented");
	}

	@Override
	public IHPosition position(String idOrSymbol) {
		return positions.get(idOrSymbol);
	}

	@Override
	public IHRuntime runtime() {
		throw new IllegalArgumentException("unimplemented");
	}

}
