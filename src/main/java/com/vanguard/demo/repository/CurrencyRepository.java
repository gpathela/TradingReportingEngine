package com.vanguard.demo.repository;

import com.vanguard.demo.entity.CurrencyEntity;

import io.swagger.v3.oas.annotations.Hidden;

import org.springframework.stereotype.Repository;

@Repository
@Hidden
public interface CurrencyRepository extends GenericRepository<CurrencyEntity, Long> {
    CurrencyEntity findByAttributeValue(String attributeValue);
}