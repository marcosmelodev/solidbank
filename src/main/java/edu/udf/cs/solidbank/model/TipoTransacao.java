package edu.udf.cs.solidbank.model;

import lombok.Getter;

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
