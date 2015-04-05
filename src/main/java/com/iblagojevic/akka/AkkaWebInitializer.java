package com.iblagojevic.akka;

import akka.actor.ActorSystem;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import scala.concurrent.duration.Duration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.concurrent.TimeUnit;

public class AkkaWebInitializer implements ServletContextListener {

    private static final Logger log = Logger.getLogger(AkkaWebInitializer.class);

    @Autowired
    private ActorSystem system;

    @Autowired
    private ApplicationContext applicationContext;

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        SpringExtension.SpringExtProvider.get(system).initialize(applicationContext);
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        if (system != null) {
            log.info("Shutting down ActorSystem during web application context destruction.");
            system.shutdown();
            system.awaitTermination(Duration.create(15, TimeUnit.SECONDS));
        }
        else {
            log.warn("No actor system loaded, shutting down.");
        }
    }
}
