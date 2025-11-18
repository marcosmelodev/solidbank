package edu.udf.cs.solidbank.dto;

import java.math.BigDecimal;

/**
 * Requisição para operação de depósito
 */
public record DadosDeposito(
        String numeroConta,
        BigDecimal valor,
        String descricao
) {

}
