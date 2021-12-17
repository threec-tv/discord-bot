package co.uk.cloudam.streaming.template.config;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;

import io.awspring.cloud.core.env.ResourceIdResolver;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;

@Configuration
public class SqsConfig {

    @Value("${cloud.aws.region.static}")
    private String region;

    @Bean
    @Primary
    public QueueMessagingTemplate queueMessagingTemplate(AmazonSQSAsync amazonSQSAsync, ObjectMapper objectMapper) {
        return new QueueMessagingTemplate(amazonSQSAsync, (ResourceIdResolver) null, mappingJackson2MessageConverter(objectMapper));
    }

    private MappingJackson2MessageConverter mappingJackson2MessageConverter(ObjectMapper objectMapper) {
        MappingJackson2MessageConverter jacksonMessageConverter = new MappingJackson2MessageConverter();
        jacksonMessageConverter.setObjectMapper(objectMapper);
        jacksonMessageConverter.setSerializedPayloadClass(String.class);
        return jacksonMessageConverter;
    }

    @Bean
    @Primary
    public AmazonSQSAsync amazonSQSAsync() {
        return AmazonSQSAsyncClientBuilder
            .standard()
            .withCredentials(new DefaultAWSCredentialsProviderChain())
            .withRegion(region).build();
    }
}
