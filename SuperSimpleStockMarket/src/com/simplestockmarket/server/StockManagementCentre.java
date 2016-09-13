package com.simplestockmarket.server;

import java.util.List;
import org.apache.commons.math3.stat.StatUtils;
import java.util.Iterator;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.apache.log4j.Logger;

public class StockManagementCentre {

	private static StockManagementCentre instance = null;
	private HashMap<String, Stock> stockList = new HashMap<String, Stock>();
	private List<TradeTransaction> tradeTransactionsList;
	private final int minutesDeduction = -15;
	private Logger logger = Logger.getLogger(StockManagementCentre.class);

	private StockManagementCentre() {
		loadStockList();
		this.tradeTransactionsList = new ArrayList<TradeTransaction>();

	}

	// Singleton class
	public static synchronized StockManagementCentre getInstance() {
		if (instance == null)
			instance = new StockManagementCentre();
		return instance;
	}

	private void loadStockList() {
		try {
			stockList.put("TEA", new CommonStock("TEA", 0.0, 100.0));
			stockList.put("POP", new CommonStock("POP", 8.0, 100.0));
			stockList.put("ALE", new CommonStock("ALE", 23.0, 60.0));
			stockList.put("GIN", new PreferredStock("GIN", 8.0, 0.2, 100.0));
			stockList.put("JOE", new CommonStock("JOE", 13.0, 250.0));
		} catch (Exception exception) {
			logger.error("An error has occurred while loading the stock list");
			exception.printStackTrace();
		}
	}

	public HashMap<String, Stock> getStockList() {
		return stockList;
	}

	public void recordTradeTransaction(TradeTransaction tradeTransaction) {
		try {

			// update the latest traded price for the stock in question before
			// recording the trade
			Stock stock = getStockBySymbol(tradeTransaction.getStock().getSymbol());
			stockList.get(stock.getSymbol()).setLastestTradedPrice(tradeTransaction.getPrice());
			tradeTransactionsList.add(tradeTransaction);
		} catch (Exception exception) {
			logger.error("An error has occurred while trading stock " + tradeTransaction.getStock().getSymbol()
					+ " Timestamp:" + tradeTransaction.getTimeStamp());
			exception.printStackTrace();
		}
	}

	public List<TradeTransaction> geTtradeTransactionsList() {
		return tradeTransactionsList;
	}

	public void resetTtradeTransactionsList() {
		this.tradeTransactionsList = new ArrayList<TradeTransaction>();
	}

	public void resetStockList() {
		this.stockList = new HashMap<String, Stock>();
	}

	public void setStockList(HashMap<String, Stock> stockList) {
		this.stockList = stockList;
	}

	public double CalculateVolumeWeightedStockPrice(String stockname) throws Exception {
		double summationTop = 0.0;
		double summationBottom = 0.0;
		double result = 0.0;
		// Get Trades filtered by stock and time
		List<TradeTransaction> filteredTrades = getFilteredTrades(stockname);

		if (!filteredTrades.isEmpty()) {
			// sum the upper part
			Iterator<TradeTransaction> itrTop = filteredTrades.iterator();
			TradeTransaction currentTrade;
			while (itrTop.hasNext()) {
				currentTrade = itrTop.next();
				// Summation of Trade Price * Share Qty
				summationTop = summationTop + (currentTrade.getPrice() * currentTrade.getQuantity());
			}

			// sum the bottom part
			Iterator<TradeTransaction> itrBottom = filteredTrades.iterator();
			while (itrBottom.hasNext()) {
				currentTrade = itrBottom.next();
				// Summation of Share Qty
				summationBottom = summationBottom + currentTrade.getQuantity();
			}
			result = summationTop / summationBottom;
		} else {
			result = 0.0;
		}
		return result;

	}

	private List<TradeTransaction> getFilteredTrades(String stockname) {
		Date rightNow = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(rightNow);
		cal.add(Calendar.MINUTE, minutesDeduction);
		Date fifteenMinBefore = cal.getTime();

		// the list that contains filtered content
		List<TradeTransaction> filteredTrades = new ArrayList<TradeTransaction>();
		for (TradeTransaction tradeTx : tradeTransactionsList) {
			// check first the symbol
			if (tradeTx.getStock().getSymbol().equalsIgnoreCase(stockname)) {
				// check now the date
				if (isDateInBetweenOrEqual(fifteenMinBefore, rightNow, tradeTx.getTimeStamp())) {
					filteredTrades.add(tradeTx);
				}
			}
		}
		return filteredTrades;
	}

	public double calculateGBCEAllShareIndex() throws Exception {
		double allShareIndex = 0.0;

		// Calculate stock price for all stock in the system
		HashMap<String, Stock> stockList = this.getStockList();
		ArrayList<Double> stockPrices = new ArrayList<Double>();
		for (Stock stock : stockList.values()) {
			double stockPrice = stock.getLastestTradedPrice();
			if (stockPrice > 0) {
				stockPrices.add(stockPrice);
			} else {
				return 0;
			}
		}
		if (stockPrices.size() >= 1) {
			double[] stockPricesArray = new double[stockPrices.size()];

			for (int i = 0; i <= (stockPrices.size() - 1); i++) {
				stockPricesArray[i] = stockPrices.get(i).doubleValue();
			}
			// Calculates the GBCE All Share Index
			allShareIndex = StatUtils.geometricMean(stockPricesArray);
		}
		return allShareIndex;
	}

	private boolean isDateInBetweenOrEqual(Date min, Date max, Date d) {
		boolean b = ((d.after(min) && d.before(max)) || (d.equals(min) || d.equals(max)));
		return b;
	}

	public Stock getStockBySymbol(String stockSymbol) {
		Stock stock = null;
		try {
			if (stockSymbol != null) {
				stock = stockList.get(stockSymbol);
			}
		} catch (Exception exception) {
			logger.error("An error has occurred recovering the stock object for the stock symbol: " + stockSymbol + ".",
					exception);
		}
		return stock;
	}

}