package com.ricequant.strategy.def;

/**
 * 这个工厂类对象是一个创建委托的地方。目前只支持普通的订单（order），未来可能会支持报价单（quote）等类型。
 * 
 * @author Administrator
 *
 */
public interface IHTransactionFactory {

	/**
	 * 这几个方法并不会直接创建一个新的委托，而是指定了一部分参数，由其他的方法继续完成构造。
	 * 
	 * 它们都将检查所选的股票是否存在于“universe”。目前只有存在于“universe”的股票才能发出委托。
	 * 
	 * 他们都返回一个IHOrderQuantityPicker对象，这是一个创建完整委托的中间步骤，您可以在接下来的步骤中完成创建并最终发送
	 * 
	 * @param idOrSymbol
	 * @return
	 */
	IHOrderQuantityPicker buy(String idOrSymbol);

	IHOrderQuantityPicker sell(String idOrSymbol);
}
