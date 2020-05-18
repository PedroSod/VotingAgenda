package com.voting_agenda.business;

import com.voting_agenda.DTO.VoteDTO;
import com.voting_agenda.DTO.VotingResultDTO;
import com.voting_agenda.DTO.VotingSessionInputDTO;
import com.voting_agenda.enums.VotingOption;
import com.voting_agenda.exception.DuplicateVoteException;
import com.voting_agenda.exception.VotingClosedException;
import com.voting_agenda.model.Agenda;
import com.voting_agenda.model.SessionVotes;
import com.voting_agenda.model.Vote;
import com.voting_agenda.model.VotingSession;
import com.voting_agenda.service.AgendaService;
import com.voting_agenda.service.SessionVotesService;
import com.voting_agenda.service.VotingSessionService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

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
        sessionVotesService.save(new SessionVotes(null, votingSession, null));
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

        if (sessionVotesService.existsByIdAndAllSessionVotesCpf(voteDTO.getAgendaId(), vote.getCpf())) {
            throw new DuplicateVoteException(vote.getCpf());
        }
        checkSessionVoteTime(voteDTO.getAgendaId());
        sessionVotesService.pushVote(voteDTO.getAgendaId(), vote);
    }

    private void checkSessionVoteTime(String votingSessionId) {
        if (votingSessionService.findEndTime(votingSessionId).isBefore(LocalDateTime.now())) {
            throw new VotingClosedException(votingSessionId);
        }
    }

    public VotingResultDTO getVotingResult(String agendaId) {
        Collection<Vote> votes = sessionVotesService.countByVotingSessionAgendaId(agendaId);
        Map<VotingOption, Long> map = votes.stream().collect(groupingBy(Vote::getVotingOption, counting()));
        return new VotingResultDTO(
                nullValidation(map.get(VotingOption.YES)),
                nullValidation(map.get(VotingOption.NO)),
                Long.valueOf(votes.size()));
    }

    private Long nullValidation(Long value) {
        return Objects.isNull(value) ? 0L : value;
    }
}