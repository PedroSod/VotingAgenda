package com.agendavoting.repository;

import com.agendavoting.enums.VotingOption;
import com.agendavoting.model.Vote;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DataMongoTest(properties = "spring.data.mongodb.auto-index-creation=true")
@Testcontainers(disabledWithoutDocker = true)
class VoteRepositoryIT {

    @Container
    static final MongoDBContainer mongo =
            new MongoDBContainer("mongo:7.0");

    @DynamicPropertySource
    static void mongoProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongo::getReplicaSetUrl);
    }

    @Autowired
    private VoteRepository voteRepository;

    @Test
    void rejectsTwoVotesFromTheSameCpfInOneSession() {
        voteRepository.insert(Vote.builder()
                .votingSessionId("session-1")
                .cpf("91693816075")
                .votingOption(VotingOption.YES)
                .build());

        assertThrows(DuplicateKeyException.class, () -> voteRepository.insert(Vote.builder()
                .votingSessionId("session-1")
                .cpf("91693816075")
                .votingOption(VotingOption.NO)
                .build()));
    }
}
