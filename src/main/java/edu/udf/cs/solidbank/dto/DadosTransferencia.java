package edu.udf.cs.solidbank.dto;

import java.math.BigDecimal;

public record DadosTransferencia(
        String numeroContaorigem,
        String numeroContaDestino,
        BigDecimal valor,
        String descricao
) {
}
