package com.challenge.transacao_ms.controller;

import com.challenge.transacao_ms.dto.TransactionRequest;
import com.challenge.transacao_ms.model.Transaction;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/transacao")
public class TransactionController {

    private final List<Transaction> transactions = new ArrayList<>();

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody @Valid TransactionRequest transactionRequest) {
        Transaction transaction = new Transaction(transactionRequest);

        OffsetDateTime schedule = OffsetDateTime.parse(transactionRequest.timestamp());

        if (schedule.isAfter(OffsetDateTime.now())) {
            throw new IllegalArgumentException("Transação agendada não permitida");
        }

        transactions.add(transaction);

        return ResponseEntity.ok().body(transaction);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable String id) {
        transactions.removeIf(t -> t.getId().toString().equals(id));
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getTransactions() {
        return ResponseEntity.ok().body(transactions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransaction(@PathVariable String id) {
        return ResponseEntity.ok().body(transactions.stream()
                .filter(t -> t.getId().toString().equals(id))
                .findFirst()
                .orElseThrow());
    }
}
