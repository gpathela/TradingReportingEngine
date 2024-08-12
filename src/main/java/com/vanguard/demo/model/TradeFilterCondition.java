package com.vanguard.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TradeFilterCondition {
    @JsonProperty("seller_party")
    private String sellerParty;
    @JsonProperty("buyer_party")
    private String buyerParty;
    @JsonProperty("premium_currency")
    private String premiumCurrency;
    @JsonProperty("premium_amount")
    private String premiumAmount;
    @JsonProperty("premium_amount_condition")
    private PremiumAmountCondition premiumAmountCondition;
}

