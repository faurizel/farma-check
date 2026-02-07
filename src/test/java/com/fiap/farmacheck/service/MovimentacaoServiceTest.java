package com.fiap.farmacheck.service;

import com.fiap.farmacheck.exception.ResourceNotFoundException;
import com.fiap.farmacheck.mapper.MovimentacaoMapper;
import com.fiap.farmacheck.model.dto.movimentacao.MovimentacaoRequestDTO;
import com.fiap.farmacheck.model.dto.movimentacao.MovimentacaoResponseDTO;
import com.fiap.farmacheck.model.entity.Movimentacao;
import com.fiap.farmacheck.model.enums.TipoMovimentacao;
import com.fiap.farmacheck.repository.MovimentacaoRepository;
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
class MovimentacaoServiceTest {

    @Mock
    private MovimentacaoRepository movimentacaoRepository;

    @Mock
    private MovimentacaoMapper movimentacaoMapper;

    @InjectMocks
    private MovimentacaoService movimentacaoService;

    @Test
    void criar_deveMapearSalvarERetornarResponse() {
        MovimentacaoRequestDTO dto = new MovimentacaoRequestDTO(50, TipoMovimentacao.ENTRADA, 1, 1);
        Movimentacao entity = new Movimentacao();
        entity.setId(1);
        entity.setQuantidade(50);
        MovimentacaoResponseDTO responseDTO = new MovimentacaoResponseDTO(1, 50, TipoMovimentacao.ENTRADA, null, null, null);

        when(movimentacaoMapper.toEntity(dto)).thenReturn(entity);
        when(movimentacaoRepository.save(entity)).thenReturn(entity);
        when(movimentacaoMapper.toResponse(entity)).thenReturn(responseDTO);

        MovimentacaoResponseDTO result = movimentacaoService.criar(dto);

        assertEquals(1, result.id());
        assertEquals(50, result.quantidade());
        assertEquals(TipoMovimentacao.ENTRADA, result.tipoMovimentacao());
        verify(movimentacaoMapper).toEntity(dto);
        verify(movimentacaoRepository).save(entity);
    }

    @Test
    void listarTodos_deveRetornarListaDeMovimentacoes() {
        Movimentacao mov1 = new Movimentacao();
        mov1.setId(1);
        Movimentacao mov2 = new Movimentacao();
        mov2.setId(2);
        MovimentacaoResponseDTO resp1 = new MovimentacaoResponseDTO(1, 50, TipoMovimentacao.ENTRADA, null, null, null);
        MovimentacaoResponseDTO resp2 = new MovimentacaoResponseDTO(2, 30, TipoMovimentacao.SAIDA, null, null, null);

        when(movimentacaoRepository.findAll()).thenReturn(List.of(mov1, mov2));
        when(movimentacaoMapper.toResponse(mov1)).thenReturn(resp1);
        when(movimentacaoMapper.toResponse(mov2)).thenReturn(resp2);

        List<MovimentacaoResponseDTO> result = movimentacaoService.listarTodos();

        assertEquals(2, result.size());
    }

    @Test
    void listarTodos_quandoVazio_deveRetornarListaVazia() {
        when(movimentacaoRepository.findAll()).thenReturn(Collections.emptyList());

        List<MovimentacaoResponseDTO> result = movimentacaoService.listarTodos();

        assertTrue(result.isEmpty());
    }

    @Test
    void buscarPorId_quandoExiste_deveRetornarMovimentacao() {
        Movimentacao mov = new Movimentacao();
        mov.setId(1);
        MovimentacaoResponseDTO resp = new MovimentacaoResponseDTO(1, 50, TipoMovimentacao.ENTRADA, null, null, null);

        when(movimentacaoRepository.findById(1)).thenReturn(Optional.of(mov));
        when(movimentacaoMapper.toResponse(mov)).thenReturn(resp);

        MovimentacaoResponseDTO result = movimentacaoService.buscarPorId(1);

        assertEquals(1, result.id());
    }

    @Test
    void buscarPorId_quandoNaoExiste_deveLancarResourceNotFoundException() {
        when(movimentacaoRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> movimentacaoService.buscarPorId(99));
    }
}
