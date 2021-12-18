package co.uk.cloudam.streaming.template;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.apachecommons.CommonsLog;

@SpringBootApplication
@CommonsLog
public class DiscordBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiscordBotApplication.class, args);
    }

}
