package edu.udf.cs.solidbank.controller;

import edu.udf.cs.solidbank.dto.DadosDeposito;
import edu.udf.cs.solidbank.dto.DadosSaque;
import edu.udf.cs.solidbank.dto.DadosTransacao;
import edu.udf.cs.solidbank.service.TransacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CONTROLLER: TransacaoController
 * Expõe endpoints REST para operações de transação
 *
 * Endpoints:
 * POST   /api/transacoes/depositar   - Realizar depósito
 * POST   /api/transacoes/sacar       - Realizar saque
 * POST   /api/transacoes/transferir  - Realizar transferência
 * GET    /api/transacoes/extrato/{numero} - Gerar extrato
 */
@RestController
@RequestMapping("/api/transacoes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TransacaoController {
    //Endpoints para operações de transação

    private final TransacaoService transacaoService;

    @PostMapping("/depositar")
    @Transactional
    public ResponseEntity<DadosTransacao> depositar(@RequestBody DadosDeposito request){
        return ResponseEntity.ok(transacaoService.depositar(request));
    }

    @PutMapping("/sacar")
    public ResponseEntity<DadosTransacao> sacar(@RequestBody DadosSaque request) {
        return ResponseEntity.ok(transacaoService.sacar(request));
    }

    @PutMapping("/transferir")
    public ResponseEntity<DadosTransacao> transferir(@RequestBody DadosTransacao dados) {
        return ResponseEntity.ok(transacaoService.transferir(dados));
    }

    @GetMapping("/extrato/{numeroConta}")
    public ResponseEntity<List<DadosTransacao>> gerarExtrato(@PathVariable String numeroConta) {
        return ResponseEntity.ok(transacaoService.gerarExtrato(numeroConta));
    }
}
