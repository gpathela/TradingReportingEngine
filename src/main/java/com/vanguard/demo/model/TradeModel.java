package com.vanguard.demo.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TradeModel {

    @JsonProperty("buyer_party")
    private String buyerParty;
    @JsonProperty("seller_party")
    private String sellerParty;
    @JsonProperty("premium_currency")
    private String premiumCurrency;
    @JsonProperty("premium_amount")
    private BigDecimal premiumAmount;
}
