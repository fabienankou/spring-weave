package com.example.springweave.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SHOPIVERS E-Commerce API")
                        .version("1.0.0")
                        .description("API REST pour la plateforme e-commerce cross-border SHOPIVERS")
                        .contact(new Contact()
                                .name("Support SHOPIVERS")
                                .email("support@shopivers.com")
                                .url("https://www.shopivers.com")));
    }
}

