package com.java;

import com.java.car.service.CommandLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
public class CarshopServerApplication implements CommandLineRunner {
    @Autowired
    private CommandLineService commandLineService;

    @Value("${load.default-data}")
    private boolean loadData;

    public static void main(String[] args) {
        SpringApplication.run(CarshopServerApplication.class, args);
    }

    public void run(String... params) throws IOException {
        if (loadData) {
            commandLineService.loadCarDetails();
        }

    }
}
