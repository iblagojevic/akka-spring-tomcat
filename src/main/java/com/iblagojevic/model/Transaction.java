package com.iblagojevic.model;

import javax.persistence.*;
import java.io.Serializable;
import static javax.persistence.GenerationType.AUTO;

/**
 * simple POJO representing transaction made by <tt>User</tt>
 */

@Entity
@Table(name = "transaction")
public class Transaction implements Serializable {

    private static final long serialVersionUID = 5486186403849267680L;
    private Long id;
    private User user;
    private Currency currencyFrom;
    private Currency currencyTo;
    private long timePlaced;
    private Country originatingCountry;
    private double amountSell;
    private double amountBuy;
    private double rate;

    public Transaction() {}

    public Transaction(Long id, User user, Currency currencyFrom, Currency currencyTo, long timePlaced, Country originatingCountry, double amountSell, double amountBuy, double rate) {
        this.id = id;
        this.user = user;
        this.currencyFrom = currencyFrom;
        this.currencyTo = currencyTo;
        this.timePlaced = timePlaced;
        this.originatingCountry = originatingCountry;
        this.amountSell = amountSell;
        this.amountBuy = amountBuy;
        this.rate = rate;
    }


    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "time_placed", nullable = false)
    public long getTimePlaced() {
        return timePlaced;
    }

    public void setTimePlaced(long timePlaced) {
        this.timePlaced = timePlaced;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    public Country getOriginatingCountry() {
        return originatingCountry;
    }

    public void setOriginatingCountry(Country originatingCountry) {
        this.originatingCountry = originatingCountry;
    }

    @Column(name = "amount_sell", nullable = false)
    public double getAmountSell() {
        return amountSell;
    }

    public void setAmountSell(double amountSell) {
        this.amountSell = amountSell;
    }

    @Column(name = "amount_buy", nullable = false)
    public double getAmountBuy() {
        return amountBuy;
    }

    public void setAmountBuy(double amountBuy) {
        this.amountBuy = amountBuy;
    }

    @Column(name = "rate", nullable = false)
    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_from_id", nullable = false)
    public Currency getCurrencyFrom() {
        return currencyFrom;
    }

    public void setCurrencyFrom(Currency currencyFrom) {
        this.currencyFrom = currencyFrom;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_to_id", nullable = false)
    public Currency getCurrencyTo() {
        return currencyTo;
    }

    public void setCurrencyTo(Currency currencyTo) {
        this.currencyTo = currencyTo;
    }


}