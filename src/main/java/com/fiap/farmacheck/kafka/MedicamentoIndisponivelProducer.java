package com.fiap.farmacheck.kafka;

import com.fiap.farmacheck.model.dto.disponibilidade.MedicamentoIndisponivelEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class MedicamentoIndisponivelProducer {

    private static final Logger logger = LoggerFactory.getLogger(MedicamentoIndisponivelProducer.class);

    private final KafkaTemplate<String, MedicamentoIndisponivelEvent> kafkaTemplate;

    @Value("${farmacheck.kafka.topic.medicamento-indisponivel}")
    private String topic;

    public MedicamentoIndisponivelProducer(KafkaTemplate<String, MedicamentoIndisponivelEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void enviar(MedicamentoIndisponivelEvent event) {
        logger.info("Enviando evento de medicamento indisponivel para Kafka: {}", event);
        kafkaTemplate.send(topic, event.getNomeMedicamento(), event);
    }
}
