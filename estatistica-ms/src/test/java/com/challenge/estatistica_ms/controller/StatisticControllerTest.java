package com.challenge.estatistica_ms.controller;

import com.challenge.estatistica_ms.feignclients.TransactionClient;
import com.challenge.estatistica_ms.model.Statistic;
import com.challenge.estatistica_ms.model.Transaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatisticControllerTest {

    @Mock
    private TransactionClient transactionClient;

    @InjectMocks
    private StatisticController statisticController;

    @Test
    @DisplayName("case01 - Deve retornar estatísticas corretas com transações válidas")
    void case01() {
        OffsetDateTime now = OffsetDateTime.now();
        UUID id = UUID.randomUUID();
        List<Transaction> transactions = List.of(
                new Transaction(id, new BigDecimal("10.00"), now.minusSeconds(30).toString()),
                new Transaction(id, new BigDecimal("20.00"), now.minusSeconds(10).toString())
        );

        when(transactionClient.findAll()).thenReturn(ResponseEntity.ok(transactions));

        Statistic result = statisticController.getEstatistica();

        assertEquals(2, result.getCount());
        assertEquals(new BigDecimal("30.00"), result.getSum());
        assertEquals(new BigDecimal("15.00"), result.getAvg());
        assertEquals(new BigDecimal("10.00"), result.getMin());
        assertEquals(new BigDecimal("20.00"), result.getMax());
    }

    @Test
    @DisplayName("case02 - Deve ignorar transações mais antigas que 60 segundos")
    void case02() {
        OffsetDateTime now = OffsetDateTime.now();
        UUID id = UUID.randomUUID();
        List<Transaction> transactions = List.of(
                new Transaction(id, new BigDecimal("10.00"), now.minusSeconds(70).toString()), // Fora do intervalo
                new Transaction(id, new BigDecimal("20.00"), now.minusSeconds(10).toString()) // Dentro do intervalo
        );

        when(transactionClient.findAll()).thenReturn(ResponseEntity.ok(transactions));

        Statistic result = statisticController.getEstatistica();

        assertEquals(1, result.getCount());
        assertEquals(new BigDecimal("20.00"), result.getSum());
        assertEquals(new BigDecimal("20.00"), result.getAvg());
        assertEquals(new BigDecimal("20.00"), result.getMin());
        assertEquals(new BigDecimal("20.00"), result.getMax());
    }

    @Test
    @DisplayName("case03 - Deve retornar estatísticas zeradas quando todas as transações são antigas")
    void case03() {
        OffsetDateTime now = OffsetDateTime.now();
        UUID id = UUID.randomUUID();
        List<Transaction> transactions = List.of(
                new Transaction(id, new BigDecimal("10.00"), now.minusSeconds(70).toString()),
                new Transaction(id, new BigDecimal("20.00"), now.minusSeconds(80).toString())
        );

        when(transactionClient.findAll()).thenReturn(ResponseEntity.ok(transactions));

        Statistic result = statisticController.getEstatistica();

        assertEquals(0, result.getCount());
        assertEquals(BigDecimal.ZERO, result.getSum());
        assertEquals(BigDecimal.ZERO, result.getAvg());
        assertNull(result.getMin());
        assertEquals(BigDecimal.ZERO, result.getMax());
    }

}