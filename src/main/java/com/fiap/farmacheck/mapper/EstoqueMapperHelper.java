package com.fiap.farmacheck.mapper;

import com.fiap.farmacheck.model.entity.Estoque;
import com.fiap.farmacheck.repository.EstoqueRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class EstoqueMapperHelper {

    private final EstoqueRepository repository;

    public EstoqueMapperHelper(EstoqueRepository repository) {
        this.repository = repository;
    }

    public Estoque map(Integer id) {
        if (id == null) return null;
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Estoque não encontrado com ID: " + id));
    }
}