package com.agendavoting.repository;

import com.agendavoting.model.Vote;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class CustomSessionVotesRepositoryImpl implements CustomSessionVotesRepository {

    private final MongoTemplate mongoTemplate;

    public CustomSessionVotesRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void pushVote(String agendaId, Vote vote) {

        mongoTemplate.updateFirst(
                Query.query(Criteria.where("votingSession.agenda._id").is(new ObjectId(agendaId))),
                new Update().addToSet("allSessionVotes", vote), "sessionVotes");
    }
}
