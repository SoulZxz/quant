package com.ricequant.strategy.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.ricequant.strategy.def.IHStatisticsHistory;

public class HistoryDataProvider {

	private static FileUtils reader = new FileUtils();

	private static Map<String, DummyHistoryData> cache = new HashMap<String, DummyHistoryData>();

	public static IHStatisticsHistory getData(String fileName) {
		return getData(fileName, null, null);
	}

	public static IHStatisticsHistory getData(String fileName, Integer from, Integer to) {
		DummyHistoryData data = cache.get(fileName);

		if (data == null) {
			String content = reader.readFileAsString(fileName);
			List<Double> high = new ArrayList<Double>();
			List<Double> low = new ArrayList<Double>();
			List<Double> open = new ArrayList<Double>();
			List<Double> close = new ArrayList<Double>();

			Scanner scanner = new Scanner(content);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (StringUtils.isNotBlank(line)) {
					String[] prices = line.split(",");
					high.add(Double.valueOf(prices[0]).doubleValue());
					low.add(Double.valueOf(prices[1]).doubleValue());
					open.add(Double.valueOf(prices[2]).doubleValue());
					close.add(Double.valueOf(prices[3]).doubleValue());
				}
			}
			IOUtils.closeQuietly(scanner);

			data = new DummyHistoryData();
			data.setHighPrice(copy(high.toArray(new Double[high.size()])));
			data.setLowPrice(copy(low.toArray(new Double[low.size()])));
			data.setOpeningPrice(copy(open.toArray(new Double[open.size()])));
			data.setClosingPrice(copy(close.toArray(new Double[close.size()])));

			cache.put(fileName, data);
		}

		int fromIndex = from == null ? 0 : from.intValue();
		int toIndex = to == null ? data.getClosingPrice().length : to.intValue();

		DummyHistoryData copy = new DummyHistoryData();
		copy.setHighPrice(Arrays.copyOfRange(data.getHighPrice(), fromIndex, toIndex));
		copy.setLowPrice(Arrays.copyOfRange(data.getLowPrice(), fromIndex, toIndex));
		copy.setOpeningPrice(Arrays.copyOfRange(data.getOpeningPrice(), fromIndex, toIndex));
		copy.setClosingPrice(Arrays.copyOfRange(data.getClosingPrice(), fromIndex, toIndex));
		return copy;
	}

	private static double[] copy(Double[] src) {
		double[] result = new double[src.length];
		for (int i = 0; i < src.length; i++) {
			result[i] = src[i].doubleValue();
		}
		return result;
	}

}
