package com.fiap.farmacheck.service;

import com.fiap.farmacheck.exception.ResourceNotFoundException;
import com.fiap.farmacheck.mapper.MedicamentoMapper;
import com.fiap.farmacheck.model.dto.medicamento.MedicamentoRequestDTO;
import com.fiap.farmacheck.model.dto.medicamento.MedicamentoResponseDTO;
import com.fiap.farmacheck.model.entity.Medicamento;
import com.fiap.farmacheck.repository.MedicamentoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicamentoService {

    private static final Logger logger = LoggerFactory.getLogger(MedicamentoService.class);

    private final MedicamentoRepository medicamentoRepository;
    private final MedicamentoMapper medicamentoMapper;

    public MedicamentoService(MedicamentoRepository medicamentoRepository, MedicamentoMapper medicamentoMapper) {
        this.medicamentoRepository = medicamentoRepository;
        this.medicamentoMapper = medicamentoMapper;
    }

    public MedicamentoResponseDTO criar(MedicamentoRequestDTO dto) {
        Medicamento entity = medicamentoMapper.toEntity(dto);
        Medicamento salvo = medicamentoRepository.save(entity);
        logger.info("Medicamento cadastrado: {}", salvo.getNome());
        return medicamentoMapper.toResponse(salvo);
    }

    public List<MedicamentoResponseDTO> listarTodos() {
        return medicamentoRepository.findAll()
                .stream()
                .map(medicamentoMapper::toResponse)
                .toList();
    }

    public MedicamentoResponseDTO buscarPorId(Integer id) {
        Medicamento medicamento = medicamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicamento nao encontrado com ID: " + id));
        return medicamentoMapper.toResponse(medicamento);
    }

    public MedicamentoResponseDTO atualizar(Integer id, MedicamentoRequestDTO dto) {
        Medicamento medicamento = medicamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicamento nao encontrado com ID: " + id));
        medicamentoMapper.updateEntityFromDto(dto, medicamento);
        Medicamento atualizado = medicamentoRepository.save(medicamento);
        logger.info("Medicamento atualizado: {}", atualizado.getNome());
        return medicamentoMapper.toResponse(atualizado);
    }

    public void deletar(Integer id) {
        Medicamento medicamento = medicamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicamento nao encontrado com ID: " + id));
        medicamentoRepository.delete(medicamento);
        logger.info("Medicamento deletado: {}", medicamento.getNome());
    }
}
