package com.simplestockmarket.server;

import java.util.Date;

import org.apache.log4j.Logger;

import com.simplestockmarket.enums.TradeType;

public class TradeTransaction {

	private Date timeStamp = null;
	private Stock stock = null;
	private TradeType tradeIndicator = TradeType.BUY;
	private int quantity = 0;
	private double price = 0.0;
	private Logger logger = Logger.getLogger(TradeTransaction.class);

	public TradeTransaction(Date timeStamp, Stock stock, TradeType tradeIndicator, int quantity, double price) {
		this.timeStamp = timeStamp;
		this.stock = stock;
		this.tradeIndicator = tradeIndicator;
		this.quantity = quantity;
		this.price = price;
		try{
		validateTradeTransaction();
		}catch (Exception exception) {
			logger.error("Trade transaction failed to get initialized!");
		}
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Stock getStock() {
		return stock;
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}

	public TradeType getTradeIndicator() {
		return tradeIndicator;
	}

	public void setTradeIndicator(TradeType tradeIndicator) {
		this.tradeIndicator = tradeIndicator;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	private void validateTradeTransaction() throws Exception {
		Date date = this.getTimeStamp();
		Stock stock = this.getStock();
		TradeType tradeType = this.getTradeIndicator();
		int qtty = this.getQuantity();
		double price =  this.getPrice();
		if(date == null || stock == null){
			throw new Exception();
		}
		if(!tradeType.equals(TradeType.BUY) && !tradeType.equals(TradeType.SELL)){
			throw new Exception();
		}
		if(qtty<=0.0){
			throw new Exception();
		}
		if(price<0.0){
			throw new Exception();
		}
	}
	
}
