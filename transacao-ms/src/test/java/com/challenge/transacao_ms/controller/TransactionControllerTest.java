package com.challenge.transacao_ms.controller;

import com.challenge.transacao_ms.dto.TransactionRequest;
import com.challenge.transacao_ms.model.Transaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @InjectMocks
    private TransactionController transactionController;

    @Test
    @DisplayName("case01 - Criação de transação com sucesso")
    void case01() {
        TransactionRequest request = new TransactionRequest(new BigDecimal(100));
        Transaction transaction = new Transaction(request);

        assertEquals(transaction.getValue(), new BigDecimal(100));
    }

    @Test
    @DisplayName("case04 - Exclusão com sucesso")
    void case04() {
        TransactionRequest request = new TransactionRequest(new BigDecimal(100));
        Transaction transaction = new Transaction(request);
        transactionController.createTransaction(request);

        assertThatCode(() -> transactionController.deleteTransaction(transaction.getId().toString()))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("case05 - Id de transação não encontrado")
    void case05() {
        String fakeId = UUID.randomUUID().toString();

        assertThatCode(() -> transactionController.deleteTransaction(fakeId))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("case06 - Listagem com sucesso com transações dentro")
    void case06() {
        TransactionRequest request = new TransactionRequest(new BigDecimal(100));
        transactionController.createTransaction(request);

        List<Transaction> transactions = transactionController.getTransactions().getBody();
        assertThat(transactions).isNotEmpty();
    }

    @Test
    @DisplayName("case07 - Listagem com sucesso sem transações dentro")
    void case07() {
        List<Transaction> transactions = transactionController.getTransactions().getBody();
        assertThat(transactions).isEmpty();
    }

}