package edu.udf.cs.solidbank.dto;

import java.math.BigDecimal;

public record DadosSaque(
        String numeroConta,
        BigDecimal valor,
        String descricao
) {
}
