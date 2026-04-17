package br.pucminas.sistema_hospedagem.service;

import br.pucminas.sistema_hospedagem.dto.ClienteRequestDTO;
import br.pucminas.sistema_hospedagem.dto.ClienteResponseDTO;
import br.pucminas.sistema_hospedagem.dto.LoginRequestDTO;
import br.pucminas.sistema_hospedagem.dto.LoginResponseDTO;
import br.pucminas.sistema_hospedagem.exception.RegraDeNegocioException;
import br.pucminas.sistema_hospedagem.exception.RecursoNaoEncontradoException;
import br.pucminas.sistema_hospedagem.model.Cliente;
import br.pucminas.sistema_hospedagem.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public ClienteResponseDTO cadastrarCliente(ClienteRequestDTO clienteRequestDTO) {
        validarCpfDuplicado(clienteRequestDTO.getCpf());
        validarEmailDuplicado(clienteRequestDTO.getEmail());

        Cliente cliente = converterParaEntidade(clienteRequestDTO);
        Cliente clienteSalvo = clienteRepository.save(cliente);

        return converterParaResponseDTO(clienteSalvo);
    }

    public List<ClienteResponseDTO> listarTodos() {
        return clienteRepository.findAll()
                .stream()
                .map(this::converterParaResponseDTO)
                .toList();
    }

    public ClienteResponseDTO buscarPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado com o id: " + id));

        return converterParaResponseDTO(cliente);
    }

    public ClienteResponseDTO atualizarCliente(Long id, ClienteRequestDTO clienteRequestDTO) {
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado com o id: " + id));

        if (!clienteExistente.getCpf().equals(clienteRequestDTO.getCpf())) {
            validarCpfDuplicado(clienteRequestDTO.getCpf());
        }

        if (!clienteExistente.getEmail().equalsIgnoreCase(clienteRequestDTO.getEmail())) {
            validarEmailDuplicado(clienteRequestDTO.getEmail());
        }

        clienteExistente.setNome(clienteRequestDTO.getNome());
        clienteExistente.setCpf(clienteRequestDTO.getCpf());
        clienteExistente.setEndereco(clienteRequestDTO.getEndereco());
        clienteExistente.setTelefone(clienteRequestDTO.getTelefone());
        clienteExistente.setEmail(clienteRequestDTO.getEmail());
        clienteExistente.setSenha(clienteRequestDTO.getSenha());

        Cliente clienteAtualizado = clienteRepository.save(clienteExistente);
        return converterParaResponseDTO(clienteAtualizado);
    }

    public void excluirCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado com o id: " + id));

        clienteRepository.delete(cliente);
    }

    public LoginResponseDTO autenticar(LoginRequestDTO loginRequestDTO) {
        Cliente cliente = clienteRepository.findByEmailAndSenha(loginRequestDTO.getEmail(), loginRequestDTO.getSenha())
                .orElseThrow(() -> new RegraDeNegocioException("E-mail ou senha inválidos."));

        return new LoginResponseDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getEmail(),
                "Autenticação realizada com sucesso."
        );
    }

    private void validarCpfDuplicado(String cpf) {
        if (clienteRepository.existsByCpf(cpf)) {
            throw new RegraDeNegocioException("Já existe um cliente cadastrado com este CPF.");
        }
    }

    private void validarEmailDuplicado(String email) {
        if (clienteRepository.existsByEmail(email)) {
            throw new RegraDeNegocioException("Já existe um cliente cadastrado com este e-mail.");
        }
    }

    private Cliente converterParaEntidade(ClienteRequestDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setNome(dto.getNome());
        cliente.setCpf(dto.getCpf());
        cliente.setEndereco(dto.getEndereco());
        cliente.setTelefone(dto.getTelefone());
        cliente.setEmail(dto.getEmail());
        cliente.setSenha(dto.getSenha());
        return cliente;
    }

    private ClienteResponseDTO converterParaResponseDTO(Cliente cliente) {
        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpf(),
                cliente.getEndereco(),
                cliente.getTelefone(),
                cliente.getEmail()
        );
    }
}