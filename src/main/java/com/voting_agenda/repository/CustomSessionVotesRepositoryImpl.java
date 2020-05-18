package com.voting_agenda.repository;

import com.voting_agenda.model.Vote;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class CustomSessionVotesRepositoryImpl implements CustomSessionVotesRepository {

    private MongoTemplate mongoTemplate;

    public CustomSessionVotesRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void pushVote(String id, Vote vote) {

        mongoTemplate.updateFirst(
                Query.query(Criteria.where("votingSession.agenda._id").is(new ObjectId(id))),
                new Update().addToSet("allSessionVotes", vote), "sessionVotes");
    }
}
