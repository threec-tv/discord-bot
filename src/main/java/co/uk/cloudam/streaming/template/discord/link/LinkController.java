package co.uk.cloudam.streaming.template.discord.link;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import lombok.Data;
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
    public TokenResponse getToken(@PathVariable("discordUserName") String discordUserName) {
        Optional<LinkCodeEntity> getTokenById = linkCodeRepo.findById(discordUserName);
        return getTokenById.isPresent() ? new TokenResponse(getTokenById.get().getUserName(), getTokenById.get().getToken(), getTokenById.get().getExpireTime()) : new TokenResponse("notFound", null, null);
    }


    @GetMapping(value = "/byToken/{token}", produces = "application/json")
    public TokenResponse getUsernameFromToken(@PathVariable("token") UUID token) {
        Optional<LinkCodeEntity> getTokenByToken = linkCodeRepo.findByToken(token);
        return getTokenByToken.isPresent() ? new TokenResponse(getTokenByToken.get().getUserName(), getTokenByToken.get().getToken(), getTokenByToken.get().getExpireTime()) : new TokenResponse("notFound", null, null);
    }

    @Data
    public static class TokenResponse {

        private String userName;
        private UUID token;
        private LocalDateTime expireTime;

        public TokenResponse(String userName, UUID token, LocalDateTime expireTime) {
            this.userName = userName;
            this.token = token;
            this.expireTime = expireTime;
        }
    }

}
