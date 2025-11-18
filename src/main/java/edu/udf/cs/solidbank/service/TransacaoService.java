package edu.udf.cs.solidbank.service;

import edu.udf.cs.solidbank.dto.DadosDeposito;
import edu.udf.cs.solidbank.dto.DadosSaque;
import edu.udf.cs.solidbank.dto.DadosTransacao;
import edu.udf.cs.solidbank.model.Conta;
import edu.udf.cs.solidbank.model.TipoTransacao;
import edu.udf.cs.solidbank.model.Transacao;
import edu.udf.cs.solidbank.repository.ContaRepository;
import edu.udf.cs.solidbank.repository.TransacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SERVICE: TransacaoService
 * Gerencia todas as transações bancárias
 *
 * Responsabilidades:
 * - Realizar depósitos
 * - Realizar saques (com validação de saldo)
 * - Realizar transferências entre contas
 * - Registrar histórico de transações
 * - Gerar extratos
 *
 */
@Service
@RequiredArgsConstructor
public class TransacaoService {

    private final ContaRepository contaRepository;
    private final TransacaoRepository transacaoRepository;

    @Transactional
    public DadosTransacao depositar(DadosDeposito request) {
        Conta conta = contaRepository.findByNumeroConta(request.numeroConta())
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        if (request.valor().compareTo(BigDecimal.ZERO) <= 0){
            throw new RuntimeException("O valor deve ser positivo");
        }

        //atualizar saldo
        conta.setSaldo(conta.getSaldo().add(request.valor()));

        //Registrar transação
        Transacao transacao = Transacao.builder()
                .contaDestino(conta)
                .tipoTransacao(TipoTransacao.DEPOSITO)
                .valor(request.valor())
                //.descricao(request.descricao)
                .build();

        transacao = transacaoRepository.save(transacao);
        return converterParaDTO(transacao);
    }

    /**
     * Realiza um saque de uma conta
     * Valida se há saldo suficiente
     */
    @Transactional
    public DadosTransacao sacar(DadosSaque request) {
        Conta conta = contaRepository.findByNumeroConta(request.numeroConta())
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        if (request.valor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Valor deve ser positivo");
        }

        if (conta.getSaldo().compareTo(request.valor()) < 0) {
            throw new RuntimeException("Saldo insuficiente");
        }

        // Atualizar saldo
        conta.setSaldo(conta.getSaldo().subtract(request.valor()));
        contaRepository.save(conta);

        // Registrar transação
        Transacao transacao = Transacao.builder()
                .contaOrigem(conta)
                .tipoTransacao(TipoTransacao.SAQUE)
                .valor(request.valor())
                .descricao(request.descricao())
                .build();

        transacao = transacaoRepository.save(transacao);
        return converterParaDTO(transacao);
    }

    /**
     * Realiza transferência entre contas
     * Operação atômica: ou ambas as operações ocorrem ou nenhuma
     */
    @Transactional
    public DadosTransacao transferir(DadosTransacao request) {
        Conta contaOrigem = contaRepository.findByNumeroConta(request.numeroContaOrigem())
                .orElseThrow(() -> new RuntimeException("Conta origem não encontrada"));

        Conta contaDestino = contaRepository.findByNumeroConta(request.numeroContaDestino())
                .orElseThrow(() -> new RuntimeException("Conta destino não encontrada"));

        if (request.valor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Valor deve ser positivo");
        }

        if (contaOrigem.getSaldo().compareTo(request.valor()) < 0) {
            throw new RuntimeException("Saldo insuficiente");
        }

        // Debitar da origem
        contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(request.valor()));

        // Creditar no destino
        contaDestino.setSaldo(contaDestino.getSaldo().add(request.valor()));

        contaRepository.save(contaOrigem);
        contaRepository.save(contaDestino);

        // Registrar transação
        Transacao transacao = Transacao.builder()
                .contaOrigem(contaOrigem)
                .contaDestino(contaDestino)
                .tipoTransacao(TipoTransacao.TRANSFERENCIA)
                .valor(request.valor())
                .descricao(request.descricao())
                .build();

        transacao = transacaoRepository.save(transacao);
        return converterParaDTO(transacao);
    }

    /**
     * Gera extrato de uma conta
     */
    public List<DadosTransacao> gerarExtrato(String numeroConta) {
        Conta conta = contaRepository.findByNumeroConta(numeroConta)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        return transacaoRepository
                .findByContaOrigemIdOrContaDestinoIdOrderByDataTransacaoDesc(
                        conta.getId(), conta.getId())
                .stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    private DadosTransacao converterParaDTO(Transacao transacao) {
        // RECORD: usar construtor direto
        return new DadosTransacao(
                transacao.getId(),
                transacao.getContaOrigem() != null ? transacao.getContaOrigem().getNumeroConta() : null,
                transacao.getContaDestino() != null ? transacao.getContaDestino().getNumeroConta() : null,
                transacao.getTipoTransacao().name(),
                transacao.getValor(),
                transacao.getDataTransacao(),
                transacao.getDescricao()
        );
    }
}
