package edu.udf.cs.solidbank.repository;

import edu.udf.cs.solidbank.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * REPOSITORY: TransacaoRepository
 * Interface de acesso aos dados de Transacao
 *
 * Responsabilidades:
 * - Realizar operações CRUD em Transacao
 * - Buscar transações por conta
 * - Buscar transações por período
 * - Gerar extratos
 */
@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
    List<Transacao> findByContaOrigemIdOrContaDestinoIdOrderByDataTransacaoDesc(
            Long contaOrigemId, Long contaDestinoId);

    List<Transacao> findByContaOrigemIdOrderByDataTransacaoDesc(Long contaId);
    List<Transacao> findByContaDestinoIdOrderByDataTransacaoDesc(Long contaId);

    Transacao save(Transacao transacao);
}
