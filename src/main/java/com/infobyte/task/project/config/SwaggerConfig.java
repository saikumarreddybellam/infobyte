package com.infobyte.task.project.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI quizApi() {
        return new OpenAPI()
                .info(new Info().title("Online Quiz API")
                        .description("Quiz platform backend")
                        .version("1.0.0"));
    }
}
