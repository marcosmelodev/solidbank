package edu.udf.cs.solidbank.service;

import edu.udf.cs.solidbank.dto.DadosConta;
import edu.udf.cs.solidbank.model.Cliente;
import edu.udf.cs.solidbank.model.Conta;
import edu.udf.cs.solidbank.model.TipoConta;
import edu.udf.cs.solidbank.repository.ClienteRepository;
import edu.udf.cs.solidbank.repository.ContaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContaService {

    private final ContaRepository contaRepository;
    private final ClienteRepository clienteRepository;

    @Transactional
    public DadosConta abrirConta(Long clienteId, TipoConta tipoConta){
        //Validar se o cliente existe
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(()-> new RuntimeException("Cliente não encontrado"));

        //Validar se o cliente já possui conta deste tipo
        if (contaRepository.existsByClienteIdAndTipoConta(clienteId, tipoConta)){
            throw  new RuntimeException(("Cliente já possui uma conta " + tipoConta.getDescricao()));
        }

        //Deve gerar o número da conta
        String numeroConta = gerarNumeroConta(tipoConta);

        Conta conta = Conta.builder()
                .numeroConta(numeroConta)
                .cliente(cliente)
                .tipoConta(tipoConta)
                .saldo(BigDecimal.ZERO)
                .ativo(true)
                .build();

        conta = contaRepository.save(conta);
        return converterParaDTO(conta);
    }

    public DadosConta buscarPorNumeroConta(String numeroConta){
        Conta conta = contaRepository.findByNumeroConta(numeroConta)
                .orElseThrow(()-> new RuntimeException("Conta não encontrada"));
        return converterParaDTO(conta);
    }

    public List<DadosConta> listarContasDoCliente(Long clienteId) {
        return contaRepository.findByClienteId(clienteId).stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public BigDecimal consultarSaldo(String numeroConta){
        Conta conta = contaRepository.findByNumeroConta(numeroConta)
                .orElseThrow(()-> new RuntimeException("Conta não encontrada"));
        return  conta.getSaldo();
    }

    private String gerarNumeroConta(TipoConta tipoConta){
        String prefixo = switch (tipoConta){
            case CORRENTE -> "CC";
            case POUPANCA -> "CP";
            case JURIDICA -> "CJ";
        };

        long count = contaRepository.count() + 1;
        return String.format("%s%06d", prefixo, count);
    }

    private DadosConta converterParaDTO(Conta conta){
        return new DadosConta(
                conta.getId(),
                conta.getNumeroConta(),
                conta.getCliente().getId(),
                conta.getCliente().getNome(),
                conta.getTipoConta().name(),
                conta.getSaldo(),
                conta.getDataAbertura(),
                conta.getAtivo()
        );
    }

    public List<DadosConta> listar() {
        return contaRepository.findAll()
                .stream()
                .map(DadosConta::new)
                .toList();
    }

    public void fecharConta(Long clienteId, String tipoConta){
        //pegar todas as contas do cliente
        var contas = contaRepository.findByClienteId(clienteId);

        //filtrar pelo tipo de conta
        var conta = contas.stream()
                .filter(c-> c.getTipoConta() == TipoConta.tipoConta(tipoConta))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        conta.setAtivo(false);
        contaRepository.save(conta);
    }

}
