package com.challenge.transacao_ms;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info = @Info(
		title = "Transacao API",
		version = "1.0",
		description = "API para transações"
))
@SpringBootApplication
public class TransacaoMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransacaoMsApplication.class, args);
	}

}
