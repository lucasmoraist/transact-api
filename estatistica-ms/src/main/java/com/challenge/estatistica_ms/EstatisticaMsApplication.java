package com.challenge.estatistica_ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class EstatisticaMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(EstatisticaMsApplication.class, args);
	}

}
