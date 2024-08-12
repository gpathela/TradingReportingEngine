package com.vanguard.demo.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vanguard.demo.model.TradeFilterCondition;
import com.vanguard.demo.model.TradeFilterResponse;
import com.vanguard.demo.model.TradeModel;


public interface MainService {

    String loadData(final String path);
    Page<TradeModel> findAllTrades(Pageable pageable);
    TradeFilterResponse findByDefaultConditions();
    TradeFilterResponse findByDynamicConditions(List<TradeFilterCondition> conditions);
}
