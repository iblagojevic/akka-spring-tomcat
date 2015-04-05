package com.iblagojevic.service;


import com.iblagojevic.controller.TransactionPayload;
import com.iblagojevic.model.Country;
import com.iblagojevic.model.Currency;
import com.iblagojevic.model.Transaction;
import com.iblagojevic.model.User;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * here we have necessary methods to get/create related entities
 * in more complex ("real-life") implementation, each entity would have own service class
 */
public interface EntityService {

    Transaction getTransaction(Long id);

    Transaction createTransaction(TransactionPayload transactionPayload);

    User getOrCreateUser(Long userId);

    Country getOrCreateCountry(String id);

    Currency getOrCreateCurrency(String id);

    boolean transferMoneyToUser(User u, double amount);

    void logError(Map<String, String> message);

    List getReportByCountry(Date from, Date to);

    List getReportByCurrencyFrom(Date from, Date to);

    List getReportByCurrencyTo(Date from, Date to);

    List getErrorReport(Date from, Date to);

}
