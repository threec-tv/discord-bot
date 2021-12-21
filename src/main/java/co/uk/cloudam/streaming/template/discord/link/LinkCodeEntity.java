package co.uk.cloudam.streaming.template.discord.link;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Data
@Document("linkCode")
@Builder
public class LinkCodeEntity {

    @Id
    private String userName;
    private String token;

}
