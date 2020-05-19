package com.votingAgenda.business;

import com.votingAgenda.DTO.VoteDTO;
import com.votingAgenda.DTO.VotingResultDTO;
import com.votingAgenda.DTO.VotingSessionInputDTO;
import com.votingAgenda.enums.Status;
import com.votingAgenda.enums.VotingOption;
import com.votingAgenda.exception.DuplicateVoteException;
import com.votingAgenda.exception.UnableToVoteException;
import com.votingAgenda.exception.VotingClosedException;
import com.votingAgenda.model.Agenda;
import com.votingAgenda.model.SessionVotes;
import com.votingAgenda.model.Vote;
import com.votingAgenda.model.VotingSession;
import com.votingAgenda.restClient.CPFConsultationClient;
import com.votingAgenda.service.AgendaService;
import com.votingAgenda.service.SessionVotesService;
import com.votingAgenda.service.VotingSessionService;
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
    private CPFConsultationClient cpfConsultationClient;

    public SessionVoteBusiness(VotingSessionService votingSessionService, AgendaService agendaService, SessionVotesService sessionVotesService, ModelMapper defaultModelMapper, CPFConsultationClient cpfConsultationClient) {
        this.votingSessionService = votingSessionService;
        this.agendaService = agendaService;
        this.sessionVotesService = sessionVotesService;
        this.defaultModelMapper = defaultModelMapper;
        this.cpfConsultationClient = cpfConsultationClient;
    }

    public String startVotingSession(VotingSessionInputDTO votingSessionInputDTO) {
        VotingSession votingSession = defaultModelMapper.map(votingSessionInputDTO, VotingSession.class);
        validateVotingSession(votingSession, votingSessionInputDTO.getTimeDuration());
        Agenda agenda = agendaService.findById(votingSessionInputDTO.getAgendaId());
        votingSession.setAgenda(agenda);
        VotingSession votingSessionReturned = votingSessionService.save(votingSession);
        sessionVotesService.save(new SessionVotes(null, votingSessionReturned, null));
        return votingSessionReturned.getId();
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

    public void toVote(VoteDTO voteDTO) {
        checkCPF(voteDTO);
        checkSessionVoteTime(voteDTO.getAgendaId());
        Vote vote = defaultModelMapper.map(voteDTO, Vote.class);
        sessionVotesService.pushVote(voteDTO.getAgendaId(), vote);
    }

    private void checkSessionVoteTime(String agendaId) {
        if (votingSessionService.findEndTime(agendaId).isBefore(LocalDateTime.now())) {
            throw new VotingClosedException(agendaId);
        }
    }

    private void checkCPF(VoteDTO voteDTO) {
        if (cpfConsultationClient.getStatus(voteDTO.getCpf()).equals(Status.UNABLE_TO_VOTE)) {
            throw new UnableToVoteException(voteDTO.getCpf());
        }
        if (sessionVotesService.existsByIdAndAllSessionVotesCpf(voteDTO.getAgendaId(), voteDTO.getCpf())) {
            throw new DuplicateVoteException(voteDTO.getCpf());
        }
    }

    public VotingResultDTO getVotingResult(String agendaId) {
        Collection<Vote> votes = sessionVotesService.findVotesByAgendaId(agendaId);
        Map<VotingOption, Long> map = votes.stream().collect(groupingBy(Vote::getVotingOption, counting()));
        return new VotingResultDTO().builder()
                .yesVotes(nullValidation(map.get(VotingOption.YES)))
                .noVotes(nullValidation(map.get(VotingOption.NO)))
                .totalVotes(Long.valueOf(votes.size()))
                .build();
    }

    private Long nullValidation(Long value) {
        return Objects.isNull(value) ? 0L : value;
    }
}