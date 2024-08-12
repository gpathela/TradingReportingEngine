package com.vanguard.demo.service.impl;

import com.vanguard.demo.entity.TradeEntity;
import com.vanguard.demo.service.XMLParserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class XMLParserServiceImpl implements XMLParserService {

    private static final String AMOUNT = "amount";
    private static final String CURRENCY = "currency";
    private static final String HREF = "href";
    private static final String PAYMENT_AMOUNT_REFERENCE = "//paymentAmount";
    private static final String SELLER_PARTY_REFERENCE = "//sellerPartyReference";
    private static final String BUYER_PARTY_REFERENCE = "//buyerPartyReference";

    private final PartyService partyService;
    private final CurrencyService currencyService;

    @Autowired
    public XMLParserServiceImpl(PartyService partyService, CurrencyService currencyService) {
        this.partyService = partyService;
        this.currencyService = currencyService;
    }

    @Override
    public TradeEntity parseTradeXML(String xmlFilePath) {
        try {
            // Initialize DocumentBuilder
            final DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            final Document document = builder.parse(new File(xmlFilePath));

            // Initialize XPath
            final XPath xPath = XPathFactory.newInstance().newXPath();

            final String buyerParty = getAttributeValue(xPath, document, BUYER_PARTY_REFERENCE, HREF);
            final String sellerParty = getAttributeValue(xPath, document, SELLER_PARTY_REFERENCE, HREF);
            
            // Extract currency and amount from paymentAmount
            final Element paymentAmountElement = getAttribute(xPath, document, PAYMENT_AMOUNT_REFERENCE);
            final String currency = getElementTextContent(paymentAmountElement, CURRENCY);
            final BigDecimal amount = new BigDecimal(getElementTextContent(paymentAmountElement, AMOUNT));

            // Create and populate TradeEntity object
            final TradeEntity tradeEntity = new TradeEntity();
            tradeEntity.setBuyerParty(partyService.getOrCreateEntity(buyerParty));
            tradeEntity.setSellerParty(partyService.getOrCreateEntity(sellerParty));
            tradeEntity.setPremiumCurrency(currencyService.getOrCreateEntity(currency));
            tradeEntity.setPremiumAmount(amount);
            return tradeEntity;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    
    private String getAttributeValue(XPath xPath, Document document, String expression, String attribute) throws Exception {
        return  getAttribute(xPath, document, expression).getAttribute(attribute);
    }

    private Element getAttribute(XPath xPath, Document document, String expression) throws Exception {
        XPathExpression xPathExpression = xPath.compile(expression);
        Element element = (Element) xPathExpression.evaluate(document, XPathConstants.NODE);
        if (Objects.isNull(element)) {
            throw new NoSuchElementException("Element not found for expression: " + expression);
        }
        return element;
    }

    private String getElementTextContent(Element parentElement, String tagName) {
        Element element = (Element) parentElement.getElementsByTagName(tagName).item(0);
        if (Objects.isNull(element)) {
            throw new NoSuchElementException("Element not found for tag name: " + tagName);
        }
        String textContent = element.getTextContent();
        if (Objects.isNull(textContent) || textContent.isEmpty()) {
            throw new IllegalArgumentException("Text content not found for tag name: " + tagName);
        }
        return textContent;
    }
}   
