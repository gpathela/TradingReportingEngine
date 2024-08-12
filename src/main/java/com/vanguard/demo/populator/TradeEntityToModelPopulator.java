package com.vanguard.demo.populator;

import org.springframework.stereotype.Component;

import com.vanguard.demo.entity.TradeEntity;
import com.vanguard.demo.model.TradeModel;

@Component
public class TradeEntityToModelPopulator {

    public TradeModel populate(TradeEntity entity) {
        if(entity == null) {
            return null;
        }
        TradeModel model = new TradeModel();
        model.setBuyerParty(entity.getBuyerParty().getAttributeValue());
        model.setSellerParty(entity.getSellerParty().getAttributeValue());
        model.setPremiumCurrency(entity.getPremiumCurrency().getAttributeValue());
        model.setPremiumAmount(entity.getPremiumAmount());
        return model;
    }
}