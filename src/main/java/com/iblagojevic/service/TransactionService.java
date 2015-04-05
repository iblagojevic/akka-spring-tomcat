package com.iblagojevic.service;

import com.iblagojevic.controller.TransactionPayload;

import java.util.Date;
import java.util.Map;

/**
 * defines methods used for message consumption and transactions reporting
 */
public interface TransactionService {
    void startProcessingActor(TransactionPayload transactionPayload);
    Map<String, Date> fromToDates(String from, String to);
}
