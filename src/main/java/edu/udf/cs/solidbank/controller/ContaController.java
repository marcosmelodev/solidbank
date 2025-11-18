package edu.udf.cs.solidbank.controller;

import edu.udf.cs.solidbank.dto.DadosCliente;
import edu.udf.cs.solidbank.dto.DadosConta;
import edu.udf.cs.solidbank.model.TipoConta;
import edu.udf.cs.solidbank.service.ContaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * CONTROLLER: ContaController
 * Expõe endpoints REST para operações de conta
 *
 * Endpoints:
 * POST   /api/contas/abrir         - Abrir nova conta
 * GET    /api/contas/{numero}      - Buscar por número
 * GET    /api/contas/cliente/{id}  - Listar contas do cliente
 * GET    /api/contas/{numero}/saldo - Consultar saldo
 */
@RestController
@RequestMapping("/api/contas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ContaController {

    private final ContaService contaService;

    @PostMapping("/abrir")
    @Transactional
    public ResponseEntity<DadosConta> abrirConta(@RequestBody DadosConta dados){
        TipoConta tipo = TipoConta.valueOf(dados.tipoConta().toUpperCase());
        return ResponseEntity.ok(contaService.abrirConta(dados.clienteId(), tipo));
    }

    @GetMapping("/{numeroConta}")
    public ResponseEntity<DadosConta> buscarPorNumero(@PathVariable String numeroConta){
        return ResponseEntity.ok(contaService.buscarPorNumeroConta(numeroConta));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<DadosConta>> listarContasDoCliente(@PathVariable Long clienteId){
        return ResponseEntity.ok(contaService.listarContasDoCliente(clienteId));
    }

    @GetMapping
    public ResponseEntity<List<DadosConta>> listarTodos(){
        return ResponseEntity.ok(contaService.listar());
    }

    @DeleteMapping("/cliente/{clienteId}/{tipoConta}")
    @Transactional
    public ResponseEntity<Void> fecharConta(@PathVariable Long clienteId, @PathVariable String tipoConta){
        contaService.fecharConta(clienteId, tipoConta);
        return ResponseEntity.noContent().build();

    }


}
