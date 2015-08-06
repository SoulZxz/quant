package com.ricequant.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.ricequant.strategy.basic.EntrySignalGenerator;
import com.ricequant.strategy.basic.di.adx.ADXComputer;
import com.ricequant.strategy.basic.di.vhf.VHFComputer;
import com.ricequant.strategy.basic.oscillator.rsi.RSIEntrySignalGenerator;
import com.ricequant.strategy.basic.utils.ExtremumComputer;
import com.ricequant.strategy.def.HPeriod;
import com.ricequant.strategy.def.IHStatisticsHistory;
import com.ricequant.strategy.support.DummyStatistics;
import com.ricequant.strategy.support.FileUtils;

public class EntryMatcher {

	public static void main(String[] args) {
		String fileName = "data/000528-20131101-20141031.csv";
		int startDay = 5;
		int endDay = 226;

		Map<Integer, String> map = loadDateMap(fileName);

		VHFComputer vhfComputer = new VHFComputer();
		ADXComputer adxComputer = new ADXComputer();
		ExtremumComputer extremumComputer = new ExtremumComputer();
		
		List<EntrySignalGenerator> gens = new ArrayList<EntrySignalGenerator>();
//		gens.add(new MAEntrySignalGenerator(20, 40, MAComputer.MA_TYPE_SMA, MAComputer.MA_TYPE_SMA));
//		gens.add(new RSIEntrySignalGenerator(35, 65, 40, 14));
//		gens.add(new VBEntrySignalGenerator(20, VBEntrySignalGenerator.RVT_PREVIOUS_CLOSE,
//				VBEntrySignalGenerator.VMT_PRICE_CHANGE_STD, 1));
		
		for (int i = startDay; i < endDay; i++) {
			DummyStatistics stat = new DummyStatistics(i, fileName);
			for (EntrySignalGenerator gen : gens) {
				double signal = gen.generateSignal(stat);
				if (signal != 0) {
					// double[] vhfs = vhfComputer.computeVHF(stat.history(20,
					// HPeriod.Day)
					// .getClosingPrice(), 20);
					// double vhf = vhfs[vhfs.length - 1];
					double vhf = 0;
					
					IHStatisticsHistory history = stat.history(startDay, HPeriod.Day);
					double[] adxs = adxComputer.computeADX(history.getClosingPrice(), history.getHighPrice(), history.getLowPrice(), 14);
					double adx = adxs[adxs.length - 1];
					
					System.out.println("on " + map.get(i) + " " + gen.getClass().getSimpleName()
							+ " generate signal " + signal + " vhf " + adx);
				}
			}
			
			IHStatisticsHistory history = stat.history(startDay, HPeriod.Day);
			double[] close = history.getClosingPrice();
			int[] result = extremumComputer.findExtremum(close, 5, 0.03);
			if(result[0] != 0){
				System.out.println("found Extremum " + result[0] + " on day " + map.get(i - result[1] - 1) + " "+ close[close.length - 5 + result[1]]);
			}
		}
	}

	private static Map<Integer, String> loadDateMap(String fileName) {
		FileUtils utils = new FileUtils();
		String content = utils.readFileAsString(fileName);

		Map<Integer, String> result = new HashMap<Integer, String>();

		Scanner scanner = new Scanner(content);
		int i = 0;
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (StringUtils.isNotBlank(line)) {
				String[] prices = line.split(",");
				result.put(i, prices[5]);
			}
			i++;
		}
		IOUtils.closeQuietly(scanner);

		return result;
	}

}
