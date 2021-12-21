package co.uk.cloudam.streaming.template.discord;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import co.uk.cloudam.streaming.dto.ChannelDto;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import discord4j.core.spec.InteractionApplicationCommandCallbackReplyMono;
import lombok.extern.apachecommons.CommonsLog;
import reactor.core.publisher.Mono;

@Component
@CommonsLog
public class OnlineCommand implements SlashCommand {

    protected static final String REST_CHANNEL_FEATURED = "/rest/channel/featured";
    protected static final String REST_CHANNEL_SEARCH = "/rest/channel/search?search={name}";
    private final RestTemplate restTemplate;

    public OnlineCommand(@Qualifier("webserverRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String getName() {
        return "online";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        Optional<String> searchedUserParameterOptional = event.getOption("user")
            .flatMap(ApplicationCommandInteractionOption::getValue)
            .map(ApplicationCommandInteractionOptionValue::asString);

        if (searchedUserParameterOptional.isPresent()) {
            String searchedUserParameter = searchedUserParameterOptional.get();
            List<ChannelDto> channels = List.of(restTemplate.getForObject(REST_CHANNEL_SEARCH, ChannelDto[].class, Map.of("name", searchedUserParameter)));

            if (channels.isEmpty()) {
                return errorResponse(event, searchedUserParameter);
            }

            ChannelDto channel = channels.get(0);

            String foundText = "User: **" + channel.getDisplayName() + "**; They are currently " + (channel.isStreamOnline() ? "Streaming" : "not streaming");

            Button button = Button.link("https://threec.tv/" + channel.getDisplayName(), "Watch " + channel.getDisplayName() + "'s Stream!");
            return event.reply()
                .withEphemeral(true)
                .withContent(foundText)
                .withComponents(ActionRow.of(button));

        } else {
            return onlineChannelsResponse(event);
        }
    }

    private InteractionApplicationCommandCallbackReplyMono errorResponse(ChatInputInteractionEvent event, String searchedUserParameter) {
        String notFoundText = "We could not find **" + searchedUserParameter + "** at this time. Please check your spelling and try again; Perhaps they're private? O:";

        log.error("no channels found, existing");

        return event.reply()
            .withEphemeral(true)
            .withContent(notFoundText);
    }

    private InteractionApplicationCommandCallbackReplyMono onlineChannelsResponse(ChatInputInteractionEvent event) {
        // If no searchedUserParameter present
        List<ChannelDto> list = List.of(restTemplate.getForObject(REST_CHANNEL_FEATURED, ChannelDto[].class));
        long count = list.stream().map(ChannelDto::isStreamOnline).count();
        List<ChannelDto> onlineChannels = list.stream().filter(ChannelDto::isStreamOnline).toList();

        String onlineString = onlineChannels.stream()
            .map(channelDto -> channelDto.getDisplayName() + " find them here https://threec.tv/" + channelDto.getDisplayName())
            .collect(Collectors.joining("!\n\r "));

        String content = "There are " + count + " people streaming. Check out " + onlineString;

        return event.reply()
            .withEphemeral(true)
            .withContent(content);
    }
}
