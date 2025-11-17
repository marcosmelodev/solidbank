package edu.udf.cs.solidbank.repository;

import edu.udf.cs.solidbank.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByCpfCnpj(String cpfCnpj);
    List<Cliente> findByAtivoTrue();
    boolean existsByCpfCnpj(String cpfCnpj);


}
