package co.uk.cloudam.streaming.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import co.uk.cloudam.streaming.template.config.discord.SlashCommandListener;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.rest.RestClient;
import lombok.extern.apachecommons.CommonsLog;
import reactor.core.publisher.Mono;

@SpringBootApplication
@CommonsLog
public class DiscordBotApplication implements CommandLineRunner {

    private final String discordToken;
    private final SlashCommandListener slashCommandListener;

    public static void main(String[] args) {
        SpringApplication.run(DiscordBotApplication.class, args);
    }

    @Autowired
    public DiscordBotApplication(@Value("${discord.token}") String discordToken, SlashCommandListener slashCommandListener) {
        this.discordToken = discordToken;
        this.slashCommandListener = slashCommandListener;
    }

    @Bean
    public RestClient discordRestClient() {
        return RestClient.create(discordToken);
    }

    @Override
    public void run(String... args) throws Exception {
        //Login
        DiscordClientBuilder.create(discordToken).build()
            .withGateway(gatewayClient -> {

                Mono<Void> onSlashCommandMono = gatewayClient
                    .on(ChatInputInteractionEvent.class, slashCommandListener::handle)
                    .then();

                return Mono.when(onSlashCommandMono);
            }).block();
    }
}
