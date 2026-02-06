package com.fiap.farmacheck.mapper;

import com.fiap.farmacheck.model.entity.Movimentacao;
import com.fiap.farmacheck.repository.MovimentacaoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class MovimentacaoMapperHelper {

    private final MovimentacaoRepository repository;

    public MovimentacaoMapperHelper(MovimentacaoRepository repository) {
        this.repository = repository;
    }

    public Movimentacao map(Integer id) {
        if (id == null) return null;
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Movimentação não encontrada com ID: " + id));
    }
}