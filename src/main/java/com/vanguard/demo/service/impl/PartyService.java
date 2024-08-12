package com.vanguard.demo.service.impl;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vanguard.demo.entity.PartyEntity;
import com.vanguard.demo.repository.PartyRepository;
import com.vanguard.demo.service.EntityService;

@Service
public class PartyService implements EntityService<PartyEntity> {
    private final PartyRepository partyRepository;

    @Autowired
    public PartyService(PartyRepository partyRepository) {
        this.partyRepository = partyRepository;
    }

    @Override
    public PartyEntity getOrCreateEntity(String attributeValue) {
        PartyEntity partyEntity = partyRepository.findByAttributeValue(attributeValue);
        if(Objects.isNull(partyEntity)) {
            partyEntity = new PartyEntity();
            partyEntity.setAttributeValue(attributeValue);
            partyRepository.save(partyEntity);
        }
        return partyEntity;
    }
    
}
