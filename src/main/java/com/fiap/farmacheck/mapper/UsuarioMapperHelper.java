package com.fiap.farmacheck.mapper;

import com.fiap.farmacheck.model.entity.Usuario;
import com.fiap.farmacheck.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapperHelper {

    private final UsuarioRepository repository;

    public UsuarioMapperHelper(UsuarioRepository repository) {
        this.repository = repository;
    }

    public Usuario map(Integer id) {
        if (id == null) return null;
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com ID: " + id));
    }
}