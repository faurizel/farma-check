package com.fiap.farmacheck.service;

import com.fiap.farmacheck.kafka.MedicamentoIndisponivelProducer;
import com.fiap.farmacheck.model.dto.disponibilidade.DisponibilidadeResponseDTO;
import com.fiap.farmacheck.model.dto.disponibilidade.MedicamentoIndisponivelEvent;
import com.fiap.farmacheck.model.entity.Estoque;
import com.fiap.farmacheck.repository.EstoqueRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DisponibilidadeService {

    private static final Logger logger = LoggerFactory.getLogger(DisponibilidadeService.class);

    private final EstoqueRepository estoqueRepository;
    private final MedicamentoIndisponivelProducer producer;

    public DisponibilidadeService(EstoqueRepository estoqueRepository,
                                  MedicamentoIndisponivelProducer producer) {
        this.estoqueRepository = estoqueRepository;
        this.producer = producer;
    }

    public DisponibilidadeResponseDTO verificarDisponibilidade(String nomeMedicamento, String emailUsuario, String nomeUsuario) {
        List<Estoque> estoques = estoqueRepository.findByMedicamentoNomeIgnoreCaseAndQuantidadeGreaterThan(
                nomeMedicamento, 0);

        if (!estoques.isEmpty()) {
            logger.info("Medicamento '{}' esta disponivel no estoque", nomeMedicamento);
            return new DisponibilidadeResponseDTO(
                    nomeMedicamento,
                    true,
                    "Medicamento disponivel na farmacia publica"
            );
        }

        logger.info("Medicamento '{}' NAO disponivel. Enviando para fila Kafka. Usuario: {}", nomeMedicamento, emailUsuario);

        MedicamentoIndisponivelEvent event = new MedicamentoIndisponivelEvent(
                nomeMedicamento,
                emailUsuario,
                nomeUsuario,
                LocalDateTime.now()
        );
        producer.enviar(event);

        return new DisponibilidadeResponseDTO(
                nomeMedicamento,
                false,
                "Medicamento nao disponivel. Sua pesquisa foi registrada e voce sera notificado quando estiver disponivel."
        );
    }
}
