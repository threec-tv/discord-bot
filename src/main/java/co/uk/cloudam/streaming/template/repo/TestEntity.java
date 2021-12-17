package co.uk.cloudam.streaming.template.repo;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Document("testEntity")
public class TestEntity {

    String string;
}
