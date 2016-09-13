package com.simplestockmarket.server;

public class PreferredStock extends Stock {
	private Double fixedDividend = 0.0;

	public PreferredStock(String symbol, Double lastDividend, Double parValue, Double fixedDividend) {
		super(symbol, lastDividend, parValue);
		this.fixedDividend = fixedDividend;
		try {
			this.validateStock();
		} catch (Exception e) {
			logger.error("Stock failed to get initialized!");
		}
	}

	public Double getFixedDividend() {
		return fixedDividend;
	}

	public void setFixedDividend(Double fixedDividend) {
		this.fixedDividend = fixedDividend;
	}

	protected void validate() throws Exception {
		super.validateStock();
		if (this.getFixedDividend() < 0.0) {
			throw new Exception();
		}
	}

	public Double CalculateDividendYield(Double price) {
		double dividendYield = -1.0;
		if (price > 0) {
			dividendYield = this.getFixedDividend() * this.getParValue() / price;
		}
		return dividendYield;
	}
}
