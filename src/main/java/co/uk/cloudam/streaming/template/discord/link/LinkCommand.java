package co.uk.cloudam.streaming.template.discord.link;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

import co.uk.cloudam.streaming.template.discord.SlashCommand;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import lombok.extern.apachecommons.CommonsLog;
import reactor.core.publisher.Mono;

@Component
@CommonsLog
public class LinkCommand implements SlashCommand {


    private final LinkCodeRepo linkCodeRepo;

    @Autowired
    public LinkCommand(LinkCodeRepo linkCodeRepo) {
        this.linkCodeRepo = linkCodeRepo;
    }


    @Override
    public String getName() {
        return "link";
    }
    public String getDescription() {
        return "Handles the Linking System.";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        LinkCodeEntity searchByUser = linkCodeRepo.findById(getDiscordName(event)).orElse(addToDatabase(getDiscordName(event)));
        if (isExpired(searchByUser.getExpireTime())) {
            searchByUser = addToDatabase(getDiscordName(event));
        }
        return event.reply()
            .withEphemeral(true)
            .withContent("Hello " + searchByUser.getUserName() + ", " + searchByUser.getToken() + " .. -Insert nifty bow, here-");

    }

    private UUID getNewToken() {
        return UUID.randomUUID();
    }

    private String getDiscordName(ChatInputInteractionEvent event) {
        return event.getInteraction().getUser().getUsername();
    }

    private boolean isExpired(LocalDateTime expireTime) {
        return LocalDateTime.now().isAfter(expireTime);
    }

    private LinkCodeEntity addToDatabase(String discordName) {

        LinkCodeEntity obToSave = LinkCodeEntity.builder().token(getNewToken()).userName(discordName).expireTime(LocalDateTime.now().plusMinutes(10)).build();
        return linkCodeRepo.save(obToSave);
    }


    /*
    Success criteria

    Upon a user saying link
    It pulls the discord username out of the event

    It then needs to save the token and the discord username into the database

    IF the user has already ran the command they should already have a token and it needs to pull that back out
    IF the user has not ran the command then it needs to generate one and send it back to the user.




    THEN
    What we need to do is expose a rest API for something to come along and ask for the token for a given discordUserName


     */
}
