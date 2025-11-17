package edu.udf.cs.solidbank.repository;

import edu.udf.cs.solidbank.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
    List<Transacao> findByContaOrigemIdOrContaDestinoIdOrderByDataTransacaoDesc(
            Long contaOrigemId, Long contaDestinoId);

    List<Transacao> findByContaOrigemIdOrderByDataTransacaoDesc(Long contaId);
    List<Transacao> findByContaDestinoIdOrderByDataTransacaoDesc(Long contaId);

    Transacao save(Transacao transacao);
}
