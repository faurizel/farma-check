package com.fiap.farmacheck.service;

import com.fiap.farmacheck.exception.ResourceNotFoundException;
import com.fiap.farmacheck.mapper.UnidadeMapper;
import com.fiap.farmacheck.model.dto.unidade.UnidadeRequestDTO;
import com.fiap.farmacheck.model.dto.unidade.UnidadeResponseDTO;
import com.fiap.farmacheck.model.entity.Unidade;
import com.fiap.farmacheck.model.enums.TipoUnidade;
import com.fiap.farmacheck.repository.UnidadeRepository;
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
class UnidadeServiceTest {

    @Mock
    private UnidadeRepository unidadeRepository;

    @Mock
    private UnidadeMapper unidadeMapper;

    @InjectMocks
    private UnidadeService unidadeService;

    @Test
    void criar_deveMapearSalvarERetornarResponse() {
        UnidadeRequestDTO dto = new UnidadeRequestDTO("UBS Vila Mariana", "04101-300", "11999999999", "ubs@test.com", TipoUnidade.UBS);
        Unidade entity = new Unidade();
        entity.setId(1);
        entity.setNome("UBS Vila Mariana");
        UnidadeResponseDTO responseDTO = new UnidadeResponseDTO(1, "UBS Vila Mariana", "04101-300", "11999999999", "ubs@test.com", TipoUnidade.UBS, null);

        when(unidadeMapper.toEntity(dto)).thenReturn(entity);
        when(unidadeRepository.save(entity)).thenReturn(entity);
        when(unidadeMapper.toResponse(entity)).thenReturn(responseDTO);

        UnidadeResponseDTO result = unidadeService.criar(dto);

        assertEquals(1, result.id());
        assertEquals("UBS Vila Mariana", result.nome());
        verify(unidadeMapper).toEntity(dto);
        verify(unidadeRepository).save(entity);
    }

    @Test
    void listarTodos_deveRetornarListaDeUnidades() {
        Unidade unidade = new Unidade();
        unidade.setId(1);
        UnidadeResponseDTO resp = new UnidadeResponseDTO(1, "UBS", "04101-300", "11999999999", "ubs@test.com", TipoUnidade.UBS, null);

        when(unidadeRepository.findAll()).thenReturn(List.of(unidade));
        when(unidadeMapper.toResponse(unidade)).thenReturn(resp);

        List<UnidadeResponseDTO> result = unidadeService.listarTodos();

        assertEquals(1, result.size());
        assertEquals("UBS", result.get(0).nome());
    }

    @Test
    void listarTodos_quandoVazio_deveRetornarListaVazia() {
        when(unidadeRepository.findAll()).thenReturn(Collections.emptyList());

        List<UnidadeResponseDTO> result = unidadeService.listarTodos();

        assertTrue(result.isEmpty());
    }

    @Test
    void buscarPorId_quandoExiste_deveRetornarUnidade() {
        Unidade unidade = new Unidade();
        unidade.setId(1);
        UnidadeResponseDTO resp = new UnidadeResponseDTO(1, "UBS", "04101-300", "11999999999", "ubs@test.com", TipoUnidade.UBS, null);

        when(unidadeRepository.findById(1)).thenReturn(Optional.of(unidade));
        when(unidadeMapper.toResponse(unidade)).thenReturn(resp);

        UnidadeResponseDTO result = unidadeService.buscarPorId(1);

        assertEquals(1, result.id());
    }

    @Test
    void buscarPorId_quandoNaoExiste_deveLancarResourceNotFoundException() {
        when(unidadeRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> unidadeService.buscarPorId(99));
    }

    @Test
    void atualizar_quandoExiste_deveAtualizarERetornar() {
        Unidade unidade = new Unidade();
        unidade.setId(1);
        unidade.setNome("UBS Antiga");
        UnidadeRequestDTO dto = new UnidadeRequestDTO("UBS Nova", "04101-300", "11999999999", "ubs@test.com", TipoUnidade.UBS);
        UnidadeResponseDTO resp = new UnidadeResponseDTO(1, "UBS Nova", "04101-300", "11999999999", "ubs@test.com", TipoUnidade.UBS, null);

        when(unidadeRepository.findById(1)).thenReturn(Optional.of(unidade));
        when(unidadeRepository.save(unidade)).thenReturn(unidade);
        when(unidadeMapper.toResponse(unidade)).thenReturn(resp);

        UnidadeResponseDTO result = unidadeService.atualizar(1, dto);

        assertEquals("UBS Nova", result.nome());
        verify(unidadeMapper).updateEntityFromDto(dto, unidade);
        verify(unidadeRepository).save(unidade);
    }

    @Test
    void atualizar_quandoNaoExiste_deveLancarResourceNotFoundException() {
        UnidadeRequestDTO dto = new UnidadeRequestDTO("UBS", "04101-300", "11999999999", "ubs@test.com", TipoUnidade.UBS);
        when(unidadeRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> unidadeService.atualizar(99, dto));
    }

    @Test
    void deletar_quandoExiste_deveDeletar() {
        Unidade unidade = new Unidade();
        unidade.setId(1);

        when(unidadeRepository.findById(1)).thenReturn(Optional.of(unidade));

        unidadeService.deletar(1);

        verify(unidadeRepository).delete(unidade);
    }

    @Test
    void deletar_quandoNaoExiste_deveLancarResourceNotFoundException() {
        when(unidadeRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> unidadeService.deletar(99));
    }
}
