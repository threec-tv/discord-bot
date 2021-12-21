package co.uk.cloudam.streaming.template.discord.link;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface LinkCodeRepo extends MongoRepository<LinkCodeEntity, String> {

    Optional<LinkCodeEntity> findByToken(UUID token);

    Optional<LinkCodeEntity> findById(String id);

}
