package co.uk.cloudam.streaming.template.discord.link;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface LinkCodeRepo extends MongoRepository<LinkCodeEntity, String> {

    Optional<LinkCodeEntity> findByToken(String token);

}
