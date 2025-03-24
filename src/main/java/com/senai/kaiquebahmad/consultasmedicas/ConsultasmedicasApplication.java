package com.senai.kaiquebahmad.consultasmedicas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.senai.kaiquebahmad.consultasmedicas.entity")
@EnableJpaRepositories(basePackages = "com.senai.kaiquebahmad.consultasmedicas.repository")
public class ConsultasmedicasApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsultasmedicasApplication.class, args);
	}

}
