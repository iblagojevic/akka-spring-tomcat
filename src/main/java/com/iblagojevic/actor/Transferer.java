package com.iblagojevic.actor;

import akka.actor.UntypedActor;
import com.iblagojevic.enums.SimpleMessageType;
import com.iblagojevic.model.Transaction;
import com.iblagojevic.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import javax.inject.Named;

@Named("Transferer")
@Scope("prototype")
public class Transferer extends UntypedActor {

    @Autowired
    EntityService entityService;

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Transaction) {
            transferAmountToUser((Transaction)message);
        } else {
            unhandled(message);
        }
    }

    /**
     * tries to move bought funds to user's account, informs parent actor on success of operation
     */
    private void transferAmountToUser(Transaction transaction) {
        // this code can be something that contacts 3rd party provider and sends converted money to user
        if (entityService.transferMoneyToUser(transaction.getUser(), transaction.getAmountBuy())) {
            context().parent().tell(SimpleMessageType.TRANSFER_ACTOR_DONE, self());
        }
        else {
            context().parent().tell(transaction.getUser().getId().toString(), self());
        }
    }
}
