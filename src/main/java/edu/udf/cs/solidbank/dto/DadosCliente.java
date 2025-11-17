package edu.udf.cs.solidbank.dto;

import edu.udf.cs.solidbank.model.Cliente;

import java.time.LocalDateTime;

public record DadosCliente(
        Long id,
        String nome,
        String cpfCnpj,
        String email,
        String telefone,
        LocalDateTime dataCadastro,
        Boolean ativo
) {
    public DadosCliente(Cliente cliente){
        this(cliente.getId(), cliente.getNome(), cliente.getCpfCnpj(), cliente.getEmail(),
                cliente.getTelefone(), cliente.getDataCadastro(), cliente.getAtivo());
    }

}
