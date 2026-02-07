package com.fiap.farmacheck.service;

import com.fiap.farmacheck.kafka.MedicamentoIndisponivelProducer;
import com.fiap.farmacheck.model.dto.disponibilidade.DisponibilidadeResponseDTO;
import com.fiap.farmacheck.model.dto.disponibilidade.MedicamentoIndisponivelEvent;
import com.fiap.farmacheck.model.entity.Estoque;
import com.fiap.farmacheck.repository.EstoqueRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DisponibilidadeServiceTest {

    @Mock
    private EstoqueRepository estoqueRepository;

    @Mock
    private MedicamentoIndisponivelProducer producer;

    @InjectMocks
    private DisponibilidadeService disponibilidadeService;

    @Test
    void verificarDisponibilidade_quandoMedicamentoDisponivel_deveRetornarDisponivel() {
        Estoque estoque = new Estoque();
        estoque.setQuantidade(10);

        when(estoqueRepository.findByMedicamentoNomeIgnoreCaseAndQuantidadeGreaterThan("Paracetamol", 0))
                .thenReturn(List.of(estoque));

        DisponibilidadeResponseDTO response = disponibilidadeService.verificarDisponibilidade(
                "Paracetamol", "user@test.com", "User Test");

        assertTrue(response.disponivel());
        assertEquals("Paracetamol", response.nomeMedicamento());
        assertNotNull(response.mensagem());
        verify(producer, never()).enviar(any(MedicamentoIndisponivelEvent.class));
    }

    @Test
    void verificarDisponibilidade_quandoMedicamentoIndisponivel_deveRetornarIndisponivelEEnviarKafka() {
        when(estoqueRepository.findByMedicamentoNomeIgnoreCaseAndQuantidadeGreaterThan("Amoxicilina", 0))
                .thenReturn(Collections.emptyList());

        DisponibilidadeResponseDTO response = disponibilidadeService.verificarDisponibilidade(
                "Amoxicilina", "user@test.com", "User Test");

        assertFalse(response.disponivel());
        assertEquals("Amoxicilina", response.nomeMedicamento());
        assertNotNull(response.mensagem());
        verify(producer, times(1)).enviar(any(MedicamentoIndisponivelEvent.class));
    }

    @Test
    void verificarDisponibilidade_quandoMedicamentoIndisponivel_deveEnviarEventoComDadosCorretos() {
        when(estoqueRepository.findByMedicamentoNomeIgnoreCaseAndQuantidadeGreaterThan("Ibuprofeno", 0))
                .thenReturn(Collections.emptyList());

        disponibilidadeService.verificarDisponibilidade("Ibuprofeno", "maria@test.com", "Maria Silva");

        verify(producer).enviar(argThat(event ->
                "Ibuprofeno".equals(event.getNomeMedicamento()) &&
                "maria@test.com".equals(event.getEmailUsuario()) &&
                "Maria Silva".equals(event.getNomeUsuario()) &&
                event.getDataPesquisa() != null
        ));
    }
}
