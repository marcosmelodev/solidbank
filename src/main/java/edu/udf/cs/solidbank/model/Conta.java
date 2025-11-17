package edu.udf.cs.solidbank.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EqualsAndHashCode
@Table(name = "conta", uniqueConstraints = @UniqueConstraint(columnNames = {"cliente_id", "tipo_conta"}))
public class Conta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_conta", nullable = false, unique = true, length = 20)
    private String numeroConta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_conta", nullable = false)
    private TipoConta tipoConta;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal saldo = BigDecimal.ZERO;

    @Column(name = "data_abertura")
    private LocalDateTime dataAbertura;

    @Column(nullable = false)
    private Boolean ativo = true;

    @OneToMany(mappedBy = "contaOrigem", cascade = CascadeType.ALL)
    private List<Transacao> transacaosOrigem;

    @OneToMany(mappedBy = "contaDestino", cascade = CascadeType.ALL)
    private List<Transacao> transacoesDestino;

    @PrePersist
    protected void onCreate(){
        dataAbertura = LocalDateTime.now();
    }


    public Boolean getAtiva() {
       return this.ativo = ativo;
    }

    public void excluir(){
        this.ativo = false;
    }
}
