package edu.example.food.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ClientesSQSService {

    @Value("${aws.service-queue-name}")
    private String queue;


    private final SqsClient sqsClient;

    private String getQueueURL() {
        return sqsClient.getQueueUrl(GetQueueUrlRequest.builder().queueName(queue).build()).queueUrl();
    }

    public List<Message> receiveMessagesFromQueue(Integer maxNumberMessages, Integer waitTimeSeconds) {
        ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                .queueUrl(getQueueURL())
                .maxNumberOfMessages(maxNumberMessages)
                .waitTimeSeconds(waitTimeSeconds)
                .build();
        return sqsClient.receiveMessage(receiveMessageRequest).messages();
    }
}
