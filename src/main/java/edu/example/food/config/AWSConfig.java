package edu.example.food.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.net.URI;

@Configuration
public class AWSConfig {
    @Value("${aws.service-endpoint}")
    private String serviceEndpoint;
    @Bean
    public SqsClient sqsConfig(){
        return SqsClient.builder()
                .region(Region.US_EAST_1)
                .endpointOverride(URI.create(serviceEndpoint))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }
}
