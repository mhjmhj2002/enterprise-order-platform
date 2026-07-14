package com.mercadoaurora.order.infrastructure.event;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
@Profile("kafka")
@EnableConfigurationProperties(KafkaProperties.class)
public class KafkaProducerConfig {
    @Bean
    ProducerFactory<String, OrderConfirmedEventEnvelope> orderConfirmedProducerFactory(KafkaProperties properties) {
        return new DefaultKafkaProducerFactory<>(properties.buildProducerProperties(null));
    }

    @Bean
    KafkaTemplate<String, OrderConfirmedEventEnvelope> orderConfirmedKafkaTemplate(
            ProducerFactory<String, OrderConfirmedEventEnvelope> orderConfirmedProducerFactory
    ) {
        return new KafkaTemplate<>(orderConfirmedProducerFactory);
    }
}
