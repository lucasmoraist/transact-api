package com.challenge.estatistica_ms.controller;

import com.challenge.estatistica_ms.feignclients.TransactionClient;
import com.challenge.estatistica_ms.model.Statistic;
import com.challenge.estatistica_ms.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/estatistica")
public class StatisticController {

    @Autowired
    private TransactionClient transactionClient;

    @GetMapping
    public Statistic getEstatistica() {
        List<Transaction> transactions = this.transactionClient.findAll().getBody();

        OffsetDateTime currentTime = OffsetDateTime.now();
        BigDecimal sum = BigDecimal.ZERO;
        BigDecimal max = BigDecimal.ZERO;
        BigDecimal min = null;
        BigDecimal avg = BigDecimal.ZERO;
        int count = 0;

        for (Transaction transaction : transactions) {
            if (isWithinLastMinute(OffsetDateTime.parse(transaction.getTimestamp()), currentTime)) {
                sum = sum.add(transaction.getValue());

                max = max.max(transaction.getValue());

                if (min == null || transaction.getValue().compareTo(min) < 0) {
                    min = transaction.getValue();
                }

                count++;
            }
        }

        if (count > 0) {
            avg = sum.divide(BigDecimal.valueOf(count), RoundingMode.HALF_UP);
        }

        return new Statistic(count, sum, avg, min, max);
    }

    private boolean isWithinLastMinute(OffsetDateTime transactionTime, OffsetDateTime currentTime) {
        Duration duration = Duration.between(transactionTime, currentTime);
        return duration.getSeconds() <= 60;
    }

}
