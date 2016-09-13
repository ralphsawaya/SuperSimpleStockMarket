package com.simplestockmarket.server;

import org.apache.log4j.Logger;

public abstract class Stock {

	protected String symbol = "";
	protected Double lastDividend = 0.0;
	protected Double parValue = 0.0;
	protected Double lastestTradedPrice = 0.0;
	protected Logger logger = Logger.getLogger(TradeTransaction.class);

	public Stock(String symbol, Double lastDividend, Double parValue) {
		this.setSymbol(symbol);
		this.setLastDividend(lastDividend);
		this.setParValue(parValue);
		try {
			validateStock();
		} catch (Exception e) {
			logger.error("Stock failed to get initialized!");
		}
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Double getLastDividend() {
		return lastDividend;
	}

	public void setLastDividend(Double lastDividend) {
		this.lastDividend = lastDividend;
	}

	public Double getParValue() {
		return parValue;
	}

	public void setParValue(Double parValue) {
		this.parValue = parValue;
	}

	public void setLastestTradedPrice(Double lastestTradedPrice) {
		this.lastestTradedPrice = lastestTradedPrice;
	}

	public double getLastestTradedPrice() {
		return lastestTradedPrice;
	}

	public abstract Double CalculateDividendYield(Double price);

	public double calculatePeRatio(Double price) {
		double peRatio = -1.0;
		double dividentYield = CalculateDividendYield(price);
		if (dividentYield > 0.0) {
			peRatio = price / dividentYield;
		}
		return peRatio;
	}

	protected void validateStock() throws Exception {
		if (this.getSymbol() == null) {
			throw new Exception();
		}
		if (this.getLastDividend() < 0.0) {
			throw new Exception();
		}
		if (this.getParValue() < 0.0) {
			throw new Exception();
		}
	}

}
