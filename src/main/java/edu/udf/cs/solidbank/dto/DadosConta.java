package edu.udf.cs.solidbank.dto;

import edu.udf.cs.solidbank.model.Conta;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DadosConta(
        Long id,
        String numeroConta,
        Long clienteId,
        String clienteNome,
        String tipoConta,
        BigDecimal saldo,
        LocalDateTime dataAbertura,
        Boolean ativa
) {
    public DadosConta(Conta conta) {
        this(
                conta.getId(),
                conta.getNumeroConta(),
                conta.getCliente().getId(),
                conta.getCliente().getNome(),
                conta.getTipoConta().name(),
                conta.getSaldo(),
                conta.getDataAbertura(),
                conta.getAtiva()
        );
    }
}
