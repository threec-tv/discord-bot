package co.uk.cloudam.streaming.template.discord;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import co.uk.cloudam.streaming.dto.ChannelDto;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import reactor.core.publisher.Mono;

@Component
public class ShuffleChannelCommand implements SlashCommand {

    protected static final String REST_CHANNEL_SHUFFLE = "/rest/channel/shuffle";
    private final RestTemplate restTemplate;

    public ShuffleChannelCommand(@Qualifier("webserverRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String getName() {
        return "shuffle";
    }
    public String getDescription() { return "Uses the ThreeC API to shuffle between channels."; }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        ChannelDto randomChannel = restTemplate.getForObject(REST_CHANNEL_SHUFFLE, ChannelDto.class);

        return event.reply()
            .withEphemeral(true)
            .withContent("Check out, " + randomChannel.getDisplayName() + " https://threec.tv/" + randomChannel.getDisplayName());
    }
}
