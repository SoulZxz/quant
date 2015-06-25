package com.ricequant.strategy.def;

/**
 * 每日回测时，每个委托都将被即时成交或拒绝，因此所有委托并没有像在真实交易一样存在“等待成交”的状态。于是像修改和撤单这样的操作也变得没有意义了。
 * 为了简单起见，我们从SDK中暂时隐藏了这两种功能，当我们支持更复杂的委托状态时再开放出来
 * 
 * @author Administrator
 *
 */
public interface IHOrder {

	/**
	 * 返回与此委托相关联的IHInstrument对象。
	 * 
	 * @return
	 */
	IHInstrument getInstrument();

	/**
	 * 返回该委托的成交价。如果委托被拒绝，它会返回0。因为所有的委托在每日回测下都将是市场单，所以在发送委托的时候这个字段并不能被设置。
	 * 
	 * @return
	 */
	double getPrice();

	/**
	 * 返回被成交的股票数量。它要么是0，要么等于您发送时设置的值。如果您发送时使用的是IHOrderQuantityPicker.lots()，
	 * 它仍旧会被转化成股数。
	 * 
	 * @return
	 */
	double getFilledShares();

	/**
	 * 返回被成交的股票数量。它要么是0，要么等于您发送时设置的值
	 * 
	 * @return
	 */
	double getFilledLots();

}
