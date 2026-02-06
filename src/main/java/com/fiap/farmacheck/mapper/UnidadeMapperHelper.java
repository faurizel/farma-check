package com.fiap.farmacheck.mapper;

import com.fiap.farmacheck.model.entity.Unidade;
import com.fiap.farmacheck.repository.UnidadeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UnidadeMapperHelper {

    private final UnidadeRepository repository;

    public UnidadeMapperHelper(UnidadeRepository repository) {
        this.repository = repository;
    }

    public Unidade map(Integer id) {
        if (id == null) return null;
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Unidade não encontrada com ID: " + id));
    }
}