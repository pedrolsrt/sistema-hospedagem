package br.pucminas.sistema_hospedagem.service;

import br.pucminas.sistema_hospedagem.dto.ClienteRequestDTO;
import br.pucminas.sistema_hospedagem.exception.RegraDeNegocioException;
import br.pucminas.sistema_hospedagem.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class ClienteServiceTest {

    private ClienteRepository clienteRepository;
    private ClienteService clienteService;

    @BeforeEach
    void setUp() {
        clienteRepository = Mockito.mock(ClienteRepository.class);
        clienteService = new ClienteService(clienteRepository);
    }

    @Test
    void deveLancarExcecaoQuandoCpfJaEstiverCadastrado() {
        ClienteRequestDTO clienteRequestDTO = new ClienteRequestDTO();
        clienteRequestDTO.setNome("Pedro Lucas");
        clienteRequestDTO.setCpf("12345678901");
        clienteRequestDTO.setEndereco("Rua A, 123");
        clienteRequestDTO.setTelefone("31999999999");
        clienteRequestDTO.setEmail("pedro@email.com");

        when(clienteRepository.existsByCpf("12345678901")).thenReturn(true);

        assertThrows(RegraDeNegocioException.class, () -> {
            clienteService.cadastrarCliente(clienteRequestDTO);
        });
    }
}