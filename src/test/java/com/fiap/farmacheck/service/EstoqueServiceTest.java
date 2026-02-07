package com.fiap.farmacheck.service;

import com.fiap.farmacheck.exception.ResourceNotFoundException;
import com.fiap.farmacheck.mapper.EstoqueMapper;
import com.fiap.farmacheck.model.dto.estoque.EstoqueRequestDTO;
import com.fiap.farmacheck.model.dto.estoque.EstoqueResponseDTO;
import com.fiap.farmacheck.model.entity.Estoque;
import com.fiap.farmacheck.repository.EstoqueRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstoqueServiceTest {

    @Mock
    private EstoqueRepository estoqueRepository;

    @Mock
    private EstoqueMapper estoqueMapper;

    @InjectMocks
    private EstoqueService estoqueService;

    @Test
    void criar_deveMapearSalvarERetornarResponse() {
        EstoqueRequestDTO dto = new EstoqueRequestDTO(100, 1, 1);
        Estoque entity = new Estoque();
        entity.setId(1);
        entity.setQuantidade(100);
        EstoqueResponseDTO responseDTO = new EstoqueResponseDTO(1, 100, null, null, null);

        when(estoqueMapper.toEntity(dto)).thenReturn(entity);
        when(estoqueRepository.save(entity)).thenReturn(entity);
        when(estoqueMapper.toResponse(entity)).thenReturn(responseDTO);

        EstoqueResponseDTO result = estoqueService.criar(dto);

        assertEquals(1, result.id());
        assertEquals(100, result.quantidade());
        verify(estoqueMapper).toEntity(dto);
        verify(estoqueRepository).save(entity);
        verify(estoqueMapper).toResponse(entity);
    }

    @Test
    void listarTodos_deveRetornarListaDeEstoques() {
        Estoque estoque1 = new Estoque();
        estoque1.setId(1);
        Estoque estoque2 = new Estoque();
        estoque2.setId(2);
        EstoqueResponseDTO resp1 = new EstoqueResponseDTO(1, 50, null, null, null);
        EstoqueResponseDTO resp2 = new EstoqueResponseDTO(2, 30, null, null, null);

        when(estoqueRepository.findAll()).thenReturn(List.of(estoque1, estoque2));
        when(estoqueMapper.toResponse(estoque1)).thenReturn(resp1);
        when(estoqueMapper.toResponse(estoque2)).thenReturn(resp2);

        List<EstoqueResponseDTO> result = estoqueService.listarTodos();

        assertEquals(2, result.size());
        assertEquals(1, result.get(0).id());
        assertEquals(2, result.get(1).id());
    }

    @Test
    void listarTodos_quandoVazio_deveRetornarListaVazia() {
        when(estoqueRepository.findAll()).thenReturn(Collections.emptyList());

        List<EstoqueResponseDTO> result = estoqueService.listarTodos();

        assertTrue(result.isEmpty());
    }

    @Test
    void buscarPorId_quandoExiste_deveRetornarEstoque() {
        Estoque estoque = new Estoque();
        estoque.setId(1);
        EstoqueResponseDTO responseDTO = new EstoqueResponseDTO(1, 100, null, null, null);

        when(estoqueRepository.findById(1)).thenReturn(Optional.of(estoque));
        when(estoqueMapper.toResponse(estoque)).thenReturn(responseDTO);

        EstoqueResponseDTO result = estoqueService.buscarPorId(1);

        assertEquals(1, result.id());
        verify(estoqueRepository).findById(1);
    }

    @Test
    void buscarPorId_quandoNaoExiste_deveLancarResourceNotFoundException() {
        when(estoqueRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> estoqueService.buscarPorId(99));
    }

    @Test
    void atualizar_quandoExiste_deveAtualizarERetornar() {
        Estoque estoque = new Estoque();
        estoque.setId(1);
        estoque.setQuantidade(50);
        EstoqueRequestDTO dto = new EstoqueRequestDTO(200, 1, 1);
        EstoqueResponseDTO responseDTO = new EstoqueResponseDTO(1, 200, null, null, null);

        when(estoqueRepository.findById(1)).thenReturn(Optional.of(estoque));
        when(estoqueRepository.save(estoque)).thenReturn(estoque);
        when(estoqueMapper.toResponse(estoque)).thenReturn(responseDTO);

        EstoqueResponseDTO result = estoqueService.atualizar(1, dto);

        assertEquals(200, result.quantidade());
        verify(estoqueMapper).updateEntityFromDto(dto, estoque);
        verify(estoqueRepository).save(estoque);
    }

    @Test
    void atualizar_quandoNaoExiste_deveLancarResourceNotFoundException() {
        EstoqueRequestDTO dto = new EstoqueRequestDTO(200, 1, 1);
        when(estoqueRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> estoqueService.atualizar(99, dto));
    }

    @Test
    void deletar_quandoExiste_deveDeletar() {
        Estoque estoque = new Estoque();
        estoque.setId(1);

        when(estoqueRepository.findById(1)).thenReturn(Optional.of(estoque));

        estoqueService.deletar(1);

        verify(estoqueRepository).delete(estoque);
    }

    @Test
    void deletar_quandoNaoExiste_deveLancarResourceNotFoundException() {
        when(estoqueRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> estoqueService.deletar(99));
    }
}
