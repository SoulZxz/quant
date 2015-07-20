package com.ricequant.strategy.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.ricequant.strategy.def.IHStatisticsHistory;

public class HistoryDataProvider {

	private static FileUtils reader = new FileUtils();

	public static IHStatisticsHistory getData(String fileName) {
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

		DummyHistoryData result = new DummyHistoryData();
		result.setHighPrice(copy(high.toArray(new Double[high.size()])));
		result.setLowPrice(copy(low.toArray(new Double[low.size()])));
		result.setOpeningPrice(copy(open.toArray(new Double[open.size()])));
		result.setClosingPrice(copy(close.toArray(new Double[close.size()])));
		return result;
	}

	private static double[] copy(Double[] src) {
		double[] result = new double[src.length];
		for (int i = 0; i < src.length; i++) {
			result[i] = src[i].doubleValue();
		}
		return result;
	}

}
