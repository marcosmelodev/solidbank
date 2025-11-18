package edu.udf.cs.solidbank.model;

import lombok.Getter;
/**
 * ENUM: TipoConta
 * Define os tipos de conta disponíveis no banco
 *
 * Tipos:
 * - CORRENTE: Conta corrente para movimentações diárias
 * - POUPANCA: Conta poupança para investimento
 * - JURIDICA: Conta para pessoas jurídicas
 */

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
