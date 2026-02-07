package com.fiap.farmacheck.repository;

import com.fiap.farmacheck.model.entity.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EstoqueRepository extends JpaRepository<Estoque, Integer> {

    List<Estoque> findByMedicamentoNomeIgnoreCaseAndQuantidadeGreaterThan(String nome, Integer quantidade);
}
