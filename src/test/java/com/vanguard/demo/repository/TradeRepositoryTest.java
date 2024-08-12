package com.vanguard.demo.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.vanguard.demo.entity.CurrencyEntity;
import com.vanguard.demo.entity.PartyEntity;
import com.vanguard.demo.entity.TradeEntity;


@DataJpaTest
public class TradeRepositoryTest {

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private PartyRepository partyRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Test
    public void testSaveAndFindTradeEntity() {
        PartyEntity buyerParty = new PartyEntity();
        buyerParty.setAttributeValue("Buyer");
        partyRepository.save(buyerParty);

        PartyEntity sellerParty = new PartyEntity();
        sellerParty.setAttributeValue("Seller");
        partyRepository.save(sellerParty);

        CurrencyEntity premiumCurrency = new CurrencyEntity();
        premiumCurrency.setAttributeValue("USD");
        currencyRepository.save(premiumCurrency);

        TradeEntity tradeEntity = new TradeEntity();
        tradeEntity.setBuyerParty(buyerParty);
        tradeEntity.setSellerParty(sellerParty);
        tradeEntity.setPremiumCurrency(premiumCurrency);
        tradeEntity.setPremiumAmount(BigDecimal.valueOf(1000));

        tradeRepository.save(tradeEntity);

        TradeEntity foundEntity = tradeRepository.findById(tradeEntity.getId()).orElse(null);
        assertThat(foundEntity).isNotNull();
        assertThat(foundEntity.getBuyerParty().getAttributeValue()).isEqualTo("Buyer");
        assertThat(foundEntity.getSellerParty().getAttributeValue()).isEqualTo("Seller");
        assertThat(foundEntity.getPremiumCurrency().getAttributeValue()).isEqualTo("USD");
        assertThat(foundEntity.getPremiumAmount()).isEqualTo(BigDecimal.valueOf(1000));
    }
}
