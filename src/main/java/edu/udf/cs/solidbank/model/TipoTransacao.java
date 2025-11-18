package edu.udf.cs.solidbank.model;

import lombok.Getter;
/**
 * ENUM: TipoTransacao
 * Define os tipos de transação disponíveis
 */
@Getter
public enum TipoTransacao {
    DEPOSITO("Depósito"),
    SAQUE("Saque"),
    TRANSFERENCIA("Transferência");

    private final String descricao;

    TipoTransacao(String descricao) {
        this.descricao = descricao;
    }

}
