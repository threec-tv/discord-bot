package co.uk.cloudam.streaming.template.discord;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import co.uk.cloudam.streaming.dto.ChannelDto;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.Interaction;
import discord4j.core.spec.InteractionApplicationCommandCallbackReplyMono;
import discord4j.discordjson.json.InteractionData;
import discord4j.gateway.ShardInfo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShuffleChannelCommandTest {

    @Mock
    RestTemplate restTemplate;
    @Mock
    GatewayDiscordClient gateway;
    @Mock
    ShardInfo shardInfo;
    @Mock
    InteractionData interactionData;

    @BeforeEach
    void setUp() {
        when(interactionData.applicationId()).thenReturn("1");
    }

    @Test
    void shouldCallApiAndReturnChannel() {
        ChatInputInteractionEvent event = new ChatInputInteractionEvent(gateway, shardInfo, new Interaction(gateway, interactionData));
        when(restTemplate.getForObject(ShuffleChannelCommand.REST_CHANNEL_SHUFFLE, ChannelDto.class))
            .thenReturn(ChannelDto.builder().displayName("display").build());
        ShuffleChannelCommand underTest = new ShuffleChannelCommand(restTemplate);

        InteractionApplicationCommandCallbackReplyMono result = (InteractionApplicationCommandCallbackReplyMono) underTest.handle(event);

        assertThat(result.content().get(), is("Check out, display https://threec.tc/display"));
    }
}
