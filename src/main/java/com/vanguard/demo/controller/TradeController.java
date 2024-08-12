package com.vanguard.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vanguard.demo.exception.BadRequestException;
import com.vanguard.demo.model.TradeFilter;
import com.vanguard.demo.model.TradeFilterResponse;
import com.vanguard.demo.model.TradeModel;
import com.vanguard.demo.service.MainService;




@RestController
@RequestMapping("/api/trades")
public class TradeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TradeController.class);
    private MainService mainService;
    private final PagedResourcesAssembler<TradeModel> pagedResourcesAssembler;

    @Autowired
    public TradeController(MainService mainService, PagedResourcesAssembler<TradeModel> pagedResourcesAssembler) {
        this.mainService = mainService;
        this.pagedResourcesAssembler = new PagedResourcesAssembler<>(null, null);
    }

     /**
     * Get all trades with pagination.
     *
     * @param page the page number to retrieve
     * @param size the number of records per page
     * @return ResponseEntity containing a paginated list of trades
     */
    @GetMapping("")
    public ResponseEntity<PagedModel<EntityModel<TradeModel>>> getAllTrades(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TradeModel> trades = mainService.findAllTrades(pageable);
        PagedModel<EntityModel<TradeModel>> pagedModel = pagedResourcesAssembler.toModel(trades);
        return ResponseEntity.ok(pagedModel);
    }

    /**
     * Get all trades with default conditions.
     *
     * @return ResponseEntity containing TradeFilterResponse
     */
    @GetMapping("/defaultsearch")
    public ResponseEntity<TradeFilterResponse> defaultSearch() {
        return ResponseEntity.ok(mainService.findByDefaultConditions());
    }

    /**
     * Search trades based on provided filter conditions.
     *
     * @param filter TradeFilter containing search conditions
     * @return ResponseEntity containing TradeFilterResponse
     * @throws BadRequestException if filter conditions are missing
     */
    @PostMapping("/search")
    public ResponseEntity<TradeFilterResponse> search(@RequestBody TradeFilter filter) throws BadRequestException  {
        if(filter.getConditions() == null || filter.getConditions().isEmpty()) {
            LOGGER.error("Conditions are required");
            throw new BadRequestException("Conditions are required");
        }
        return ResponseEntity.ok(mainService.findByDynamicConditions(filter.getConditions()));
    }


    
    
}
