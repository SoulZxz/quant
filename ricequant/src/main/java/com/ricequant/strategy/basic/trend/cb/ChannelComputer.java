package com.ricequant.strategy.basic.trend.cb;

public class ChannelComputer {

	public ChannelItem[] compute(double[] input, int period) {
		ChannelItem[] out = new ChannelItem[input.length - period + 1];

		// 初始化
		double currentLowest = input[0];
		double currentHighest = input[0];

		for (int i = 0; i < input.length; i++) {
			// 超过channel周期, 简化为取上一段channel的第一个值, 如果是最大/小值, 重新计算, 否则取当前元素与最大/小值比较
			if (i > period) {
				double dropValue = input[i - period - 1];

				// 重算最小值
				if (dropValue == currentLowest) {
					currentLowest = this.computeLowest(input, period, i - period);
				} else {
					currentLowest = Math.min(currentLowest, input[i]);
				}

				// 重算最大值
				if (dropValue == currentHighest) {
					currentHighest = this.computeHighest(input, period, i - period);
				} else {
					currentHighest = Math.max(currentHighest, input[i]);
				}

				out[i - period] = new ChannelItem(currentHighest, currentLowest);
			}
			// 未到channel周期, 取当前所有元素的最大/小值
			else {
				currentLowest = Math.min(currentLowest, input[i]);
				currentHighest = Math.max(currentHighest, input[i]);

				if (i == period) {
					out[0] = new ChannelItem(currentHighest, currentLowest);
				}
			}
		}

		return out;
	}

	private double computeLowest(double[] input, int period, int start) {
		double currentLowest = input[start];
		for (int i = start; i < start + period; i++) {
			currentLowest = Math.min(currentLowest, input[i]);
		}
		return currentLowest;
	}

	private double computeHighest(double[] input, int period, int start) {
		double currentHighest = input[start];
		for (int i = start; i < start + period; i++) {
			currentHighest = Math.max(currentHighest, input[i]);
		}
		return currentHighest;
	}
}
