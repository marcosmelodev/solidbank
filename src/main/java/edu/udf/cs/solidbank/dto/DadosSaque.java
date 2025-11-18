package edu.udf.cs.solidbank.dto;

import java.math.BigDecimal;
/**
 * Requisição para operação de saque
 */
public record DadosSaque(
        String numeroConta,
        BigDecimal valor,
        String descricao
) {
}
