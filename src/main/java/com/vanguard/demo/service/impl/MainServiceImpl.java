package com.vanguard.demo.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.vanguard.demo.entity.TradeEntity;
import com.vanguard.demo.model.TradeFilterCondition;
import com.vanguard.demo.model.TradeFilterResponse;
import com.vanguard.demo.model.TradeModel;
import com.vanguard.demo.populator.TradeEntityToModelPopulator;
import com.vanguard.demo.repository.TradeRepository;
import com.vanguard.demo.repository.impl.TradeRepositoryImpl;
import com.vanguard.demo.service.MainService;
import com.vanguard.demo.service.XMLParserService;
import com.vanguard.demo.util.Utils;

@Service
public class MainServiceImpl implements MainService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainServiceImpl.class);
    public static final String SELLER_PARTY_CONDITION_1 = "EMU_BANK";
    public static final String CURRENCY_CONDITION_1 = "AUD";
    public static final String SELLER_PARTY_CONDITION_2 = "BISON_BANK";
    public static final String CURRENCY_CONDITION_2 = "USD";

    private XMLParserService xmlParserService;
    private TradeRepositoryImpl tradeRepositoryImpl;
    private TradeRepository tradeRepository;
    private TradeEntityToModelPopulator tradeEntityToModelPopulator;

    @Autowired
    public MainServiceImpl(XMLParserService xmlParserService, 
                            TradeRepositoryImpl tradeRepositoryImpl, 
                            TradeEntityToModelPopulator tradeEntityToModelPopulator,
                            TradeRepository tradeRepository) {
        this.xmlParserService = xmlParserService;
        this.tradeRepositoryImpl = tradeRepositoryImpl;
        this.tradeEntityToModelPopulator = tradeEntityToModelPopulator;
        this.tradeRepository = tradeRepository;
    }

    /**
     * Find all trades with pagination.
     *
     * @param pageable the pagination information
     * @return a paginated list of trades
     */
    public Page<TradeModel> findAllTrades(Pageable pageable) {
        Page<TradeEntity> trades = tradeRepository.findAll(pageable);
        return trades.map(tradeEntityToModelPopulator::populate);
    }

    /**
     * Find trades by default condition.
     *
     *
     * @return TradeFilterResponse
     */
    @Override
    public TradeFilterResponse findByDefaultConditions() {
        TradeFilterCondition condition = new TradeFilterCondition();
        condition.setSellerParty(SELLER_PARTY_CONDITION_1);
        condition.setPremiumCurrency(CURRENCY_CONDITION_1);
        TradeFilterCondition condition2 = new TradeFilterCondition();
        condition2.setSellerParty(SELLER_PARTY_CONDITION_2);
        condition2.setPremiumCurrency(CURRENCY_CONDITION_2);
        List<TradeEntity> entities = tradeRepositoryImpl.findByDynamicConditions(List.of(condition, condition2));
        entities = CollectionUtils.emptyIfNull(entities).stream()
                .filter(entity -> !areAnagrams(entity.getBuyerParty().getAttributeValue(), entity.getSellerParty().getAttributeValue()))
                .collect(Collectors.toList());
        LOGGER.info("Found {} entities", entities.size());
        return new TradeFilterResponse(populateTradeModels(entities));
    }

    /**
     * Find trades by user given conditions.
     *
     * @param conditions the list of conditions
     * @return TradeFilterResponse
     */
    @Override
    public TradeFilterResponse findByDynamicConditions(List<TradeFilterCondition> conditions) {
        List<TradeEntity> entities = tradeRepositoryImpl.findByDynamicConditions(conditions);
        LOGGER.info("Found {} entities", entities.size());
        return new TradeFilterResponse(populateTradeModels(entities));
    }

    /**
     * Read trade event from a xml file & insert it in DB.
     *
     * @param path the path of xml file
     * @return String
     */
    @Override
    public String loadData(final String path) {
        List<TradeEntity> events = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get(path))) {
            events =  paths.filter(Files::isRegularFile)
                    .map(Path::toString)
                    .map(xmlParserService::parseTradeXML)
                    .collect(Collectors.toList());
            tradeRepository.saveAll(events);
            LOGGER.info("Loaded {} events from path: {}", events.size(), path);
        } catch (IOException e) {
            LOGGER.error("Error while loading data from path: {}", path, e);
            return Utils.ERROR_LOADING_DATA;
        }
        return "events loaded";
    }

    private List<TradeModel> populateTradeModels(List<TradeEntity> entities) {
        return CollectionUtils.emptyIfNull(entities).stream()
                .map(tradeEntityToModelPopulator::populate)
                .collect(Collectors.toList());
    }

    private boolean areAnagrams(String str1, String str2) {
        if (str1.length() != str2.length()) {
            return false;
        }
        char[] charArray1 = str1.toCharArray();
        char[] charArray2 = str2.toCharArray();
        Arrays.sort(charArray1);
        Arrays.sort(charArray2);
        return Arrays.equals(charArray1, charArray2);
    }
        
}
