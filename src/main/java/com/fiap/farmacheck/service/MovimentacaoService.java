package com.fiap.farmacheck.service;

import com.fiap.farmacheck.exception.ResourceNotFoundException;
import com.fiap.farmacheck.mapper.MovimentacaoMapper;
import com.fiap.farmacheck.model.dto.movimentacao.MovimentacaoRequestDTO;
import com.fiap.farmacheck.model.dto.movimentacao.MovimentacaoResponseDTO;
import com.fiap.farmacheck.model.entity.Movimentacao;
import com.fiap.farmacheck.repository.MovimentacaoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovimentacaoService {

    private static final Logger logger = LoggerFactory.getLogger(MovimentacaoService.class);

    private final MovimentacaoRepository movimentacaoRepository;
    private final MovimentacaoMapper movimentacaoMapper;

    public MovimentacaoService(MovimentacaoRepository movimentacaoRepository, MovimentacaoMapper movimentacaoMapper) {
        this.movimentacaoRepository = movimentacaoRepository;
        this.movimentacaoMapper = movimentacaoMapper;
    }

    public MovimentacaoResponseDTO criar(MovimentacaoRequestDTO dto) {
        Movimentacao entity = movimentacaoMapper.toEntity(dto);
        Movimentacao salvo = movimentacaoRepository.save(entity);
        logger.info("Movimentacao cadastrada com ID: {}", salvo.getId());
        return movimentacaoMapper.toResponse(salvo);
    }

    public List<MovimentacaoResponseDTO> listarTodos() {
        return movimentacaoRepository.findAll()
                .stream()
                .map(movimentacaoMapper::toResponse)
                .toList();
    }

    public MovimentacaoResponseDTO buscarPorId(Integer id) {
        Movimentacao movimentacao = movimentacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movimentação não encontrada com ID: " + id));
        return movimentacaoMapper.toResponse(movimentacao);
    }
}
