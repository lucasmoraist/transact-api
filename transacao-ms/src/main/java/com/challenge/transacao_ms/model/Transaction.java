package com.challenge.transacao_ms.model;

import com.challenge.transacao_ms.dto.TransactionRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    private UUID id;
    private BigDecimal value;
    private String timestamp;

    public Transaction(TransactionRequest request) {
        this.id = UUID.randomUUID();
        this.value = request.value();
        this.timestamp = OffsetDateTime.now().toString();
    }

}
