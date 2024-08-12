package com.vanguard.demo.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class TradeEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "buyer_party")
    private PartyEntity buyerParty;
    @ManyToOne
    @JoinColumn(name = "seller_party")
    private PartyEntity sellerParty;
    @ManyToOne
    private CurrencyEntity premiumCurrency;
    private BigDecimal premiumAmount;

}
