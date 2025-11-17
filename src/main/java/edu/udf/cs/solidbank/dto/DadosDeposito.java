package edu.udf.cs.solidbank.dto;

import java.math.BigDecimal;

public record DadosDeposito(
        String numeroConta,
        BigDecimal valor,
        String descricao
) {

}
