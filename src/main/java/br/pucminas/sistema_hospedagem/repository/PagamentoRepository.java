package br.pucminas.sistema_hospedagem.repository;

import br.pucminas.sistema_hospedagem.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
}