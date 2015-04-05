package com.iblagojevic.actor;

import akka.actor.UntypedActor;
import com.iblagojevic.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import javax.inject.Named;
import java.util.Map;

@Named("ErrorHandler")
@Scope("prototype")
public class ErrorHandler extends UntypedActor {

    @Autowired
    EntityService entityService;

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Map) {
            logError((Map)message);
        } else {
            unhandled(message);
        }
    }

    private void logError(Map<String, String> message) {
        entityService.logError(message);
        context().stop(self());
    }
}
