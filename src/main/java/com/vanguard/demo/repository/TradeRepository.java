package com.vanguard.demo.repository;

import com.vanguard.demo.entity.TradeEntity;

import io.swagger.v3.oas.annotations.Hidden;

import org.springframework.stereotype.Repository;

@Repository
@Hidden
public interface TradeRepository extends GenericRepository<TradeEntity, Long>, TradeRepositoryCustom {
}