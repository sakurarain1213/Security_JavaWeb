package com.example.hou.controller;

import com.example.hou.service.KafkaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaMessageController {

    @Autowired
    private KafkaService kafkaService;

    @GetMapping("/send/{message}")
    public String sendMessage(@PathVariable String message) {
        kafkaService.sendMessage(message);
        return "Message sent OK";
    }
}
