package com.challenge.transacao_ms.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransactionRequest(
        @NotNull(message = "O valor da transação não pode ser nulo")
        @DecimalMin(value = "0.01", message = "O valor da transação deve ser maior que zero")
        BigDecimal value
) {

}
