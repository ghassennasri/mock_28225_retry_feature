package com.marklogic.mock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@SpringBootApplication

public class Mock28225Application {

    public static void main(String[] args) {
        SpringApplication.run(Mock28225Application.class, args);
    }

}
