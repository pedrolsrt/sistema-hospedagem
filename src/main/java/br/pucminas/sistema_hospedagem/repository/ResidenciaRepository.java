package br.pucminas.sistema_hospedagem.repository;

import br.pucminas.sistema_hospedagem.model.Residencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResidenciaRepository extends JpaRepository<Residencia, Long> {
}