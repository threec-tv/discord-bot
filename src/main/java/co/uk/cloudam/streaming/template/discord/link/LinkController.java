package co.uk.cloudam.streaming.template.discord.link;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import lombok.extern.apachecommons.CommonsLog;

@RestController

@CommonsLog
@RequestMapping("/rest/link")
public class LinkController {

    private Map<String, String> tMap = new HashMap<>();
    private final LinkCodeRepo linkCodeRepo;

    @Autowired
    public LinkController(LinkCodeRepo linkCodeRepo) {
        this.linkCodeRepo = linkCodeRepo;
    }

    @GetMapping(value = "/byUser/{discordUserName}", produces = "application/json")
    public LinkCodeEntity getToken(@PathVariable("discordUserName") String discordUserName) {
        Optional<LinkCodeEntity> getTokenById = linkCodeRepo.findById(discordUserName);
        LinkCodeEntity linkCodeEntity = new LinkCodeEntity(getTokenById.get().getUserName(), getTokenById.get().getToken(), getTokenById.get().getExpireTime());
        return linkCodeEntity;
    }


    @GetMapping(value = "/byToken/{token}", produces = "application/json")
    public LinkCodeEntity getUsernameFromToken(@PathVariable("token") UUID token) {
        Optional<LinkCodeEntity> getTokenByToken = linkCodeRepo.findByToken(token);
        LinkCodeEntity linkCodeEntity = new LinkCodeEntity(getTokenByToken.get().getUserName(), getTokenByToken.get().getToken(), getTokenByToken.get().getExpireTime());
        return linkCodeEntity;
    }

}
