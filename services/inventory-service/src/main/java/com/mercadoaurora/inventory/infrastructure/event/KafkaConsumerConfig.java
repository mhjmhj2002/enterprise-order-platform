package com.mercadoaurora.inventory.infrastructure.event;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Profile("kafka")
@EnableConfigurationProperties(KafkaProperties.class)
public class KafkaConsumerConfig {
    @Bean
    ConsumerFactory<String, OrderConfirmedEventEnvelope> orderConfirmedConsumerFactory(KafkaProperties properties) {
        Map<String, Object> consumerProperties = new HashMap<>(properties.buildConsumerProperties(null));
        return new DefaultKafkaConsumerFactory<>(consumerProperties, new StringDeserializer(),
                new ErrorHandlingDeserializer<>(new JsonDeserializer<>(OrderConfirmedEventEnvelope.class, false)));
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, OrderConfirmedEventEnvelope> orderConfirmedKafkaListenerContainerFactory(
            ConsumerFactory<String, OrderConfirmedEventEnvelope> orderConfirmedConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, OrderConfirmedEventEnvelope> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(orderConfirmedConsumerFactory);
        factory.setCommonErrorHandler(new DefaultErrorHandler(new FixedBackOff(1_000L, 2L)));
        return factory;
    }
}
