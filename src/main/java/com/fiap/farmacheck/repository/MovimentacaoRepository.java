package com.fiap.farmacheck.repository;

import com.fiap.farmacheck.model.entity.Movimentacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Integer> {
}
