package edu.udf.cs.solidbank.model;

import lombok.Getter;

@Getter
public enum TipoConta {

    CORRENTE("Conta Corrente"),
    POUPANCA("Conta Poupança"),
    JURIDICA("Conta Jurídica");

    private final String descricao;

    TipoConta(String descricao) {
        this.descricao = descricao;
    }

    public static TipoConta tipoConta(String tipo) {
        for (TipoConta t : TipoConta.values()){
            if (t.name().equalsIgnoreCase(tipo)){
                return t;
            }
        }
        throw new IllegalArgumentException(("Tipo de conta inválido: " + tipo));
    }
}
