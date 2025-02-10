package com.challenge.estatistica_ms.feignclients;

import com.challenge.estatistica_ms.model.Transaction;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Component
@FeignClient(name = "transacao-ms", path = "/transacao")
public interface TransactionClient {

    @GetMapping
    ResponseEntity<List<Transaction>> findAll();
}
