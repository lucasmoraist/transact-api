package com.challenge.estatistica_ms.controller;

import com.challenge.estatistica_ms.feignclients.TransactionClient;
import com.challenge.estatistica_ms.model.Statistic;
import com.challenge.estatistica_ms.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/estatistica")
public class StatisticController {

    @Autowired
    private TransactionClient transactionClient;

    @GetMapping
    public Statistic getEstatistica() {
        log.info("Iniciando cálculo de estatísticas");
        List<Transaction> transactions = this.transactionClient.findAll().getBody();

        if (transactions == null) {
            log.warn("Nenhuma transação encontrada");
            return new Statistic(0, BigDecimal.ZERO, BigDecimal.ZERO, null, BigDecimal.ZERO);
        }

        OffsetDateTime currentTime = OffsetDateTime.now();
        BigDecimal sum = BigDecimal.ZERO;
        BigDecimal max = BigDecimal.ZERO;
        BigDecimal min = null;
        BigDecimal avg = BigDecimal.ZERO;
        int count = 0;

        for (Transaction transaction : transactions) {
            OffsetDateTime transactionTime = OffsetDateTime.parse(transaction.getTimestamp());
            if (isWithinLastMinute(transactionTime, currentTime)) {
                log.debug("Transação válida: {} com valor {}", transactionTime, transaction.getValue());
                sum = sum.add(transaction.getValue());
                max = max.max(transaction.getValue());

                if (min == null || transaction.getValue().compareTo(min) < 0) {
                    min = transaction.getValue();
                }
                count++;
            } else {
                log.debug("Transação ignorada: {}", transactionTime);
            }
        }

        if (count > 0) {
            avg = sum.divide(BigDecimal.valueOf(count), RoundingMode.HALF_UP);
        }

        log.info("Estatísticas calculadas: count={}, sum={}, avg={}, min={}, max={}", count, sum, avg, min, max);
        return new Statistic(count, sum, avg, min, max);
    }

    private boolean isWithinLastMinute(OffsetDateTime transactionTime, OffsetDateTime currentTime) {
        Duration duration = Duration.between(transactionTime, currentTime);
        boolean result = duration.getSeconds() <= 60;
        log.debug("Verificando transação: {} - Está no último minuto? {}", transactionTime, result);
        return result;
    }

}
