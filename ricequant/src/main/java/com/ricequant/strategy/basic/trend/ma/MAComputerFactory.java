package com.ricequant.strategy.basic.trend.ma;

public class MAComputerFactory {

	private static final MovingAvgComputer sma = new SMAComputer();

	private static final MovingAvgComputer ema = new ExMAComputer();

	private static final MovingAvgComputer wma = new WMAComputer();

	public static MovingAvgComputer create(MAType type) {
		switch (type) {
		case SMA:
			return sma;
		case EMA:
			return ema;
		case WMA:
			return wma;
		default:
			throw new RuntimeException("unsupported MAType " + type);
		}
	}

}
