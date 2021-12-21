package co.uk.cloudam.streaming.template.discord.link;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

import lombok.extern.apachecommons.CommonsLog;

@RestController

@CommonsLog
@RequestMapping("/rest/link")
public class LinkController {

    private final LinkCodeRepo linkCodeRepo;

    @Autowired
    public LinkController(LinkCodeRepo linkCodeRepo) {
        this.linkCodeRepo = linkCodeRepo;
    }

    @GetMapping(value = "/byUser/{discordUserName}", produces = "application/json")
    public String getToken(@PathVariable("discordUserName") String discordUserName) {
        Optional<LinkCodeEntity> getTokenById = linkCodeRepo.findById(discordUserName);
        UUID gotToken = getTokenById.get().getToken();
        String gotDiscordUser = getTokenById.get().getUserName();
        return "{ \"userName\": \"" + gotDiscordUser + "\", \"token\":\"" + gotToken + "\" }";
    }


    @GetMapping(value = "/byToken/{token}", produces = "application/json")
    public String getUsernameFromToken(@PathVariable("token") UUID token) {
        Optional<LinkCodeEntity> getTokenByToken = linkCodeRepo.findByToken(token);
        UUID gotToken = getTokenByToken.get().getToken();
        String gotDiscordUser = getTokenByToken.get().getUserName();
        return "{ \"userName\": \"" + gotDiscordUser + "\", \"token\":\"" + gotToken + "\" }";

    }


}
