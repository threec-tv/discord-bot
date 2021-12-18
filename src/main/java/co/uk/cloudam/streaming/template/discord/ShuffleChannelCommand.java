package co.uk.cloudam.streaming.template.discord;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import co.uk.cloudam.streaming.dto.ChannelDto;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import reactor.core.publisher.Mono;

@Component
public class ShuffleChannelCommand implements SlashCommand {

    private final RestTemplate restTemplate;

    public ShuffleChannelCommand(@Qualifier("webserverRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String getName() {
        return "shuffle";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        //We reply to the command with "Pong!" and make sure it is ephemeral (only the command user can see it)

        ChannelDto randomChannel = restTemplate.getForObject("/rest/channel/shuffle", ChannelDto.class);

        return event.reply()
            .withEphemeral(true)
            .withContent("Check out, " + randomChannel.getDisplayName() + " https://threec.tc/" + randomChannel.getDisplayName());
    }
}
