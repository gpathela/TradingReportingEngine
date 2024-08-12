package com.vanguard.demo.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.vanguard.demo.entity.TradeEntity;
import com.vanguard.demo.model.TradeFilterResponse;
import com.vanguard.demo.model.TradeModel;
import com.vanguard.demo.populator.TradeEntityToModelPopulator;
import com.vanguard.demo.repository.TradeRepository;
import com.vanguard.demo.repository.impl.TradeRepositoryImpl;
import com.vanguard.demo.util.TestDataUtil;

@ExtendWith(MockitoExtension.class)
public class MainServiceTest {

    @Mock
    private TradeRepository tradeRepository;

    @Mock
    private TradeEntityToModelPopulator tradeEntityToModelPopulator;
    @Mock
    private TradeRepositoryImpl tradeRepositoryImpl;
    @Mock
    private XMLParserServiceImpl xmlParserService;
    @Mock
    private FileSystem fileSystem;

    @Mock
    private Path path1;
    @Mock
    private Path path2;
    @Mock
    private Stream<Path> pathStream;

    @InjectMocks
    private MainServiceImpl mainServiceImpl;

    @Test
    public void testFindAllTrades() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        TradeEntity tradeEntity = TestDataUtil.getTradeEntity("Buyer", "Seller", "USD", new BigDecimal(100));
        TradeModel tradeModel = new TradeModel();
        Page<TradeEntity> tradeEntities = new PageImpl<>(Collections.singletonList(tradeEntity));

        when(tradeRepository.findAll(pageable)).thenReturn(tradeEntities);
        when(tradeEntityToModelPopulator.populate(tradeEntity)).thenReturn(tradeModel);

        // Act
        Page<TradeModel> result = mainServiceImpl.findAllTrades(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(tradeModel, result.getContent().get(0));

        verify(tradeRepository, times(1)).findAll(pageable);
        verify(tradeEntityToModelPopulator, times(1)).populate(tradeEntity);
    }

    @Test
    public void testFindByDefaultConditions() {
        // Arrange
        TradeEntity tradeEntity1 = TestDataUtil.getTradeEntity("Buyer1", "Seller1", "USD", new BigDecimal(100));
        TradeEntity tradeEntity2 = TestDataUtil.getTradeEntity("Buyer2", "Seller2", "USD", new BigDecimal(200));
        List<TradeEntity> tradeEntities = List.of(tradeEntity1, tradeEntity2);
        when(tradeRepositoryImpl.findByDynamicConditions(anyList())).thenReturn(tradeEntities);
        when(tradeEntityToModelPopulator.populate(tradeEntity1)).thenReturn(new TradeModel());
        when(tradeEntityToModelPopulator.populate(tradeEntity2)).thenReturn(new TradeModel());

        // Act
        TradeFilterResponse response = mainServiceImpl.findByDefaultConditions();

        // Assert
        assertNotNull(response);
        assertEquals(2, response.getTrades().size());
        verify(tradeRepositoryImpl, times(1)).findByDynamicConditions(anyList());
        verify(tradeEntityToModelPopulator, times(2)).populate(any());
    }

    @Test
    public void testFindByDynamicConditions() {
        // Arrange
        TradeEntity tradeEntity1 = TestDataUtil.getTradeEntity("Buyer1", "Seller1", "USD", new BigDecimal(100));
        TradeEntity tradeEntity2 = TestDataUtil.getTradeEntity("Buyer2", "Seller2", "USD", new BigDecimal(200));
        
        List<TradeEntity> tradeEntities = List.of(tradeEntity1, tradeEntity2);
        TradeModel tradeModel1 = new TradeModel();
        TradeModel tradeModel2 = new TradeModel();

        when(tradeRepositoryImpl.findByDynamicConditions(anyList())).thenReturn(tradeEntities);
        when(tradeEntityToModelPopulator.populate(tradeEntity1)).thenReturn(tradeModel1);
        when(tradeEntityToModelPopulator.populate(tradeEntity2)).thenReturn(tradeModel2);

        // Act
        TradeFilterResponse response = mainServiceImpl.findByDynamicConditions(anyList());

        // Assert
        assertNotNull(response);
        assertEquals(2, response.getTrades().size());

        verify(tradeRepositoryImpl, times(1)).findByDynamicConditions(anyList());
        verify(tradeEntityToModelPopulator, times(2)).populate(any());
    }

}