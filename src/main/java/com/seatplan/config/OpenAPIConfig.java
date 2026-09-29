package com.seatplan.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SeatPlan — Exam Hall Seating Arrangement Generator REST API")
                        .version("1.0.0")
                        .description("Production-grade Spring Boot REST API for managing halls, seats, students, exam sessions, and generating optimal seating arrangements avoiding adjacent same-section allocations.")
                        .contact(new Contact()
                                .name("Exam Cell Administration")
                                .email("admin@seatplan.edu"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}
