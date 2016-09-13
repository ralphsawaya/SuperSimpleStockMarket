package com.simplestockmarket.server;

public class CommonStock extends Stock {

	public CommonStock(String symbol, Double lastDividend, Double parValue) {
		super(symbol, lastDividend, parValue);
	}

	public Double CalculateDividendYield(Double price) {
		double dividendYield = -1.0;
		if (price > 0) {
			dividendYield = this.getLastDividend() / price;
		}
		return dividendYield;
	}

}
