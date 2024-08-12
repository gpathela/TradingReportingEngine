package com.vanguard.demo.repository;

import com.vanguard.demo.entity.PartyEntity;

import io.swagger.v3.oas.annotations.Hidden;

import org.springframework.stereotype.Repository;

@Repository
@Hidden
public interface PartyRepository extends GenericRepository<PartyEntity, Long> {
    PartyEntity findByAttributeValue(String attributeValue);
}