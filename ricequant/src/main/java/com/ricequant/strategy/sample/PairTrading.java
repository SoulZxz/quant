package com.ricequant.strategy.sample;

//1.��������ѡȡ��ֻ�ɼ�������Ĺ�Ʊ��
//2.������ֻ��Ʊ����ʷ�ɼ���һ�����Իع飨OLS���õ�betaֵ����hedge ratio������hedge ratio���ǿ��Ծ���ÿ�ζԳ�ʱ��ͷ�硣��������betaֵΪһ������ֵ����ÿ��һ�μ���һ�λز⵱��֮ǰ90�����ʱ���betaֵ��
//3.���ü���õ���betaֵ���ǿ��Խ���֧��Ʊ�ļ۲spread����һ���õ�һ�������ٵ�ֵ�������۲�ƫ����ʷƽ���۲���ٸ���׼���֮ΪzScore�����۲�ܴ��ʱ��zScore����һ�����������۲��С��ʱ��zScore��һ����ֵ��
//4.�볡�����жϣ���zScore�ľ���ֵ����2��ʱ�������볡����zScore�ľ���ֵС��1��ʱ�򣬳�����
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import com.ricequant.strategy.def.HPeriod;
import com.ricequant.strategy.def.IHInformer;
import com.ricequant.strategy.def.IHInitializers;
import com.ricequant.strategy.def.IHStrategy;

public class PairTrading implements IHStrategy {

	double zScore;

	double beta;

	double shareStock1;

	double shareStock2;

	double spread;

	double buyShare;

	@Override
	public void init(IHInformer informer, IHInitializers initializers) {

		String stockId1 = "600016.XSHG";
		String stockId2 = "000001.XSHE";
		double closePrice[][] = new double[90][2];
		double spread[] = new double[90];
		int numRows = closePrice.length;
		int betShare = 10000;
		int period = 89;

		SimpleRegression regression = new SimpleRegression();
		DescriptiveStatistics stat = new DescriptiveStatistics();
		initializers.instruments((universe) -> universe.add(stockId1, stockId2));
		initializers.shortsell().allow();
		initializers.events().statistics(
				(stats, info, trans) -> {

					double[] closePxInStockId1 = stats.get(stockId1)
							.history(period + 1, HPeriod.Day).getClosingPrice();
					double[] closePxInStockId2 = stats.get(stockId2)
							.history(period + 1, HPeriod.Day).getClosingPrice();

					for (int row = 0; row < numRows; ++row) {
						closePrice[row][0] = closePxInStockId1[row];
						closePrice[row][1] = closePxInStockId2[row];

					}

					regression.addData(closePrice);
					beta = regression.getSlope();
					buyShare = beta * betShare;
					for (int row = 0; row < numRows; ++row) {
						spread[row] = closePxInStockId2[row] - beta * closePxInStockId1[row];
						stat.addValue(spread[row]);
					}

					double mean = stat.getMean();
					double std = stat.getStandardDeviation();

					zScore = ((closePxInStockId2[89] - regression.getSlope()
							* closePxInStockId1[89]) - mean)
							/ std;
					informer.plot("zScore", zScore);
					informer.plot("beta", beta);
					informer.plot("spread", (closePxInStockId2[89] - regression.getSlope()
							* closePxInStockId1[89]));

					shareStock1 = info.position(stockId1).getNonClosedTradeQuantity();
					shareStock2 = info.position(stockId2).getNonClosedTradeQuantity();

					// enter the market
					if (zScore > 1.5) {
						trans.sell(stockId2).shares(-buyShare).commit();
						trans.buy(stockId1).shares(betShare).commit();
					}

					// enter the market
					if (zScore < -1.5) {
						trans.sell(stockId1).shares(betShare).commit();
						trans.buy(stockId2).shares(buyShare).commit();
					}

					// clear the position and exit the market
					if ((zScore < 0.5) && (zScore > -0.5)) {
						if (shareStock1 > 0) {
							trans.sell(stockId1).shares(shareStock1).commit();
						}
						if (shareStock1 < 0) {
							trans.buy(stockId1).shares(-shareStock1).commit();
						}
						if (shareStock2 > 0) {
							trans.sell(stockId2).shares(shareStock2).commit();
						}
						if (shareStock2 < 0) {
							trans.buy(stockId2).shares(-shareStock2).commit();

						}
					}
				});
	}
}
