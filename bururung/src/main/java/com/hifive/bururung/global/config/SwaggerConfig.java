package com.hifive.bururung.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Hifive Bururung API")
                .version("1.0.0")
                .description("하이파이브 부르릉 API 문서")
                .contact(new Contact()
                    .name("Team. hifive")
                )
            );
    }
}