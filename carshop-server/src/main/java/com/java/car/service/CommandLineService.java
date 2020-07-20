package com.java.car.service;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.car.domain.Car;
import com.java.car.domain.Warehouse;
import com.java.car.repository.WarehouseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Serivce class for the application. To load data.
 */
@Service
public class CommandLineService {
    private static final Logger log = LoggerFactory.getLogger(CommandLineService.class);

    private final WarehouseRepository warehouseRepository;


    public CommandLineService(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    /**
     * Load Car data on startup
     *
     * @throws IOException when jspn file is not found
     */

    public void loadCarDetails() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        List<Warehouse> inputList = mapper.readValue(new File(CommandLineService.class.getClassLoader().getResource("dataset/jsonCars.json").getFile()), mapper.getTypeFactory().constructCollectionType(List.class, Warehouse.class));
        log.debug("Mock data to be loaded -- {}",inputList.size());

        warehouseRepository.saveAll(inputList);

    }
}
