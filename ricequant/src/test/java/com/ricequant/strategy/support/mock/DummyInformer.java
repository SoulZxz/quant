package com.ricequant.strategy.support.mock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ricequant.strategy.def.IHInformer;

public class DummyInformer implements IHInformer {

	private Logger log = LogManager.getLogger(DummyInformer.class);

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
		log.info(o);
	}

	@Override
	public void info(String s) {
		log.info(s);
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
