package edu.udf.cs.solidbank.controller;

import edu.udf.cs.solidbank.dto.DadosCliente;
import edu.udf.cs.solidbank.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ClienteController {
    //Endpoints REST para operações de cliente

    private final ClienteService clienteService;

    @PostMapping
    public ResponseEntity<DadosCliente> criarCliente(@RequestBody DadosCliente dados){
        return ResponseEntity.ok(clienteService.criarCliente(dados));
    }

    @GetMapping
    public ResponseEntity<List<DadosCliente>> listarTodos(){
        return ResponseEntity.ok(clienteService.listarTodosAtivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosCliente> buscarPorId(@PathVariable Long id){
        return ResponseEntity.ok(clienteService.buscarPorId(id));
    }

    @GetMapping("/cpf/{cpfCnpj}")
    public ResponseEntity<DadosCliente> buscarPorCpf(@PathVariable String cpfCnpj){
        return ResponseEntity.ok(clienteService.buscarPorCpfCnpj(cpfCnpj));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DadosCliente> atualizarCliente(
            @PathVariable Long id,
            @RequestBody DadosCliente dados) {
        return ResponseEntity.ok(clienteService.atualizarCliente(id, dados));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id){
        clienteService.excluir(id);
        return ResponseEntity.noContent().build();
    }

}
