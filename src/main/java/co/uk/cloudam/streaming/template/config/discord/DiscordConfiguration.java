package co.uk.cloudam.streaming.template.config.discord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.rest.RestClient;
import reactor.core.publisher.Mono;

@Profile("!test")
@Configuration
public class DiscordConfiguration implements CommandLineRunner {

    private final String discordToken;
    private final SlashCommandListener slashCommandListener;

    @Autowired
    public DiscordConfiguration(@Value("${discord.token}") String discordToken, SlashCommandListener slashCommandListener) {
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
