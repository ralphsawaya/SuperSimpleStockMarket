package com.simplestockmarket.test;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import com.simplestockmarket.enums.TradeType;
import com.simplestockmarket.server.CommonStock;
import com.simplestockmarket.server.PreferredStock;
import com.simplestockmarket.server.StockManagementCentre;
import com.simplestockmarket.server.TradeTransaction;

public class StockManagementCentreTest {

	private Logger logger = Logger.getLogger(StockManagementCentreTest.class);
	private StockManagementCentre smc = StockManagementCentre.getInstance();
	TradeTransaction tx1;
	TradeTransaction tx2;
	TradeTransaction tx3;
	TradeTransaction tx4;
	TradeTransaction tx5;
	List<TradeTransaction> tradeTransactionsList;

	private void recordSomeTrades(int tradeListSetNumber) {
		CommonStock stockALE = new CommonStock("ALE", 23.0, 60.0);
		CommonStock stockPOP = new CommonStock("POP", 8.0, 100.0);
		CommonStock stockTEA = new CommonStock("TEA", 0.0, 100.0);
		PreferredStock stockGIN = new PreferredStock("GIN", 8.0, 0.02, 100.0);
		CommonStock stockJOE = new CommonStock("JOE", 13.0, 250.0);
		/*
		 * I provide here four different sets of trade transactions to be used for testing
		 */
		if (tradeListSetNumber == 1) {
			// regular case
			this.tx1 = new TradeTransaction(new Date(), stockALE, TradeType.SELL, 6, 12.0);
			this.tx2 = new TradeTransaction(new Date(), stockPOP, TradeType.BUY, 12, 5.0);
			this.tx3 = new TradeTransaction(new Date(), stockTEA, TradeType.SELL, 10, 8.0);
			this.tx4 = new TradeTransaction(new Date(), stockGIN, TradeType.SELL, 16, 10.0);
			this.tx5 = new TradeTransaction(new Date(), stockJOE, TradeType.SELL, 19, 15.0);
		} else if (tradeListSetNumber == 2) {
			// test with one of the stocks having a zero price
			this.tx1 = new TradeTransaction(new Date(), stockALE, TradeType.SELL, 6, 0.0);
			this.tx2 = new TradeTransaction(new Date(), stockPOP, TradeType.BUY, 12, 5.0);
			this.tx3 = new TradeTransaction(new Date(), stockTEA, TradeType.SELL, 11, 8.0);
			this.tx4 = new TradeTransaction(new Date(), stockGIN, TradeType.SELL, 10, 8.0);
			this.tx5 = new TradeTransaction(new Date(), stockJOE, TradeType.SELL, 4, 8.0);
		} else if (tradeListSetNumber == 3) {
			// case used to test the VolumeWeightedStockPrice, we recorded 3
			// trades of POP within the 15 min
			Date rightNow = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(rightNow);
			cal.add(Calendar.MINUTE, -14);
			Date fourteenMinBefore = cal.getTime();
			this.tx1 = new TradeTransaction(new Date(), stockALE, TradeType.SELL, 6, 10.0);
			this.tx2 = new TradeTransaction(new Date(), stockPOP, TradeType.BUY, 4, 5.0);
			this.tx3 = new TradeTransaction(new Date(), stockTEA, TradeType.SELL, 5, 8.0);
			this.tx4 = new TradeTransaction(new Date(), stockPOP, TradeType.BUY, 4, 3.0);
			this.tx5 = new TradeTransaction(fourteenMinBefore, stockPOP, TradeType.BUY, 8, 7.0);
		} else if (tradeListSetNumber == 4) {
			// case used to test the VolumeWeightedStockPrice, we recorded 3
			// traded of POP
			Date rightNow = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(rightNow);
			cal.add(Calendar.MINUTE, -16);
			Date sixteenMinBefore = cal.getTime();

			this.tx1 = new TradeTransaction(new Date(), stockALE, TradeType.SELL, 6, 10.0);
			this.tx2 = new TradeTransaction(new Date(), stockPOP, TradeType.BUY, 4, 5.0);
			this.tx3 = new TradeTransaction(new Date(), stockTEA, TradeType.SELL, 5, 8.0);
			this.tx4 = new TradeTransaction(new Date(), stockPOP, TradeType.BUY, 4, 3.0);
			this.tx5 = new TradeTransaction(sixteenMinBefore, stockPOP, TradeType.BUY, 8, 7.0);
		}

		smc.recordTradeTransaction(tx1);
		smc.recordTradeTransaction(tx2);
		smc.recordTradeTransaction(tx3);
		smc.recordTradeTransaction(tx4);
		smc.recordTradeTransaction(tx5);
	}

	@Test
	public void recordTradeTransactionTest() {
		logger.info("Start of recordTradeTransactionTest...");
		List<TradeTransaction> tradeTransactionsList = smc.geTtradeTransactionsList();
		// Test that trade transaction list is initially empty
		assertEquals(tradeTransactionsList.size(), 0);

		recordSomeTrades(1);
		this.tradeTransactionsList = smc.geTtradeTransactionsList();

		// test first the number of records in the transaction list
		assertEquals(tradeTransactionsList.size(), 5);
		// test that tx1 got recorded correctly
		assertEquals(tradeTransactionsList.get(0).getTimeStamp(), tx1.getTimeStamp());
		assertEquals(tradeTransactionsList.get(0).getTradeIndicator(), tx1.getTradeIndicator());
		assertEquals(tradeTransactionsList.get(0).getQuantity(), tx1.getQuantity());
		assertEquals(tradeTransactionsList.get(0).getTradeIndicator(), tx1.getTradeIndicator());

		// test that tx2 got recorded correctly
		assertEquals(tradeTransactionsList.get(1).getTimeStamp(), tx2.getTimeStamp());
		assertEquals(tradeTransactionsList.get(1).getTradeIndicator(), tx2.getTradeIndicator());
		assertEquals(tradeTransactionsList.get(1).getQuantity(), tx2.getQuantity());
		assertEquals(tradeTransactionsList.get(1).getTradeIndicator(), tx2.getTradeIndicator());

		// test that tx3 got recorded correctly
		assertEquals(tradeTransactionsList.get(2).getTimeStamp(), tx3.getTimeStamp());
		assertEquals(tradeTransactionsList.get(2).getTradeIndicator(), tx3.getTradeIndicator());
		assertEquals(tradeTransactionsList.get(2).getQuantity(), tx3.getQuantity());
		assertEquals(tradeTransactionsList.get(2).getTradeIndicator(), tx3.getTradeIndicator());

		smc.resetTtradeTransactionsList();
		logger.info("End of recordTradeTransactionTest with OK");
	}

	@Test
	public void isStockManagementCentreSingleton() {
		logger.info("Start  isStockManagementCentreSingleton...");
		StockManagementCentre StockManagementCentreA = StockManagementCentre.getInstance();
		StockManagementCentre StockManagementCentreB = StockManagementCentre.getInstance();
		Assert.assertNotNull(StockManagementCentreA);
		Assert.assertNotNull(StockManagementCentreB);
		Assert.assertTrue(StockManagementCentreA.equals(StockManagementCentreB));
		logger.info("Finish isStockManagementCentreSingleton with OK");
	}

	@Test
	public void calculateGBCEAllShareIndexTest() {
		try {
			logger.info("Start calculateGBCEAllShareIndexTest...");
			double GBCEAllShareIndex;
			// test regular case
			recordSomeTrades(1);
			GBCEAllShareIndex = smc.calculateGBCEAllShareIndex();
			assertEquals(9.36411, GBCEAllShareIndex, 0.00001);
			smc.resetTtradeTransactionsList();
			// test with one of the stocks having a zero price
			recordSomeTrades(2);
			GBCEAllShareIndex = smc.calculateGBCEAllShareIndex();
			assertEquals(0.0, GBCEAllShareIndex, 0.0);
			smc.resetTtradeTransactionsList();

		} catch (Exception exception) {
			logger.error(exception);
			Assert.assertTrue(false);
		}
		logger.info("Finish calculateGBCEAllShareIndexTest with OK");

	}

	@Test
	public void CalculateVolumeWeightedStockPriceTest() {
		logger.info("Start CalculateVolumeWeightedStockPriceTest...");
		/*
		 * case where all 3 POP trade records are within fifteen minutes
		 */
		recordSomeTrades(3);
		String StockSymbol = "POP";
		try {
			double c = smc.CalculateVolumeWeightedStockPrice(StockSymbol);
			assertEquals(5.5, c, 0.0001);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		smc.resetTtradeTransactionsList();
		/*
		 * case where we have one of the three POP trade records more than
		 * fifteen minutes ago
		 */
		recordSomeTrades(4);
		StockSymbol = "POP";
		try {
			double c = smc.CalculateVolumeWeightedStockPrice(StockSymbol);
			assertEquals(4, c, 0.0001);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		smc.resetTtradeTransactionsList();
		/*
		 * we try as well the one TEA trade record in the fourth set of
		 * transaction
		 */
		recordSomeTrades(4);
		StockSymbol = "TEA";
		try {
			double c = smc.CalculateVolumeWeightedStockPrice(StockSymbol);
			assertEquals(8, c, 0.0001);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		smc.resetTtradeTransactionsList();
		logger.info("Finish CalculateVolumeWeightedStockPriceTest with OK");
	}

}
