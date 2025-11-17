package edu.udf.cs.solidbank.repository;

import edu.udf.cs.solidbank.model.TipoConta;
import edu.udf.cs.solidbank.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {
    Optional<Conta> findByNumeroConta(String numeroConta);
    List<Conta> findByClienteId(Long clienteId);
    List<Conta> findAll();
    List<Conta> findByAtivoTrue();

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
            "FROM Conta c WHERE c.cliente.id = :clienteId AND c.tipoConta = :tipoConta")
    boolean existsByClienteIdAndTipoConta(Long clienteId, TipoConta tipoConta);

    Optional<Conta> findByClienteIdAndTipoConta(Long clienteId, TipoConta tipoConta);
}
