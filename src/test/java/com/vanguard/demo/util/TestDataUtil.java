package com.vanguard.demo.util;

import java.math.BigDecimal;
import java.util.List;


import com.vanguard.demo.entity.CurrencyEntity;
import com.vanguard.demo.entity.PartyEntity;
import com.vanguard.demo.entity.TradeEntity;
import com.vanguard.demo.model.PremiumAmountCondition;
import com.vanguard.demo.model.TradeFilterCondition;
import com.vanguard.demo.model.TradeFilterResponse;
import com.vanguard.demo.model.TradeModel;

public class TestDataUtil {
 
    public static TradeModel tradeModel = new TradeModel("KANMU_EB", "EMU_BANK", "AUD", new BigDecimal(300.00));
    public static TradeModel tradeModel1 = new TradeModel("EMU_BANK", "BISON_BANK", "USD", new BigDecimal(500.00));
    public static TradeModel tradeModel2 = new TradeModel("EMU_BANK", "BISON_BANK", "USD", new BigDecimal(600.00));
    public static TradeFilterCondition tradeFilterCondition = new TradeFilterCondition("SELLER_PARTY", "BUYER_PARTY", "USD", "400.00", PremiumAmountCondition.GREATER_THAN);
    public static TradeEntity tradeEntity = new TradeEntity();
    
    


    public static TradeFilterResponse getTrades() {
        return new TradeFilterResponse(List.of(tradeModel, tradeModel1, tradeModel2));
    }

    public static TradeEntity getTradeEntity(String buyerParty, String sellerParty, String currency, BigDecimal premiumAmount) {
        tradeEntity.setBuyerParty(getParty(buyerParty));
        tradeEntity.setSellerParty(getParty(sellerParty));
        tradeEntity.setPremiumCurrency(getCurrencyEntity(currency));
        tradeEntity.setPremiumAmount(premiumAmount);
        return tradeEntity;
    }

    public static PartyEntity getParty(String attributeValue) {
        PartyEntity buyerParty = new PartyEntity();
        buyerParty.setAttributeValue(attributeValue);
        return buyerParty;
    }

    public static CurrencyEntity getCurrencyEntity(String attributeValue) {
        CurrencyEntity premiumCurrency = new CurrencyEntity();
        premiumCurrency.setAttributeValue(attributeValue);
        return premiumCurrency;
    }
        
}