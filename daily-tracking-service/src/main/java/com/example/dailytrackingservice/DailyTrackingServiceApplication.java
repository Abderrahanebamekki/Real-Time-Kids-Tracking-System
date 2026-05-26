package com.example.dailytrackingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DailyTrackingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DailyTrackingServiceApplication.class, args);
    }

}
