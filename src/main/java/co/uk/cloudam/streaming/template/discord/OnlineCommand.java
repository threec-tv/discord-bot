package co.uk.cloudam.streaming.template.discord;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import co.uk.cloudam.streaming.dto.ChannelDto;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import reactor.core.publisher.Mono;

@Component
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
        if (event.getOption("user").isPresent()) {
            String name = event.getOption("user")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElse("Null");
            ChannelDto[] chan = restTemplate.getForObject(REST_CHANNEL_SEARCH, ChannelDto[].class, name);
            String DisplayName = "";
            String isStreaming = "";
            Boolean streamOnline = false;
            for (ChannelDto chan2 : chan) {

                DisplayName = chan2.getDisplayName();
                isStreaming = chan2.isStreamOnline() ? "**Streaming**!"
                                                     : "not streaming. Try later?";
                streamOnline = chan2.isStreamOnline();
            }
            String foundText = "User: **" + DisplayName + "**; They are currently " + isStreaming;
            String notFoundText = "We could not find **" + name + "** at this time. Please check your spelling and try again; Perhaps they're private? O:";
            if(streamOnline){
                Button button = Button.link("https://threec.tv/"+DisplayName, "Watch "+DisplayName+"'s Stream!");
                return event.reply()
                    .withEphemeral(true)
                    .withContent((DisplayName.isEmpty() ? notFoundText : foundText))
                    .withComponents(ActionRow.of(button));
            }
            return event.reply()
                .withEphemeral(true)
                .withContent((DisplayName.isEmpty() ? notFoundText : foundText));
        } else {

            // If no name present
            ChannelDto[] list = restTemplate.getForObject(REST_CHANNEL_FEATURED, ChannelDto[].class);
            int count = getListSize(list);
            String nameList = nameList(list);
            String AllOfflineText = "No-one is currently streaming. Check back later!";
            String PeopleOnlineText = "There is " + count + " " + (count > 1 ? "people" : "person")
                                      + " streaming. Check out: " + nameList;
            return event.reply()
                .withEphemeral(true)
                .withContent(nameList.isEmpty() ? AllOfflineText : PeopleOnlineText);
        }
    }

    private String nameList(ChannelDto[] list) {
        String rlist = "";
        for (ChannelDto l : list) {
            if (l.isStreamOnline()) {
                if(!rlist.isEmpty()){
                    rlist = rlist + ", **" + l.getDisplayName()+ "** (https://threec.tv/"+l.getDisplayName()+")";
                } else {
                    rlist = "**"+ l.getDisplayName() + "** (https://threec.tv/"+l.getDisplayName()+")";
                }
            }
        }
        return rlist;
    }

    private int getListSize(ChannelDto[] list) {
        int i = 0;
        for (ChannelDto l : list) {
            if (l.isStreamOnline()) {
                i++;
            }
        }
        return i;
    }
}
