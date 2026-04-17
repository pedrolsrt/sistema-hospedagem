package br.pucminas.sistema_hospedagem.repository;

import br.pucminas.sistema_hospedagem.model.Aluguel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AluguelRepository extends JpaRepository<Aluguel, Long> {

    List<Aluguel> findByQuartoIdAndDataEntradaLessThanEqualAndDataSaidaGreaterThanEqual(
            Long quartoId,
            LocalDateTime dataSaida,
            LocalDateTime dataEntrada
    );
}