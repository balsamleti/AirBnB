package com.airbnb.be;

import lombok.Generated;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@Generated
@EnableScheduling
@SpringBootApplication
public class ABManager {

    public static void main(String[] args) {
        SpringApplication.run(ABManager.class, args);
    }

}