package edu.udf.cs.solidbank.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DadosTransacao(
        Long id,
        String numeroContaOrigem,
        String numeroContaDestino,
        String tipoTransacao,
        BigDecimal valor,
        LocalDateTime dataTransacao,
        String descricao
) {
}
