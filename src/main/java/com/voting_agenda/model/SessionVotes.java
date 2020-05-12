package com.voting_agenda.model;


import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Collection;

@Document(collection = "VotosSessao")
public class SessionVotes implements Serializable {

   private static final long serialVersionUID = -5013268988160474124L;
   private VotingSession votingSession;
   private Collection<Vote> allSessionVotes;
}
