package com.ricequant.strategy.support.mock;

public class RunnerContext {

	private StockPool stockPool = new StockPool();

	private PortfolioHolder portfolioHolder = new PortfolioHolder();

	public StockPool getStockPool() {
		return stockPool;
	}

	public void setStockPool(StockPool stockPool) {
		this.stockPool = stockPool;
	}

	public PortfolioHolder getPortfolioHolder() {
		return portfolioHolder;
	}

	public void setPortfolioHolder(PortfolioHolder portfolioHolder) {
		this.portfolioHolder = portfolioHolder;
	}

}
