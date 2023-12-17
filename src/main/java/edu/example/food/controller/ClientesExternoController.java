package edu.example.food.controller;

import edu.example.food.models.Clientes;
import edu.example.food.models.Topicos;
import edu.example.food.services.ClientesExternoKafkaService;
import edu.example.food.services.ClientesSQSService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.List;

@RequestMapping("/ext/clientes")
@RestController
@RequiredArgsConstructor
public class ClientesExternoController {

    private final ClientesExternoKafkaService clientesExternoKafkaService;
    private final ClientesSQSService clientesSQSService;

    @PostMapping("/kafka")
    public Clientes enviarClienteKafka(@RequestBody Clientes clientes) {
        clientesExternoKafkaService.sendCustomer(String.valueOf(Topicos.CLIENTES_EXTERNOS_KAFKA), clientes);
        return clientes;
    }

    @PostMapping("/sqs")
    public String enviarClientesSQSToKafka() {
        List<Message> awsSqsMessages = clientesSQSService.receiveMessagesFromQueue(10, 10);
        return clientesExternoKafkaService.sendAWSSqsListMessagesToKafka(awsSqsMessages, String.valueOf(Topicos.PRODUCTOS_SQS));
    }

}
