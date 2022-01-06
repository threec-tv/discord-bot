package co.uk.cloudam.streaming.template.discord;

import org.springframework.stereotype.Component;

import java.util.List;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import lombok.extern.apachecommons.CommonsLog;
import reactor.core.publisher.Mono;

@Component
@CommonsLog

public class HelpCommand implements SlashCommand {

    private final List<? extends SlashCommand> commands;

    public HelpCommand(List<? extends SlashCommand> commands) {
        this.commands = commands;
    }

    @Override
    public String getName() {
        return "help";
    }
    public String getDescription() { return "Displays various descriptions for commands."; }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        String Content = "";
        for( SlashCommand command : commands ) {
            Content = (Content + "/**" + command.getName() + "**: " + command.getDescription() + System.lineSeparator());
        }
            return event.reply()
                .withEphemeral(true)
                .withContent("Commands: " + System.lineSeparator() + Content);
    }

}
