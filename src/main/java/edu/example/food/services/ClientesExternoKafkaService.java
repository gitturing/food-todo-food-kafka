package edu.example.food.services;

import edu.example.food.models.Clientes;
import edu.example.food.utilities.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class ClientesExternoKafkaService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendCustomer(String topico, Clientes clientes) {
        var data = JsonUtil.getToJson(clientes);
        var future = kafkaTemplate.send(topico, clientes.nombre(), data);

        future.whenComplete((resultadoEnvio, excepcion) -> {
            if (excepcion != null) {
                log.error(excepcion.getMessage());
                future.completeExceptionally(excepcion);
            } else {
                future.complete(resultadoEnvio);
                log.info("Cliente Externo enviado al topico -> " + topico + " en Kafka " + clientes.nombre());
            }
        });
    }

    private List<Clientes> transformClientesFromAWSSqsToClientes(List<Message> messages) {
        return messages.stream()
                .map(message -> JsonUtil.getFromJson(message.body(), Clientes.class))
                .collect(Collectors.toList());
    }

    public String sendAWSSqsListMessagesToKafka(List<Message> messages, String topico) {
        var clientesSqs = transformClientesFromAWSSqsToClientes(messages);
        for (Clientes clientes : clientesSqs) {
            sendCustomer(topico, clientes);
        }
        return "Se han enviado " + clientesSqs.size() + " Clientes desde AWS SQS hacia Kafka";
    }

}
