package com.senai.kaiquebahmad.consultasmedicas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("API de Consultas Médicas")
                        .description("API para gerenciamento de consultas médicas, médicos, pacientes e especialidades")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Kaique Ahmad")
                                .email("seu-email@example.com")
                                .url("https://github.com/kaiqueahmad"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}