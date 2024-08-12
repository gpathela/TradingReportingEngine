package com.vanguard.demo.repository.impl;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.vanguard.demo.entity.TradeEntity;
import com.vanguard.demo.model.TradeFilterCondition;
import com.vanguard.demo.repository.TradeRepositoryCustom;

import java.util.ArrayList;

import java.util.List;

@Repository
public class TradeRepositoryImpl implements TradeRepositoryCustom {

    private static final Logger LOGGER = LoggerFactory.getLogger(TradeRepositoryImpl.class);

    private static final String BUYER_PARTY = "buyerParty";
    private static final String PREMIUM_AMOUNT = "premiumAmount";
    private static final String SELLER_PARTY = "sellerParty";
    private static final String PREMIUM_CURRENCY = "premiumCurrency";
    private static final String ATTRIBUTE_VALUE = "attributeValue";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TradeEntity> findByDynamicConditions(List<TradeFilterCondition> conditions) {
        final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        final CriteriaQuery<TradeEntity> query = cb.createQuery(TradeEntity.class);
        final Root<TradeEntity> trade = query.from(TradeEntity.class);
        final List<Predicate> predicates = buildPredicates(cb, trade, conditions);
        if (!predicates.isEmpty()) {
            LOGGER.info("No of conditions: {}", predicates.size());
            query.where(cb.or(predicates.toArray(new Predicate[0])));
        }
        return entityManager.createQuery(query).getResultList();
    }

     /**
     * Adds a predicates for the conditions.
     */
    private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<TradeEntity> trade, List<TradeFilterCondition> conditions) {
        final List<Predicate> predicates = new ArrayList<>();
        for (TradeFilterCondition condition : CollectionUtils.emptyIfNull(conditions)) {
            List<Predicate> predicatesForEachCondition = new ArrayList<>();
            addBuyerPartyPredicate(cb, trade, condition, predicatesForEachCondition);
            addPremiumAmountPredicate(cb, trade, condition, predicatesForEachCondition);
            addSellerPartyPredicate(cb, trade, condition, predicatesForEachCondition);
            addPremiumCurrencyPredicate(cb, trade, condition, predicatesForEachCondition);
            Predicate predicate = cb.and(predicatesForEachCondition.toArray(new Predicate[0]));
            predicates.add(predicate);
        }
        return predicates;
    }

     /**
     * Adds a predicate for the buyer party condition.
     */
    private void addBuyerPartyPredicate(CriteriaBuilder cb, Root<TradeEntity> trade, TradeFilterCondition condition, List<Predicate> predicates) {
        if (condition.getBuyerParty() != null) {
            predicates.add(cb.equal(trade.get(BUYER_PARTY).get(ATTRIBUTE_VALUE), condition.getBuyerParty()));
        }
    }
    
     /**
     * Adds a predicate for the premium amount condition.
     */
    private void addPremiumAmountPredicate(CriteriaBuilder cb, Root<TradeEntity> trade, TradeFilterCondition condition, List<Predicate> predicates) {
        if (condition.getPremiumAmount() != null) {
            if (condition.getPremiumAmountCondition() != null) {
                switch (condition.getPremiumAmountCondition()) {
                    case GREATER_THAN:
                    predicates.add(cb.greaterThan(trade.get(PREMIUM_AMOUNT), condition.getPremiumAmount()));
                        break;
                    case LESS_THAN:
                    predicates.add(cb.lessThan(trade.get(PREMIUM_AMOUNT), condition.getPremiumAmount()));
                        break;
                    default:
                        predicates.add(cb.equal(trade.get(PREMIUM_AMOUNT), condition.getPremiumAmount()));
                        break;
                }
            }   
        }
    }
    
     /**
     * Adds a predicate for the seller party condition.
     */
    private void addSellerPartyPredicate(CriteriaBuilder cb, Root<TradeEntity> trade, TradeFilterCondition condition, List<Predicate> predicates) {
        if (condition.getSellerParty() != null) {
            predicates.add(cb.equal(trade.get(SELLER_PARTY).get(ATTRIBUTE_VALUE), condition.getSellerParty()));
        }
    }

     /**
     * Adds a predicate for the premium currency condition.
     */
    private void addPremiumCurrencyPredicate(CriteriaBuilder cb, Root<TradeEntity> trade, TradeFilterCondition condition, List<Predicate> predicates) {
        if (condition.getPremiumCurrency() != null) {
            predicates.add(cb.equal(trade.get(PREMIUM_CURRENCY).get(ATTRIBUTE_VALUE), condition.getPremiumCurrency()));
        }
    }
}




