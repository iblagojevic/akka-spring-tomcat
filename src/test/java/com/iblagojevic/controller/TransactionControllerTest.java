package com.iblagojevic.controller;

import com.iblagojevic.service.EntityService;
import com.iblagojevic.service.TransactionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

;

@RunWith(PowerMockRunner.class)
public class TransactionControllerTest {

    private MockMvc mockMvc;

    @Mock
    EntityService mockEntityService;

    @Mock
    TransactionService mockTransactionService;

    @InjectMocks
    private TransactionController transactionController = new TransactionController();


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }


    @Test
    public void reportByCriteria_shouldReturnJSONArray() throws Exception {
        List<Map<String, String>> result = new ArrayList<>();
        Map<String, String> entry1 = new HashMap<>();
        entry1.put("name", "name 1");
        entry1.put("cnt", "10");
        Map<String, String> entry2 = new HashMap<>();
        entry2.put("name", "name 2");
        entry2.put("cnt", "20");
        result.add(entry1);
        result.add(entry2);
        Map<String, Date> fromToDates = new HashMap<>();
        fromToDates.put("from", new Date(0));
        fromToDates.put("to", new Date());

        when(mockTransactionService.fromToDates("", "")).thenReturn(fromToDates);
        when(mockEntityService.getReportByCountry(org.mockito.Mockito.isA(Date.class), org.mockito.Mockito.isA(Date.class))).thenReturn(result);

        mockMvc.perform(get("/transaction/byCountry"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("name 1")))
                .andExpect(jsonPath("$[0].cnt", is("10")))
                .andExpect(jsonPath("$[1].name", is("name 2")))
                .andExpect(jsonPath("$[1].cnt", is("20")));
    }

    @Test
    public void submit_badPayload() throws Exception {

        mockMvc.perform(post("/transaction/submit")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":\"23\"}".getBytes()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", equalTo("REJECTED")));
    }

    @Test
    public void submit_correctPayload() throws Exception {

        TransactionPayload tp = new TransactionPayload();
        doNothing().when(mockTransactionService).startProcessingActor(tp);
        mockMvc.perform(post("/transaction/submit")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\": \"134256\", \"currencyFrom\": \"EUR\", \"currencyTo\": \"GBP\", \"amountSell\": 1000, \"amountBuy\": 747.10, \"rate\": 0.7471, \"timePlaced\" : \"24-JAN-15 10:27:44\", \"originatingCountry\" : \"FR\"}".getBytes()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", equalTo("ACCEPTED")));
    }
}
