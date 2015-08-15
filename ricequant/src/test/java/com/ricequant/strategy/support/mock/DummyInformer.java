package com.ricequant.strategy.support.mock;

import com.ricequant.strategy.def.IHInformer;

public class DummyInformer implements IHInformer {

	@Override
	public void debug(Object o) {
		// TODO Auto-generated method stub

	}

	@Override
	public void debug(String s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void debug(String s, Object... objects) {
		// TODO Auto-generated method stub

	}

	@Override
	public void error(Object o) {
		// TODO Auto-generated method stub

	}

	@Override
	public void error(String s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void error(String s, Object... objects) {
		// TODO Auto-generated method stub

	}

	@Override
	public void info(Object o) {
		System.out.println(o);
	}

	@Override
	public void info(String s) {
		System.out.println(s);
	}

	@Override
	public void info(String s, Object... objects) {
		// TODO Auto-generated method stub

	}

	@Override
	public void warn(Object o) {
		// TODO Auto-generated method stub

	}

	@Override
	public void warn(String s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void warn(String s, Object... objects) {
		// TODO Auto-generated method stub

	}

	@Override
	public IHInformer plot(String seriesName, double value) {
		return this;
	}

}
