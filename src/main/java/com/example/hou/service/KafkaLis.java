package com.example.hou.service;


import org.springframework.stereotype.Component;

import org.springframework.kafka.annotation.KafkaListener;

@Component
public class KafkaLis {

    @KafkaListener(topics = "test_topic", groupId = "test_group")
    public void receiveMessage(String message) {
        System.out.println("Received message: " + message);
    }
}

