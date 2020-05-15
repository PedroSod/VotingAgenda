package com.voting_agenda.business;

import com.voting_agenda.DTO.VoteDTO;
import com.voting_agenda.DTO.VotingSessionInputDTO;
import com.voting_agenda.model.Agenda;
import com.voting_agenda.model.SessionVotes;
import com.voting_agenda.model.Vote;
import com.voting_agenda.model.VotingSession;
import com.voting_agenda.service.AgendaService;
import com.voting_agenda.service.SessionVotesService;
import com.voting_agenda.service.VotingSessionService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@Service
public class SessionVoteBusiness {

    private VotingSessionService votingSessionService;
    private AgendaService agendaService;
    private SessionVotesService sessionVotesService;
    private ModelMapper defaultModelMapper;

    public SessionVoteBusiness(VotingSessionService votingSessionService, AgendaService agendaService, SessionVotesService sessionVotesService, ModelMapper defaultModelMapper) {
        this.votingSessionService = votingSessionService;
        this.agendaService = agendaService;
        this.sessionVotesService = sessionVotesService;
        this.defaultModelMapper = defaultModelMapper;
    }

    public String startVotingSession(VotingSessionInputDTO votingSessionInputDTO) {
        VotingSession votingSession = defaultModelMapper.map(votingSessionInputDTO, VotingSession.class);
        validateVotingSession(votingSession, votingSessionInputDTO.getTimeDuration());
        Agenda agenda = agendaService.findById(votingSessionInputDTO.getAgendaId());
        votingSession.setAgenda(agenda);
        String idVotingSession = votingSessionService.save(votingSession).getId();
        sessionVotesService.save(new SessionVotes(votingSession, null));
        return idVotingSession;
    }

    private VotingSession validateVotingSession(VotingSession votingSession, Long timeDuration) {
        if (Objects.isNull(votingSession.getStart())) {
            votingSession.setStart(LocalDateTime.now());
        }
        if (Objects.isNull(timeDuration)) {
            votingSession.setEnd(votingSession.getStart().plusMinutes(1));
        } else {
            votingSession.setEnd(votingSession.getStart().plusMinutes(timeDuration));

        }
        return votingSession;
    }

    public void toVote(VoteDTO voteDTO) throws Exception {
        Vote vote = defaultModelMapper.map(voteDTO, Vote.class);

        if (sessionVotesService.existsByIdAndAllSessionVotesCpf(voteDTO.getVotingSessionId(), vote.getCpf())) {
            throw new Exception();
        }
        SessionVotes sessionVotes = sessionVotesService.findAllBySessionId(voteDTO.getVotingSessionId());
        Collection<Vote> allSessionVotes =
                sessionVotes.getAllSessionVotes();
        if(CollectionUtils.isEmpty(allSessionVotes)){
            allSessionVotes = new ArrayList<Vote>();
        }
        allSessionVotes.add(vote);
        sessionVotes.setAllSessionVotes(allSessionVotes);
        sessionVotesService.update(sessionVotes);
    }

}
