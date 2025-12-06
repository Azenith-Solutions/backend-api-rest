package com.azenithsolutions.backendapirest;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableRabbit
@EnableCaching
@SpringBootApplication
public class BackendApiRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApiRestApplication.class, args);
    }

}
