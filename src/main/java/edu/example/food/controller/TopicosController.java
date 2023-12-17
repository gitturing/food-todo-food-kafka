package edu.example.food.controller;

import edu.example.food.models.Topicos;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/ext/topicos")
@RestController
public class TopicosController {
    @GetMapping("")
    public Topicos[] enviarProductoExternoKafka(){
        return Topicos.values();
    }
}
