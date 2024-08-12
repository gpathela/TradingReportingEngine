package com.vanguard.demo.repository;

import java.util.List;

import com.vanguard.demo.entity.TradeEntity;
import com.vanguard.demo.model.TradeFilterCondition;

public interface TradeRepositoryCustom {
    List<TradeEntity> findByDynamicConditions(List<TradeFilterCondition> conditions);
}