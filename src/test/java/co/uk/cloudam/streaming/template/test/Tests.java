package co.uk.cloudam.streaming.template.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

import co.uk.cloudam.streaming.template.repo.TestEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;

@DataMongoTest(properties = "aws.secretsmanager.enabled=false")
class Tests {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    void mongoTest() {

        TestEntity entity = new TestEntity("String");
        mongoTemplate.save(entity);

        List<TestEntity> all = mongoTemplate.findAll(TestEntity.class);

        assertThat(all, hasItem(entity));
    }


}
