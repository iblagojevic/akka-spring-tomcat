package com.iblagojevic.actor;


import akka.actor.*;
import com.iblagojevic.akka.SpringExtension;
import com.iblagojevic.controller.TransactionPayload;
import com.iblagojevic.enums.SimpleMessageType;
import com.iblagojevic.model.Transaction;
import com.iblagojevic.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import scala.concurrent.duration.Duration;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Named("TransactionProcessor")
@Scope("prototype")
public class TransactionProcessor extends UntypedActor {

    @Autowired
    EntityService entityService;

    @Autowired
    ActorSystem system;

    /**
     * this actor should stop itself after some period of time
     * during which it is supposed to perform all processing operations
     */
    @Override
    public void preStart() throws Exception {
        context().setReceiveTimeout(Duration.create(3, TimeUnit.MINUTES));
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof TransactionPayload) {
            createTransaction(((TransactionPayload) message));
        } else if (message instanceof ReceiveTimeout) {
            context().stop(self());
        } else if (message instanceof SimpleMessageType && message == SimpleMessageType.TRANSFER_ACTOR_DONE) {
            sender().tell(PoisonPill.getInstance(), self());
        } else if (message instanceof String) {
            startErrorLogger((String)message, "Error happened while trying to send money to user, shutting down transferer actor");
            sender().tell(PoisonPill.getInstance(), self());
        } else {
            unhandled(message);
        }
    }

    /**
     * Message processing happens in this method:
     * transaction payload is transformed to domain object and tried to get persisted
     * if persistence succeeds, "transfer" actor is spawned to send money to user's account
     *
     */
    private void createTransaction(TransactionPayload transactionPayload) {
        try {
            Transaction transaction = entityService.createTransaction(transactionPayload);
            if (transaction != null) {
                ActorRef transferer = context().actorOf(SpringExtension.SpringExtProvider.get(system).props("Transferer"), "TRANSFER-FOR-".concat(transaction.getId().toString()));
                transferer.tell(transaction, self());
            }
        }
        catch (Exception e) {
            startErrorLogger(transactionPayload.getUserId(), e.getMessage() != null ? e.getMessage() : "Incorrect message format");
        }
    }

    private void startErrorLogger(String userId, String message) {
        ActorRef errorHandler = context().actorOf(SpringExtension.SpringExtProvider.get(system).props("ErrorHandler"), "ERROR-FOR-".concat(userId));
        Map<String, String> error = new HashMap<>();
        error.put("userId", userId);
        error.put("message", message);
        errorHandler.tell(error, self());
    }
}
