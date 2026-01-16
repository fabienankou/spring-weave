package com.example.springweave.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class AsyncConfig {
    // Cette annotation permet d'utiliser @Async sur nos méthodes de service
}