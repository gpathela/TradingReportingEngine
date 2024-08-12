package com.vanguard.demo.service;

import com.vanguard.demo.entity.TradeEntity;


public interface XMLParserService {

    TradeEntity parseTradeXML(String xmlFilePath);
    
} 