package com.simplestockmarket.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.simplestockmarket.server.CommonStock;
import com.simplestockmarket.server.PreferredStock;

public class StockTest {

	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void CalculateDividendYieldTest() {
		System.out.println("START of CalculateDividendYieldTest...");
		CommonStock stockALE = new CommonStock("ALE", 23.0, 60.0);
		PreferredStock stockGIN = new PreferredStock("GIN", 8.0, 0.2, 100.0);
		// Test dividend for Common
		Double dividendALE = stockALE.CalculateDividendYield(50.0);
		Double expectedDividendALE = stockALE.getLastDividend() / 50.0;
		assertEquals(expectedDividendALE, dividendALE);

		// Test dividend for Preferred
		Double dividendGIN = stockGIN.CalculateDividendYield(1.0);
		Double expectedDividendGIN = stockGIN.getFixedDividend() * stockGIN.getParValue() / 1.0;
		assertEquals(expectedDividendGIN, dividendGIN);

		// Test input price as negative
		dividendALE = stockALE.CalculateDividendYield(-5.0);
		expectedDividendALE = -1.0;
		assertEquals(expectedDividendALE, dividendALE);

		// Test input price as zero
		dividendALE = stockALE.CalculateDividendYield(0.0);
		expectedDividendALE = -1.0;
		assertEquals(expectedDividendALE, dividendALE);
		System.out.println("END of CalculateDividendYieldTest with ok");
	}

	@Test
	public void testPERatio() {
		System.out.println("START of testPERatio...");
		CommonStock stockALE = new CommonStock("ALE", 23.0, 60.0);
		// Test regular case
		Double peRatioALE = stockALE.calculatePeRatio(1.0);
		Double expectedPeRatioALE = 1.0 / stockALE.getLastDividend();
		assertEquals(expectedPeRatioALE, peRatioALE);

		// Test input price as negative
		Double peRatioGIN = stockALE.calculatePeRatio(-1.0);
		Double expectedPeRatioGIN = -1.0;
		assertEquals(expectedPeRatioGIN, peRatioGIN);

		// Test when divident is zero
		PreferredStock stockTEA = new PreferredStock("TEA", 0.0, 0.0, 100.0);
		Double peRatioTEA = stockTEA.calculatePeRatio(1.0);
		Double expectedPeRatioTEA = -1.0;
		assertEquals(expectedPeRatioTEA, peRatioTEA);
		System.out.println("END of testPERatio with ok");
	}

}
