package com.ricequant.strategy.basic.utils;

public class BehaviorComputer {

	/**
	 * 计算input元素cross过targetValue的次数
	 *
	 * @param input
	 * @param targetValue
	 * @param decisionDelay
	 *            决策延迟, 忽略最后decisionDelay个input元素的cross
	 * @return cross次数数组, [0]是向上cross次数, [1]是向下cross次数, [2]是那种情况先发生,
	 *         1是向上cross先发生, -1是向下cross先发生
	 */
	public static int[] simpleCross(double[] input, double targetValue, int decisionDelay) {
		int crossUp = 0;
		int crossDown = 0;
		int whichCrossFirst = 0;

		double lastValue = input[0];
		for (int i = 0; i < input.length; i++) {
			double currentValue = input[i];

			boolean canChangeValue = i <= input.length - 1 - decisionDelay;

			if (lastValue < targetValue && currentValue > targetValue && canChangeValue) {
				crossUp++;
				if (whichCrossFirst == 0) {
					whichCrossFirst = 1;
				}
			}

			if (lastValue > targetValue && currentValue < targetValue && canChangeValue) {
				crossDown++;
				if (whichCrossFirst == 0) {
					whichCrossFirst = -1;
				}
			}
		}

		int[] result = new int[3];
		result[0] = crossUp;
		result[1] = crossDown;
		result[2] = whichCrossFirst;
		return result;
	}

}
