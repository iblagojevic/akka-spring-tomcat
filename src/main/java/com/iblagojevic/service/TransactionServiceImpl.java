package com.iblagojevic.service;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.iblagojevic.akka.SpringExtension;
import com.iblagojevic.controller.TransactionPayload;
import com.iblagojevic.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    ActorSystem actorSystem;

    /**
     * starts supervising actor for given message
     */
    public void startProcessingActor(TransactionPayload transactionPayload) {
        ActorRef processor = actorSystem.actorOf(SpringExtension.SpringExtProvider.get(actorSystem).props("TransactionProcessor"), "TRANSACTION-".concat(UUID.randomUUID().toString()));
        processor.tell(transactionPayload, ActorRef.noSender());
    }


    /**
     * helper method to produce Date boundaries
     * if no date(s) filter requested, boundaries default to
     * start of time for lower and current date for upper;
     *
     * boundaries are normalized to start of day for lower and end of day for upper
     */
    public Map<String, Date> fromToDates(String from, String to) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Map<String, Date> result = new HashMap<>();
        try {
            result.put("from", DateUtils.getStartOfDay(sdf.parse(from)));
        }
        catch (Exception e) {
            result.put("from", new Date(0));
        }
        try {
            result.put("to", DateUtils.getEndOfDay(sdf.parse(to)));
        }
        catch (Exception e) {
            result.put("to", DateUtils.getEndOfDay(new Date()));
        }
        return result;
    }
}
