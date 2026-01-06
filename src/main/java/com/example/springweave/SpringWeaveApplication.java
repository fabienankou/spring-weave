package com.example.springweave;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringWeaveApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringWeaveApplication.class, args);
    }

}