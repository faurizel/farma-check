package com.fiap.farmacheck.service;

import com.fiap.farmacheck.exception.ResourceNotFoundException;
import com.fiap.farmacheck.mapper.UnidadeMapper;
import com.fiap.farmacheck.model.dto.unidade.UnidadeRequestDTO;
import com.fiap.farmacheck.model.dto.unidade.UnidadeResponseDTO;
import com.fiap.farmacheck.model.entity.Unidade;
import com.fiap.farmacheck.repository.UnidadeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnidadeService {

    private static final Logger logger = LoggerFactory.getLogger(UnidadeService.class);

    private final UnidadeRepository unidadeRepository;
    private final UnidadeMapper unidadeMapper;

    public UnidadeService(UnidadeRepository unidadeRepository, UnidadeMapper unidadeMapper) {
        this.unidadeRepository = unidadeRepository;
        this.unidadeMapper = unidadeMapper;
    }

    public UnidadeResponseDTO criar(UnidadeRequestDTO dto) {
        Unidade entity = unidadeMapper.toEntity(dto);
        Unidade salvo = unidadeRepository.save(entity);
        logger.info("Unidade cadastrada: {}", salvo.getNome());
        return unidadeMapper.toResponse(salvo);
    }

    public List<UnidadeResponseDTO> listarTodos() {
        return unidadeRepository.findAll()
                .stream()
                .map(unidadeMapper::toResponse)
                .toList();
    }

    public UnidadeResponseDTO buscarPorId(Integer id) {
        Unidade unidade = unidadeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Unidade não encontrada com ID: " + id));
        return unidadeMapper.toResponse(unidade);
    }

    public UnidadeResponseDTO atualizar(Integer id, UnidadeRequestDTO dto) {
        Unidade unidade = unidadeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Unidade não encontrada com ID: " + id));
        unidadeMapper.updateEntityFromDto(dto, unidade);
        Unidade atualizado = unidadeRepository.save(unidade);
        logger.info("Unidade atualizada: {}", atualizado.getNome());
        return unidadeMapper.toResponse(atualizado);
    }

    public void deletar(Integer id) {
        Unidade unidade = unidadeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Unidade não encontrada com ID: " + id));
        unidadeRepository.delete(unidade);
        logger.info("Unidade deletada: {}", unidade.getNome());
    }
}
