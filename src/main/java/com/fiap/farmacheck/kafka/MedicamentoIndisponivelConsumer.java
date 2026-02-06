package com.fiap.farmacheck.kafka;

import com.fiap.farmacheck.model.dto.disponibilidade.MedicamentoIndisponivelEvent;
import com.fiap.farmacheck.model.entity.Estoque;
import com.fiap.farmacheck.repository.EstoqueRepository;
import com.fiap.farmacheck.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MedicamentoIndisponivelConsumer {

    private static final Logger logger = LoggerFactory.getLogger(MedicamentoIndisponivelConsumer.class);

    private final EstoqueRepository estoqueRepository;
    private final EmailService emailService;

    public MedicamentoIndisponivelConsumer(EstoqueRepository estoqueRepository, EmailService emailService) {
        this.estoqueRepository = estoqueRepository;
        this.emailService = emailService;
    }

    @KafkaListener(topics = "${farmacheck.kafka.topic.medicamento-indisponivel}", groupId = "farmacheck-group")
    public void consumir(MedicamentoIndisponivelEvent event) {
        logger.info("Mensagem recebida da fila Kafka: {}", event);

        List<Estoque> estoques = estoqueRepository.findByMedicamentoNomeIgnoreCaseAndQuantidadeGreaterThan(
                event.getNomeMedicamento(), 0);

        if (!estoques.isEmpty()) {
            logger.info("Medicamento '{}' agora esta disponivel no estoque! Enviando email para: {}",
                    event.getNomeMedicamento(), event.getEmailUsuario());
            emailService.enviarNotificacaoDisponibilidade(
                    event.getEmailUsuario(),
                    event.getNomeUsuario(),
                    event.getNomeMedicamento()
            );
        } else {
            logger.warn("Medicamento '{}' ainda NAO esta disponivel. Pesquisa feita por: {} ({})",
                    event.getNomeMedicamento(), event.getNomeUsuario(), event.getEmailUsuario());
        }
    }
}
