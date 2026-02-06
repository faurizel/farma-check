package com.fiap.farmacheck.repository;

import com.fiap.farmacheck.model.entity.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MedicamentoRepository extends JpaRepository<Medicamento, Integer> {

    Optional<Medicamento> findByNomeIgnoreCase(String nome);

    List<Medicamento> findByNomeContainingIgnoreCase(String nome);
}
