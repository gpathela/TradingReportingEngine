package com.vanguard.demo.service.impl;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vanguard.demo.entity.CurrencyEntity;
import com.vanguard.demo.repository.CurrencyRepository;
import com.vanguard.demo.service.EntityService;

@Service
public  class CurrencyService implements EntityService<CurrencyEntity> {
    
    private final CurrencyRepository currencyRepository;

    @Autowired
    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Override
    public CurrencyEntity getOrCreateEntity(String attributeValue) {
        CurrencyEntity entity = currencyRepository.findByAttributeValue(attributeValue);
        if(Objects.isNull(entity)) {
            entity = new CurrencyEntity();
            entity.setAttributeValue(attributeValue);
            currencyRepository.save(entity);
        }
        return entity;
    }
    
}
