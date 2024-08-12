package com.vanguard.demo.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.vanguard.demo.service.MainService;
import com.vanguard.demo.util.Utils;


@Component
public class DataInitializer implements CommandLineRunner {

    private static final String EVENTS_RESOURCE_DIRECTORY = "src/main/resources/events";
    private static final Logger LOGGER = LoggerFactory.getLogger(DataInitializer.class);
    private MainService mainService;

    @Autowired
    public DataInitializer(MainService mainService) {
        this.mainService = mainService;
    }

    @Override
    public void run(String... args) throws Exception {
        final String loadingMessage = mainService.loadData(EVENTS_RESOURCE_DIRECTORY);
        if(loadingMessage.equals(Utils.ERROR_LOADING_DATA)) {
            LOGGER.error("Failed to load initial data, queries won't work. Check path: {} for event files" , EVENTS_RESOURCE_DIRECTORY);
        }
    }
}