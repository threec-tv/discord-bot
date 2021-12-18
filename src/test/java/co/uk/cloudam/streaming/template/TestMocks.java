package co.uk.cloudam.streaming.template;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import co.uk.cloudam.streaming.template.config.discord.SlashCommandListener;
import co.uk.cloudam.streaming.template.discord.SlashCommand;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.rest.RestClient;
import io.awspring.cloud.messaging.core.NotificationMessagingTemplate;
import io.awspring.cloud.messaging.listener.SimpleMessageListenerContainer;
import reactor.core.publisher.Mono;

@TestConfiguration
public class TestMocks {

    @MockBean
    private NotificationMessagingTemplate notificationMessagingTemplate;

    @MockBean
    private SimpleMessageListenerContainer simpleMessageListenerContainer;

    @MockBean
    private SlashCommandListener slashCommandListener;

    @MockBean
    private RestClient discordRestClient;

    @Component
    private class EmptyCommand implements SlashCommand {

        @Override
        public String getName() {
            return "test";
        }

        @Override
        public Mono<Void> handle(ChatInputInteractionEvent event) {
            return null;
        }
    }

}
