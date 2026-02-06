package com.fiap.farmacheck.repository;

import com.fiap.farmacheck.model.entity.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstoqueRepository extends JpaRepository<Estoque, Integer> {
}
