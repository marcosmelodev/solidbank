package edu.udf.cs.solidbank.service;

import edu.udf.cs.solidbank.dto.DadosCliente;
import edu.udf.cs.solidbank.model.Cliente;
import edu.udf.cs.solidbank.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * SERVICE: ClienteService
 * Gerencia operações relacionadas a clientes
 *
 * Responsabilidades:
 * - Criar novos clientes
 * - Atualizar dados cadastrais
 * - Buscar clientes
 * - Validar CPF/CNPJ único
 * - Ativar/desativar clientes
 */
@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;


    @Transactional
    public DadosCliente criarCliente(DadosCliente dados){
        // Validar se CPF/CNPJ já existe
        if (clienteRepository.existsByCpfCnpj(dados.cpfCnpj())){
            throw new RuntimeException("CPF/CNPJ já cadstrado");
        }
        // Criar cliente
        Cliente cliente = Cliente.builder()
                .nome(dados.nome())
                .cpfCnpj(dados.cpfCnpj())
                .email(dados.email())
                .telefone(dados.telefone())
                .ativo(true)
                .build();

        cliente = clienteRepository.save(cliente);
        return converterParaDTO(cliente);
    }

    public DadosCliente buscarPorId(Long id){
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Cliente não encontrado"));
        return converterParaDTO(cliente);
    }

    public List<DadosCliente> listarTodosAtivos(){
        return clienteRepository.findByAtivoTrue()
                .stream()
                .map(DadosCliente::new)
                .toList();
    }

    public DadosCliente buscarPorCpfCnpj(String cpfCnpj){
        Cliente cliente = clienteRepository.findByCpfCnpj(cpfCnpj)
                .orElseThrow(()-> new RuntimeException("Cliente não encontrado"));
        return converterParaDTO(cliente);
    }

    private DadosCliente converterParaDTO(Cliente cliente){
        return new DadosCliente(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpfCnpj(),
                cliente.getEmail(),
                cliente.getTelefone(),
                cliente.getDataCadastro(),
                cliente.getAtivo()
        );
    }

    public DadosCliente atualizarCliente(Long id, DadosCliente dados) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        cliente.setNome(dados.nome());
        cliente.setCpfCnpj(dados.cpfCnpj());
        cliente.setEmail(dados.email());
        cliente.setTelefone(dados.telefone());

        clienteRepository.save(cliente);
        return new DadosCliente(cliente);

    }

    public void excluir(Long id) {
        var cliente = clienteRepository.getReferenceById(id);
        cliente.excluir();
    }
}
