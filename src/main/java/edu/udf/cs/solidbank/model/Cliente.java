package edu.udf.cs.solidbank.model;

import edu.udf.cs.solidbank.dto.DadosAtualizacaoCliente;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "cliente")
public class Cliente {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String nome;

    @Column(name = "cpf_cnpj", nullable = false, unique = true, length = 14)
    private String cpfCnpj;

    @Column(length = 100)
    private String email;

    @Column(length = 20)
    private String telefone;

    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;

    @Column(nullable = false)
    private Boolean ativo = true;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Conta> contas;

    @PrePersist
    private void onCreate(){
        dataCadastro = LocalDateTime.now();
    }

    public void excluir(){
        this.ativo = false;
    }


}
