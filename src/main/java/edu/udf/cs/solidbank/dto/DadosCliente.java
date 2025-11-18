package edu.udf.cs.solidbank.dto;

import edu.udf.cs.solidbank.model.Cliente;

import java.time.LocalDateTime;

/**
 * DTO: ClienteDTO (Record)
 * Transfere dados de cliente entre camadas
 * Evita expor entidade JPA diretamente
 *
 * Records são IMUTÁVEIS - perfeitos para DTOs!
 * Geram automaticamente:
 * - Construtor com todos os parâmetros
 * - Getters (sem prefixo "get"): clienteDTO.nome() ao invés de .getNome()
 * - equals(), hashCode(), toString()
 */
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
