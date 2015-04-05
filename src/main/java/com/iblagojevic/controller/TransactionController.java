package com.iblagojevic.controller;

import com.iblagojevic.service.EntityService;
import com.iblagojevic.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/transaction")
public class TransactionController {

    private static final String STATUS_ACCEPTED = "ACCEPTED";
    private static final String STATUS_REJECTED = "REJECTED";

    @Autowired
    EntityService entityService;

    @Autowired
    TransactionService transactionService;

    /**
     * Endpoint consumes json and passes DTO payload to Akka actor
     */
    @RequestMapping(value="/submit", method=RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public  Map<String, String> processTransaction(@RequestBody TransactionPayload transactionPayload) {
        Map<String, String> response = new HashMap<>();
        if (transactionPayload.isPayloadValid()) {
            transactionService.startProcessingActor(transactionPayload);
            response.put("status", STATUS_ACCEPTED);
            response.put("message", "Message submitted, processing passed to actor.");
        }
        else {
            response.put("status", STATUS_REJECTED);
            response.put("message", "Could not transform submitted message, payload seems to be incorrect.");
        }
        return response;
    }

    /**
     * produces output for frontend reporting
     */
    @RequestMapping(value="/byCountry", method=RequestMethod.GET)
    @ResponseBody
    public List<Map<String, String>> reportByCountry(@RequestParam(value = "from", required = false) String from, @RequestParam(value = "to", required = false) String to) {
        Map<String, Date> fromToDates = transactionService.fromToDates(from, to);
        List<Map<String, String>> report = entityService.getReportByCountry(fromToDates.get("from"), fromToDates.get("to"));
        return report;
    }

    /**
     * produces output for frontend reporting
     */
    @RequestMapping(value="/byCurrencyFrom", method=RequestMethod.GET)
    @ResponseBody
    public List<Map<String, String>> reportByCurrencyFrom(@RequestParam(value = "from", required = false) String from, @RequestParam(value = "to", required = false) String to) {
        Map<String, Date> fromToDates = transactionService.fromToDates(from, to);
        List<Map<String, String>> report = entityService.getReportByCurrencyFrom(fromToDates.get("from"), fromToDates.get("to"));
        return report;
    }

    /**
     * produces output for frontend reporting
     */
    @RequestMapping(value="/byCurrencyTo", method=RequestMethod.GET)
    @ResponseBody
    public List<Map<String, String>> reportByCurrencyTo(@RequestParam(value = "from", required = false) String from, @RequestParam(value = "to", required = false) String to) {
        Map<String, Date> fromToDates = transactionService.fromToDates(from, to);
        List<Map<String, String>> report = entityService.getReportByCurrencyTo(fromToDates.get("from"), fromToDates.get("to"));
        return report;
    }

    /**
     * produces output for frontend reporting
     */
    @RequestMapping(value="/errors", method=RequestMethod.GET)
    @ResponseBody
    public List<Map<String, String>> errorReport(@RequestParam(value = "from", required = false) String from, @RequestParam(value = "to", required = false) String to) {
        Map<String, Date> fromToDates = transactionService.fromToDates(from, to);
        List<Map<String, String>> report = entityService.getErrorReport(fromToDates.get("from"), fromToDates.get("to"));
        return report;
    }



}
