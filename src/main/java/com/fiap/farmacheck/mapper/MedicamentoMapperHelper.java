package com.fiap.farmacheck.mapper;

import com.fiap.farmacheck.model.entity.Medicamento;
import com.fiap.farmacheck.repository.MedicamentoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class MedicamentoMapperHelper {

    private final MedicamentoRepository repository;

    public MedicamentoMapperHelper(MedicamentoRepository repository) {
        this.repository = repository;
    }

    public Medicamento map(Integer id) {
        if (id == null) return null;
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Medicamento não encontrado com ID: " + id));
    }
}