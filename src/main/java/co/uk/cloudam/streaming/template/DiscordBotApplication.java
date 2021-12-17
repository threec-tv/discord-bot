package co.uk.cloudam.streaming.template;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import co.uk.cloudam.streaming.template.config.discord.SlashCommandListener;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.rest.RestClient;
import lombok.extern.apachecommons.CommonsLog;
import reactor.core.publisher.Mono;

@SpringBootApplication
@CommonsLog
public class DiscordBotApplication {

    @Value("${discord.token}")
    private String discordToken;

    public static void main(String[] args) {
        log.info(args);
        //Start spring application
        ApplicationContext springContext = new SpringApplicationBuilder(DiscordBotApplication.class)
            .build()
            .run(args);

        //Login
        DiscordClientBuilder.create("").build()
            .withGateway(gatewayClient -> {
                SlashCommandListener slashCommandListener = new SlashCommandListener(springContext);

                Mono<Void> onSlashCommandMono = gatewayClient
                    .on(ChatInputInteractionEvent.class, slashCommandListener::handle)
                    .then();

                return Mono.when(onSlashCommandMono);
            }).block();
    }

    @Bean
    public RestClient discordRestClient() {
        return RestClient.create(discordToken);

    }

}
