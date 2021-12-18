package co.uk.cloudam.streaming.template.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

import lombok.extern.apachecommons.CommonsLog;

@Configuration
@CommonsLog
public class SpringConfig implements ApplicationListener<ApplicationReadyEvent> {

    @Value("${secret.env}")
    private String env;

    @Primary
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.warn("Secrets: " + env);
    }

    @Bean("webserverRestTemplate")
    public RestTemplate webserverRestTemplate(@Value("${threec.urls.webserver.baseUrl}") String url) {
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder().rootUri(url)
            .defaultHeader("Content-Type", "application/json");
        return restTemplateBuilder.build();
    }

}
