package com.fiap.farmacheck.service;

import com.fiap.farmacheck.exception.ResourceNotFoundException;
import com.fiap.farmacheck.mapper.EstoqueMapper;
import com.fiap.farmacheck.model.dto.estoque.EstoqueRequestDTO;
import com.fiap.farmacheck.model.dto.estoque.EstoqueResponseDTO;
import com.fiap.farmacheck.model.entity.Estoque;
import com.fiap.farmacheck.repository.EstoqueRepository;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service

public class EstoqueService {

    private static final Logger logger = LoggerFactory.getLogger(EstoqueService.class);

    private final EstoqueRepository estoqueRepository;
    private final EstoqueMapper estoqueMapper;

    public EstoqueService(EstoqueRepository estoqueRepository, EstoqueMapper estoqueMapper) {
        this.estoqueRepository = estoqueRepository;
        this.estoqueMapper = estoqueMapper;
    }

    public EstoqueResponseDTO criar(EstoqueRequestDTO dto) {
        Estoque entity = estoqueMapper.toEntity(dto);
        Estoque salvo = estoqueRepository.save(entity);
        logger.info("Estoque cadastrado com ID: {}", salvo.getId());
        return estoqueMapper.toResponse(salvo);
    }

    @Transactional(readOnly = true)
    public List<EstoqueResponseDTO> listarTodos() {
        return estoqueRepository.findAll()
                .stream()
                .map(estoqueMapper::toResponse)
                .toList();
    }

    public EstoqueResponseDTO buscarPorId(Integer id) {
        Estoque estoque = estoqueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estoque não encontrado com ID: " + id));
        return estoqueMapper.toResponse(estoque);
    }

    public EstoqueResponseDTO atualizar(Integer id, EstoqueRequestDTO dto) {
        Estoque estoque = estoqueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estoque não encontrado com ID: " + id));
        estoqueMapper.updateEntityFromDto(dto, estoque);
        Estoque atualizado = estoqueRepository.save(estoque);
        logger.info("Estoque atualizado com ID: {}", atualizado.getId());
        return estoqueMapper.toResponse(atualizado);
    }

    public void deletar(Integer id) {
        Estoque estoque = estoqueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estoque não encontrado com ID: " + id));
        estoqueRepository.delete(estoque);
        logger.info("Estoque deletado com ID: {}", estoque.getId());
    }
}
