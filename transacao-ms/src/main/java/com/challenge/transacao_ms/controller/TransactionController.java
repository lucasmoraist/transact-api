package com.challenge.transacao_ms.controller;

import com.challenge.transacao_ms.dto.TransactionRequest;
import com.challenge.transacao_ms.model.Transaction;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/transacao")
public class TransactionController {

    private final List<Transaction> transactions = new ArrayList<>();

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody @Valid TransactionRequest transactionRequest) {
        log.info("Recebendo solicitação para criar transação: {}", transactionRequest);

        Transaction transaction = new Transaction(transactionRequest);

        transactions.add(transaction);
        log.info("Transação criada com sucesso: {}", transaction);

        return ResponseEntity.ok().body(transaction);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable String id) {
        log.info("Recebendo solicitação para excluir transação com ID: {}", id);

        boolean removed = transactions.removeIf(t -> t.getId().toString().equals(id));

        if (removed) {
            log.info("Transação removida com sucesso: {}", id);
        } else {
            log.warn("Tentativa de remoção falhou. Transação com ID {} não encontrada", id);
        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getTransactions() {
        log.info("Buscando todas as transações. Total encontradas: {}", transactions.size());
        return ResponseEntity.ok().body(transactions);
    }
}
