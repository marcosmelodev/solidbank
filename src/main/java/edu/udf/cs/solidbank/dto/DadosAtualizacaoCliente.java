package edu.udf.cs.solidbank.dto;

public record DadosAtualizacaoCliente(
        Long id,
        String nome,
        String cpf,
        String email,
        String telefone

) {
}
