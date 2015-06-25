package com.ricequant.strategy.def;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * 	当您选择了委托的方向（买/卖），目标股票以及数量之后，一个最简单的委托便已经形成了（因此这个API名叫“Base”。因为我们目前只支持每日回测，因此所有的委托都将会是一个市场单，可以选择的参数只有这三个而已。

	以后我们会加入新的委托类型，敬请期待！
 * @author Administrator
 *
 */
public interface IHOrderBuilderBase {




	/**
	 * 选择完委托数量之后，委托并没有被发送到市场。您必须调用这个commit方法。commit方法返回一个IHOrder对象，在这里您可以回顾您发送出去的委托，并且可以获得它的实际成交价格。
	 * @return
	 */
	IHOrder commit();
	

	

	/**
	 * 完成委托的发送。委托发送的过程是异步的，因此会有一个带有两个回调参数的commit方法
	 * @param onSuccess
	 * @param onRejected
	 * @return
	 */
	IHOrder commit(Consumer<IHOrder> onSuccess,
	               BiConsumer<HOrderRejectReasonEnum, IHOrder> onRejected);
	               
}
