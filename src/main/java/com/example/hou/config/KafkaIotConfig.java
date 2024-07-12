package com.example.hou.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaIotConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String producerBootstrapServers;


    @Bean
    public Map<String, Object> producerIotConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, producerBootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<String, String> producerIotFactory() {
        return new DefaultKafkaProducerFactory<>(producerIotConfigs());
    }

    /**
     * 自定义的测试环境kafka-template
     *
     * @return
     */
    @Bean(name = "kafkaTemplateIot")
    public KafkaTemplate kafkaTemplateIot() {
        return new KafkaTemplate(producerIotFactory());
    }

}

