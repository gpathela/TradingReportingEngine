package com.vanguard.demo.repository.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.vanguard.demo.entity.CurrencyEntity;
import com.vanguard.demo.entity.PartyEntity;
import com.vanguard.demo.entity.TradeEntity;
import com.vanguard.demo.model.PremiumAmountCondition;
import com.vanguard.demo.model.TradeFilterCondition;
import com.vanguard.demo.repository.CurrencyRepository;
import com.vanguard.demo.repository.PartyRepository;
import com.vanguard.demo.repository.TradeRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;

@DataJpaTest
@Import(TradeRepositoryImpl.class) // Import the custom repository implementation
public class TradeRepositoryImplTest {



    private static final String CURRENCY = "USD";

    private static final String SELLER = "Seller";

    private static final String BUYER = "Buyer";

    @Autowired
    private TradeRepositoryImpl tradeRepositoryImpl;

    @Autowired
    private TradeRepository tradeJpaRepository;

    @Autowired
    private PartyRepository partyRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @PersistenceContext
    private EntityManager entityManager;
    @Mock
    private CriteriaBuilder cb;

    @BeforeEach
    public void setUp() {
        // Set up test data
        PartyEntity buyerParty = new PartyEntity();
        buyerParty.setAttributeValue(BUYER);
        partyRepository.save(buyerParty);

        PartyEntity sellerParty = new PartyEntity();
        sellerParty.setAttributeValue(SELLER);
        partyRepository.save(sellerParty);

        CurrencyEntity premiumCurrency = new CurrencyEntity();
        premiumCurrency.setAttributeValue(CURRENCY);
        currencyRepository.save(premiumCurrency);

        TradeEntity tradeEntity = new TradeEntity();
        tradeEntity.setBuyerParty(buyerParty);
        tradeEntity.setSellerParty(sellerParty);
        tradeEntity.setPremiumCurrency(premiumCurrency);
        tradeEntity.setPremiumAmount(BigDecimal.valueOf(1000));
        tradeJpaRepository.save(tradeEntity);
    }

    @Test
    public void testFindByDynamicConditions_PremiumAmountGreaterThan() {
        List<TradeFilterCondition> conditions = new ArrayList<>();
        TradeFilterCondition condition = new TradeFilterCondition();
        condition.setPremiumAmount("400");
        condition.setPremiumAmountCondition(PremiumAmountCondition.GREATER_THAN);
        condition.setSellerParty(SELLER);
        conditions.add(condition);

        List<TradeEntity> results = tradeRepositoryImpl.findByDynamicConditions(conditions);
        assertThat(results).isNotEmpty();
        assertThat(results.get(0).getPremiumAmount()).isGreaterThan(BigDecimal.valueOf(400));
    }

    @Test
    public void testFindByDynamicConditions_SellerParty() {
        List<TradeFilterCondition> conditions = new ArrayList<>();
        TradeFilterCondition condition = new TradeFilterCondition();
        condition.setSellerParty(SELLER);
        condition.setPremiumAmountCondition(PremiumAmountCondition.LESS_THAN);
        condition.setPremiumAmount("1200");
        conditions.add(condition);

        List<TradeEntity> results = tradeRepositoryImpl.findByDynamicConditions(conditions);
        assertThat(results).isNotEmpty();
        assertThat(results.get(0).getSellerParty().getAttributeValue()).isEqualTo(SELLER);
    }

    @Test
    public void testFindByDynamicConditions_BuyerParty() {
        List<TradeFilterCondition> conditions = new ArrayList<>();
        TradeFilterCondition condition = new TradeFilterCondition();
        condition.setBuyerParty(BUYER);
        condition.setPremiumAmountCondition(PremiumAmountCondition.EQUALS);
        condition.setPremiumAmount("1000");
        conditions.add(condition);

        List<TradeEntity> results = tradeRepositoryImpl.findByDynamicConditions(conditions);
        assertThat(results).isNotEmpty();
        assertThat(results.get(0).getBuyerParty().getAttributeValue()).isEqualTo(BUYER);
    }
}