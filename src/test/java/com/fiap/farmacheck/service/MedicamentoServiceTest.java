package com.fiap.farmacheck.service;

import com.fiap.farmacheck.exception.ResourceNotFoundException;
import com.fiap.farmacheck.mapper.MedicamentoMapper;
import com.fiap.farmacheck.model.dto.medicamento.MedicamentoRequestDTO;
import com.fiap.farmacheck.model.dto.medicamento.MedicamentoResponseDTO;
import com.fiap.farmacheck.model.entity.Medicamento;
import com.fiap.farmacheck.repository.MedicamentoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicamentoServiceTest {

    @Mock
    private MedicamentoRepository medicamentoRepository;

    @Mock
    private MedicamentoMapper medicamentoMapper;

    @InjectMocks
    private MedicamentoService medicamentoService;

    @Test
    void criar_deveMapearSalvarRetornarResponse() {
        MedicamentoRequestDTO dto = new MedicamentoRequestDTO("Paracetamol", "Paracetamol", "500mg", "EMS");
        Medicamento entity = new Medicamento();
        entity.setId(1);
        entity.setNome("Paracetamol");
        MedicamentoResponseDTO responseDTO = new MedicamentoResponseDTO(1, "Paracetamol", "Paracetamol", "500mg", "EMS", null);

        when(medicamentoMapper.toEntity(dto)).thenReturn(entity);
        when(medicamentoRepository.save(entity)).thenReturn(entity);
        when(medicamentoMapper.toResponse(entity)).thenReturn(responseDTO);

        MedicamentoResponseDTO result = medicamentoService.criar(dto);

        assertEquals(1, result.id());
        assertEquals("Paracetamol", result.nome());
        verify(medicamentoMapper).toEntity(dto);
        verify(medicamentoRepository).save(entity);
        verify(medicamentoMapper).toResponse(entity);
    }

    @Test
    void listarTodos_deveRetornarLista() {
        Medicamento entity = new Medicamento();
        entity.setId(1);
        MedicamentoResponseDTO resp = new MedicamentoResponseDTO(1, "Ibuprofeno", "Ibuprofeno", "400mg", "Aché", null);
        when(medicamentoRepository.findAll()).thenReturn(List.of(entity));
        when(medicamentoMapper.toResponse(entity)).thenReturn(resp);

        List<MedicamentoResponseDTO> result = medicamentoService.listarTodos();
        assertEquals(1, result.size());
        assertEquals("Ibuprofeno", result.get(0).nome());
    }

    @Test
    void listarTodos_quandoVazio() {
        when(medicamentoRepository.findAll()).thenReturn(Collections.emptyList());
        List<MedicamentoResponseDTO> result = medicamentoService.listarTodos();
        assertTrue(result.isEmpty());
    }

    @Test
    void buscarPorId_quandoExiste() {
        Medicamento entity = new Medicamento();
        entity.setId(1);
        MedicamentoResponseDTO resp = new MedicamentoResponseDTO(1, "Dipirona", "Dipirona", "500mg", "Genérico", null);
        when(medicamentoRepository.findById(1)).thenReturn(Optional.of(entity));
        when(medicamentoMapper.toResponse(entity)).thenReturn(resp);
        MedicamentoResponseDTO result = medicamentoService.buscarPorId(1);
        assertEquals(1, result.id());
        assertEquals("Dipirona", result.nome());
    }

    @Test
    void buscarPorId_quandoNaoExiste() {
        when(medicamentoRepository.findById(55)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> medicamentoService.buscarPorId(55));
    }

    @Test
    void atualizar_quandoExiste() {
        Medicamento entity = new Medicamento();
        entity.setId(1);
        entity.setNome("Amoxilina");
        MedicamentoRequestDTO dto = new MedicamentoRequestDTO("Amoxilina Atualizada", "Amoxilina", "250mg", "EMS");
        MedicamentoResponseDTO resp = new MedicamentoResponseDTO(1, "Amoxilina Atualizada", "Amoxilina", "250mg", "EMS", null);
        when(medicamentoRepository.findById(1)).thenReturn(Optional.of(entity));
        when(medicamentoRepository.save(entity)).thenReturn(entity);
        when(medicamentoMapper.toResponse(entity)).thenReturn(resp);
        MedicamentoResponseDTO result = medicamentoService.atualizar(1, dto);
        assertEquals("Amoxilina Atualizada", result.nome());
        verify(medicamentoMapper).updateEntityFromDto(dto, entity);
        verify(medicamentoRepository).save(entity);
    }

    @Test
    void atualizar_quandoNaoExiste() {
        MedicamentoRequestDTO dto = new MedicamentoRequestDTO("Amoxilina", "Amoxilina", "250mg", "EMS");
        when(medicamentoRepository.findById(55)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> medicamentoService.atualizar(55, dto));
    }

    @Test
    void deletar_quandoExiste() {
        Medicamento entity = new Medicamento();
        entity.setId(1);
        when(medicamentoRepository.findById(1)).thenReturn(Optional.of(entity));
        medicamentoService.deletar(1);
        verify(medicamentoRepository).delete(entity);
    }

    @Test
    void deletar_quandoNaoExiste() {
        when(medicamentoRepository.findById(77)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> medicamentoService.deletar(77));
    }
}
