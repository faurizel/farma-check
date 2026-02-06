package com.fiap.farmacheck.repository;

import com.fiap.farmacheck.model.entity.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicamentoRepository extends JpaRepository<Medicamento, Integer> {
}
