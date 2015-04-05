package com.iblagojevic.service;

import com.iblagojevic.util.DateUtils;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


public class TransactionServiceTest {

    TransactionService transactionService = new TransactionServiceImpl();

    @Test
    public void fromToDate_emptyArgs() {
        Map<String, Date> result = transactionService.fromToDates("", "");
        assert result.get("from").equals(new Date(0));
        assert result.get("to").equals(DateUtils.getEndOfDay(new Date()));
    }

    @Test
    public void fromToDate_givenArgsSameDateNormalizedToStartAndEnd() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String from = sdf.format(new Date());
        String to = from;
        Map<String, Date> result = transactionService.fromToDates(from, to);
        assert result.get("from").equals(DateUtils.getStartOfDay(new Date()));
        assert result.get("to").equals(DateUtils.getEndOfDay(new Date()));
    }
}
