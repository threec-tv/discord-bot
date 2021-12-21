package co.uk.cloudam.streaming.template.discord.link;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
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

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {

        var newToken = UUID.randomUUID();
        var discordName = event.getInteraction().getUser().getUsername();
        if (linkCodeRepo.findById(discordName).isEmpty()) {
            log.error("Couldn't find user in Database? Create..");
            addToDatabase(newToken, discordName);
            log.error("Created! with User: " + discordName + " and Token: " + newToken);
            log.error("Let's now check to see if it's in the database and proceed that way.");
        }
        Optional<LinkCodeEntity> searchByUser = linkCodeRepo.findById(discordName);
        log.error("SearchByID: User: " + searchByUser.get().getUserName() + " with Token: " + searchByUser.get().getToken());
        Optional<LinkCodeEntity> searchById = linkCodeRepo.findByToken(searchByUser.get().getToken());
        log.error("SearchByToken: User: " + searchById.get().getUserName() + " with Token: " + searchById.get().getToken());

        return event.reply()
            .withEphemeral(true)
            .withContent("Hello " + searchByUser.get().getUserName() + ", Your token is: " + searchByUser.get().getToken() + " .. -Insert nifty bow, here-");
    }

    private boolean addToDatabase(UUID newToken, String discordName) {
        LinkCodeEntity obToSave = LinkCodeEntity.builder().token(newToken).userName(discordName).build();
        linkCodeRepo.save(obToSave);
        return true;
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
