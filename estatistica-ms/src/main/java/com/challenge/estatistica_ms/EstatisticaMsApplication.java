package com.challenge.estatistica_ms;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@OpenAPIDefinition(info = @Info(
		title = "Estatistica API",
		version = "1.0",
		description = "API para estatísticas"
))
@SpringBootApplication
@EnableFeignClients
public class EstatisticaMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(EstatisticaMsApplication.class, args);
	}

}
