package com.ricequant.strategy.def;

/**
 * 这个枚举类型代表了委托被拒绝的原因。它包含了如下几个值：
 * 
 * @author Administrator
 *
 */
public enum HOrderRejectReasonEnum {

	/**
	 * 我们目前不支持融资单（margin order），因此您如果尝试发送一个会让您的现金在成交之后为负的委托，它将会被拒绝。
	 */
	InsufficientFund,

	/**
	 * 我们暂时也不支持融券单（short sell order），因此如果您尝试发送一个卖出多于您持仓的委托，它也将会被拒绝。
	 */
	InsufficientPosition,

	/**
	 * 它不应该发生。它的出现往往代表我们平台的内部错误。如果您看到了这个错误，我们非常希望您能跟我们分享您是如何引发它的，让我们解决问题，
	 * 更好的为您服务。
	 * 
	 */
	Other

}
