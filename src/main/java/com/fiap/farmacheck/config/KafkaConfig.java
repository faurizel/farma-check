package com.fiap.farmacheck.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class KafkaConfig {

    @Value("${farmacheck.kafka.topic.medicamento-indisponivel}")
    private String topicMedicamentoIndisponivel;

    @Bean
    public NewTopic medicamentoIndisponivelTopic() {
        return TopicBuilder.name(topicMedicamentoIndisponivel)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
