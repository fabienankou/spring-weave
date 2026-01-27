package com.example.springweave.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
// C'est cette annotation qui active le remplissage auto des dates
public class JPAconfig {
}