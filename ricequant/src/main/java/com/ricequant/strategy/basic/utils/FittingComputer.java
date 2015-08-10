package com.ricequant.strategy.basic.utils;

import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;

public class FittingComputer {

	public static double[] normalize(double[] input) {
		// 正则化到0-1之间
		double yMin = input[0];
		double yMax = input[0];

		for (double value : input) {
			yMin = Math.min(yMin, value);
			yMax = Math.max(yMax, value);
		}

		double[] normalized = new double[input.length];
		for (int i = 0; i < input.length; i++) {
			normalized[i] = (input[i] - yMin) / (yMax - yMin);
		}

		return normalized;
	}

	/**
	 * 1阶线性拟合
	 *
	 * @param input
	 *            正则化过的数组
	 * @return 拟合系数数组
	 */
	public static double[] linearFitting(double[] input) {
		double xStep = 1.0 / (input.length - 1);

		// normalize
		WeightedObservedPoints points = new WeightedObservedPoints();
		for (int i = 0; i < input.length; i++) {
			points.add(i * xStep, input[i]);
		}

		// 1阶拟合, 判断是否以大于coeffTH的斜率匀速上升
		PolynomialCurveFitter fitter = PolynomialCurveFitter.create(1);
		return fitter.fit(points.toList());
	}

	/**
	 * 计算分段插值拟合的导数值
	 *
	 * @param input
	 *            正则化过的数组
	 * @return
	 */
	public static double[] splineDerivatives(double[] input) {
		double xStep = 1.0 / (input.length - 1);

		double[] x = new double[input.length];
		double[] y = new double[input.length];

		for (int i = 0; i < input.length; i++) {
			x[i] = i * xStep;
			y[i] = input[i];
		}

		SplineInterpolator fitter = new SplineInterpolator();
		PolynomialSplineFunction func = fitter.interpolate(x, y);

		double[] derivatives = new double[input.length];
		for (int i = 0; i < derivatives.length; i++) {
			derivatives[i] = func.derivative().value(x[i]);
		}

		return derivatives;
	}
}
