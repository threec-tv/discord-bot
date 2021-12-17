package co.uk.cloudam.streaming.template.config;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import io.awspring.cloud.messaging.core.NotificationMessagingTemplate;

@Configuration
public class SnsConfig {

    @Value("${cloud.aws.region.static}")
    private String region;

    @Bean
    public NotificationMessagingTemplate notificationMessagingTemplate(AmazonSNS amazonSNS) {
        return new NotificationMessagingTemplate(amazonSNS);
    }

    @Bean
    @Primary
    public AmazonSNS amazonSNS() {
        return AmazonSNSClientBuilder
            .standard()
            .withCredentials(new DefaultAWSCredentialsProviderChain())
            .withRegion(region)
            .build();
    }
}
