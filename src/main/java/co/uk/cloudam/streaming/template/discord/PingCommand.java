package co.uk.cloudam.streaming.template.discord;

import org.springframework.stereotype.Component;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import reactor.core.publisher.Mono;

@Component
public class PingCommand implements SlashCommand {

    @Override
    public String getName() {
        return "ping.json";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        //We reply to the command with "Pong!" and make sure it is ephemeral (only the command user can see it)
        return event.reply()
            .withEphemeral(true)
            .withContent("Pong!");
    }
}
