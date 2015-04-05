package com.iblagojevic.service;

import com.iblagojevic.controller.TransactionPayload;
import com.iblagojevic.model.*;
import com.iblagojevic.model.Currency;
import com.iblagojevic.model.Error;
import com.iblagojevic.util.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class EntityServiceImpl implements EntityService {

    EntityManager entityManager;


    public EntityManager getEntityManager() {
        return entityManager;
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Transaction getTransaction(Long id) {
        return entityManager.find(Transaction.class, id);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public Transaction createTransaction(TransactionPayload transactionPayload) {
        User user = getOrCreateUser(Long.parseLong(transactionPayload.getUserId()));
        Country country = getOrCreateCountry(transactionPayload.getOriginatingCountry());
        Currency currencyFrom = getOrCreateCurrency(transactionPayload.getCurrencyFrom());
        Currency currencyTo = getOrCreateCurrency(transactionPayload.getCurrencyTo());
        Date timePlaced = DateUtils.getDateFromString(transactionPayload.getTimePlaced());
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setOriginatingCountry(country);
        transaction.setCurrencyFrom(currencyFrom);
        transaction.setCurrencyTo(currencyTo);
        transaction.setTimePlaced(timePlaced.getTime());
        transaction.setAmountBuy(transactionPayload.getAmountBuy());
        transaction.setAmountSell(transactionPayload.getAmountSell());
        transaction.setRate(transactionPayload.getRate());
        return entityManager.merge(transaction);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public User getOrCreateUser(Long id) {
        User user = entityManager.find(User.class, id);
        if (user == null) {
            user = new User();
            user.setId(id);
            user = entityManager.merge(user);
        }
        return user;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public Country getOrCreateCountry(String id) {
        Country country = entityManager.find(Country.class, id);
        if (country == null) {
            country = new Country();
            country.setId(id);
            country.setName(id); // as we don't have, in some imaginary complete system names would be entered via some UI
            country = entityManager.merge(country);
        }
        return country;

    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public Currency getOrCreateCurrency(String id) {
        Currency currency = entityManager.find(Currency.class, id);
        if (currency == null) {
            currency = new Currency();
            currency.setId(id);
            currency.setName(id); // as we don't have, in some imaginary complete system names would be entered via some UI
            currency = entityManager.merge(currency);
        }
        return currency;
    }

    public boolean transferMoneyToUser(User user, double amount) {
        //.... do send money to user using User#bankAccountNo...
        return true; // if transfer succeeds, otherwise return false
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public void logError(Map<String, String> message) {
        User user = getOrCreateUser(Long.parseLong(message.get("userId")));
        Error error = new Error();
        error.setUser(user);
        error.setMessage(message.get("message"));
        error.setErrorTimestamp(new Date().getTime());
        entityManager.persist(error);
    }

    public List<Map<String, String>> getReportByCountry(Date from, Date to) {
        return entityManager
                .createQuery("select new map(c.name as name, count(t) as cnt) from Country c, Transaction t where t.timePlaced >= :from and t.timePlaced <= :to and c.id = t.originatingCountry.id group by c.name order by count(t) desc")
                .setParameter("from", from.getTime())
                .setParameter("to", to.getTime())
                .setMaxResults(10)
                .getResultList();
    }

    public List<Map<String, String>> getReportByCurrencyFrom(Date from, Date to) {
        return entityManager
                .createQuery("select new map(c.name as name, count(t) as cnt) from Currency c, Transaction t where t.timePlaced >= :from and t.timePlaced <= :to and c.id = t.currencyFrom.id group by c.name order by count(t) desc")
                .setParameter("from", from.getTime())
                .setParameter("to", to.getTime())
                .setMaxResults(10)
                .getResultList();
    }

    public List<Map<String, String>> getReportByCurrencyTo(Date from, Date to) {
        return entityManager
                .createQuery("select new map(c.name as name, count(t) as cnt) from Currency c, Transaction t where t.timePlaced >= :from and t.timePlaced <= :to and c.id = t.currencyTo.id group by c.name order by count(t) desc")
                .setParameter("from", from.getTime())
                .setParameter("to", to.getTime())
                .setMaxResults(10)
                .getResultList();
    }

    public List<Map<String, String>> getErrorReport(Date from, Date to) {
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        List<Error> errors = entityManager
                .createQuery("select e from Error e where e.errorTimestamp >= :from and e.errorTimestamp <= :to order by e.errorTimestamp")
                .setParameter("from", from.getTime())
                .setParameter("to", to.getTime())
                .getResultList();
        for (Error e : errors) {
            Map<String, String> entry = new HashMap<String, String>();
            entry.put("userId", e.getUser().getId().toString());
            entry.put("message", e.getMessage());
            entry.put("date", DateUtils.dateToString(new Date(e.getErrorTimestamp())));
            result.add(entry);
        }
        return result;
    }


}