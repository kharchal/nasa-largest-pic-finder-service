package com.example.nasalargestpicfinderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
public class NasaLargestPicFinderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NasaLargestPicFinderServiceApplication.class, args);
    }

}
