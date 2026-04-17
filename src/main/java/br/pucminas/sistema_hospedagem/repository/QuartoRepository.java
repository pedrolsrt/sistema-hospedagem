package br.pucminas.sistema_hospedagem.repository;

import br.pucminas.sistema_hospedagem.model.Quarto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuartoRepository extends JpaRepository<Quarto, Long> {
}