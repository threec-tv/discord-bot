package co.uk.cloudam.streaming.template.config.discord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import co.uk.cloudam.streaming.template.discord.SlashCommand;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import lombok.extern.apachecommons.CommonsLog;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@CommonsLog
public record SlashCommandListener(List<? extends SlashCommand> commands) {

    @Autowired
    public SlashCommandListener {
        log.info(commands);
    }

    public Mono<Void> handle(ChatInputInteractionEvent event) {
        //Convert our list to a flux that we can iterate through
        return Flux.fromIterable(commands)
            //Filter out all commands that don't match the name this event is for
            .filter(command -> command.getName().equals(event.getCommandName()))
            //Get the first (and only) item in the flux that matches our filter
            .next()
            //Have our command class handle all logic related to its specific command.
            .flatMap(command -> command.handle(event));
    }
}
