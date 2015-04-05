package com.iblagojevic.controller;

import java.io.Serializable;

public class TransactionPayload implements Serializable {

    private static final long serialVersionUID = -5814688096292871326L;
    private String userId;
    private String currencyFrom;
    private String currencyTo;
    private String timePlaced;
    private String originatingCountry;
    private Double amountSell;
    private Double amountBuy;
    private Double rate;

    public TransactionPayload() {
    }

    public TransactionPayload(String userId, String currencyFrom, String currencyTo, String timePlaced, String originatingCountry, double amountSell, double amountBuy, double rate) {
        this.userId = userId;
        this.currencyFrom = currencyFrom;
        this.currencyTo = currencyTo;
        this.timePlaced = timePlaced;
        this.originatingCountry = originatingCountry;
        this.amountSell = amountSell;
        this.amountBuy = amountBuy;
        this.rate = rate;
    }

    // simple payload validator, could be extended to check date format, number format for doubles, etc...
    // for this test purposes will remain simple
    public boolean isPayloadValid() {
        return userId != null &&
                currencyFrom != null &&
                currencyTo != null &&
                timePlaced != null &&
                originatingCountry != null &&
                amountBuy != null &&
                amountSell != null &&
                rate != null;
    }

    public double getAmountBuy() {
        return amountBuy;
    }

    public void setAmountBuy(double amountBuy) {
        this.amountBuy = amountBuy;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCurrencyFrom() {
        return currencyFrom;
    }

    public void setCurrencyFrom(String currencyFrom) {
        this.currencyFrom = currencyFrom;
    }

    public String getCurrencyTo() {
        return currencyTo;
    }

    public void setCurrencyTo(String currencyTo) {
        this.currencyTo = currencyTo;
    }

    public String getTimePlaced() {
        return timePlaced;
    }

    public void setTimePlaced(String timePlaced) {
        this.timePlaced = timePlaced;
    }

    public String getOriginatingCountry() {
        return originatingCountry;
    }

    public void setOriginatingCountry(String originatingCountry) {
        this.originatingCountry = originatingCountry;
    }

    public double getAmountSell() {
        return amountSell;
    }

    public void setAmountSell(double amountSell) {
        this.amountSell = amountSell;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
