package co.uk.cloudam.streaming.template.config.discord;

import org.springframework.context.ApplicationContext;

import java.util.Collection;

import co.uk.cloudam.streaming.template.discord.SlashCommand;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class SlashCommandListener {

    private final Collection<SlashCommand> commands;

    public SlashCommandListener(ApplicationContext applicationContext) {
        //Get all classes that implement our SlashCommand interface and annotated with @Component
        commands = applicationContext.getBeansOfType(SlashCommand.class).values();
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
