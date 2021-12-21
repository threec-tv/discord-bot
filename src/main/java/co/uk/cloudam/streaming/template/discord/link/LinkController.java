package co.uk.cloudam.streaming.template.discord.link;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/link")
public class LinkController {

    private final LinkCodeRepo linkCodeRepo;

    @Autowired
    public LinkController(LinkCodeRepo linkCodeRepo) {
        this.linkCodeRepo = linkCodeRepo;
    }

    @GetMapping("/{discordUserName}")
    public String getToken(@PathVariable("discordUserName") String discordUserName) {

        return "EMPTY STRING BOOIIIIIII";

    }


    @GetMapping("/{token}")
    public String getUsernameFromToken(@PathVariable("token") String token) {

        return "EMPTY STRING BOOIIIIIII";

    }

}
