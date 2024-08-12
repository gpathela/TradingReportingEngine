package com.vanguard.demo.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vanguard.demo.model.TradeFilter;
import com.vanguard.demo.model.TradeFilterResponse;
import com.vanguard.demo.model.TradeModel;
import com.vanguard.demo.service.MainService;
import com.vanguard.demo.util.TestDataUtil;

@WebMvcTest(TradeController.class)
public class TradeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MainService mainService;
    @Mock
    PagedResourcesAssembler<TradeModel> pagedResourcesAssembler;
    private Page<TradeModel> trades;
    private PagedModel<EntityModel<TradeModel>> pagedModel;
    private TradeFilterResponse tradeFilterResponse;

    @BeforeEach
     public void setUp() {
        MockitoAnnotations.openMocks(this);
        TradeModel tradeModel = new TradeModel();
        trades = new PageImpl<>(Collections.singletonList(tradeModel), PageRequest.of(0, 10), 1);
        pagedModel = PagedModel.of(Collections.singletonList(EntityModel.of(tradeModel)), new PagedModel.PageMetadata(10, 0, 1));
        tradeFilterResponse = new TradeFilterResponse();
    }


    @Test
    public void testGetAllTrades() throws Exception {
        when(mainService.findAllTrades(any())).thenReturn(trades);
        when(pagedResourcesAssembler.toModel(any())).thenReturn(pagedModel);
        mockMvc.perform(get("/api/trades")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testDefaultSearch() throws Exception {
        tradeFilterResponse.setTrades(Collections.singletonList(TestDataUtil.tradeModel));
        when(mainService.findByDefaultConditions()).thenReturn(tradeFilterResponse);
        mockMvc.perform(get("/api/trades/defaultsearch")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("trades").exists());
    }

    @Test
    public void testSearchWithoutBody() throws Exception {
        mockMvc.perform(post("/api/trades/search")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testSearchNoCondition() throws Exception {
        TradeFilter tradeFilter = new TradeFilter(Collections.emptyList());
        String jsonBody = objectMapper.writeValueAsString(tradeFilter); 
        mockMvc.perform(post("/api/trades/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testSearch() throws Exception {
        TradeFilter tradeFilter = new TradeFilter(List.of(TestDataUtil.tradeFilterCondition));
        String jsonBody = objectMapper.writeValueAsString(tradeFilter); // Convert to JSON

        mockMvc.perform(post("/api/trades/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andExpect(status().isOk());
    }
}