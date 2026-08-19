package com.agendavoting.business;

import com.agendavoting.dto.VoteDTO;
import com.agendavoting.dto.VotingResultDTO;
import com.agendavoting.dto.VotingSessionInputDTO;
import com.agendavoting.enums.Status;
import com.agendavoting.enums.VotingOption;
import com.agendavoting.exception.UnableToVoteException;
import com.agendavoting.exception.VotingClosedException;
import com.agendavoting.model.Agenda;
import com.agendavoting.model.VotingSession;
import com.agendavoting.restClient.CPFConsultationClient;
import com.agendavoting.service.AgendaService;
import com.agendavoting.service.VoteService;
import com.agendavoting.service.VotingSessionService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class SessionVoteBusiness {

    private final VotingSessionService votingSessionService;
    private final AgendaService agendaService;
    private final VoteService voteService;
    private final CPFConsultationClient cpfConsultationClient;

    public SessionVoteBusiness(VotingSessionService votingSessionService, AgendaService agendaService, VoteService voteService, CPFConsultationClient cpfConsultationClient) {
        this.votingSessionService = votingSessionService;
        this.agendaService = agendaService;
        this.voteService = voteService;
        this.cpfConsultationClient = cpfConsultationClient;
    }

    public String startVotingSession(VotingSessionInputDTO votingSessionInputDTO) {
        VotingSession votingSession = VotingSession.builder().start(votingSessionInputDTO.start()).build();
        validateVotingSession(votingSession, votingSessionInputDTO.timeDuration());
        Agenda agenda = agendaService.findById(votingSessionInputDTO.agendaId());
        votingSession.setAgenda(agenda);
        VotingSession votingSessionReturned = votingSessionService.save(votingSession);
        return votingSessionReturned.getId();
    }

    private void validateVotingSession(VotingSession votingSession, Long timeDuration) {
        if (Objects.isNull(votingSession.getStart())) {
            votingSession.setStart(LocalDateTime.now());
        }
        if (Objects.isNull(timeDuration)) {
            votingSession.setEnd(votingSession.getStart().plusMinutes(1));
        } else {
            votingSession.setEnd(votingSession.getStart().plusMinutes(timeDuration));

        }
    }

    public void toVote(VoteDTO voteDTO) {
        checkCPF(voteDTO.cpf());
        VotingSession votingSession = votingSessionService.findByAgendaId(voteDTO.agendaId());
        checkSessionVoteTime(votingSession, voteDTO.agendaId());
        voteService.cast(votingSession.getId(), voteDTO.cpf(), voteDTO.votingOption());
    }

    private void checkSessionVoteTime(VotingSession votingSession, String agendaId) {
        if (!votingSession.getEnd().isAfter(LocalDateTime.now())) {
            throw new VotingClosedException(agendaId);
        }
    }

    private void checkCPF(String cpf) {
        if (cpfConsultationClient.getStatus(cpf).equals(Status.UNABLE_TO_VOTE)) {
            throw new UnableToVoteException(cpf);
        }

    }

    public VotingResultDTO getVotingResult(String agendaId) {
        VotingSession votingSession = votingSessionService.findByAgendaId(agendaId);
        Long yesVotes = voteService.countBySessionAndOption(votingSession.getId(), VotingOption.YES);
        Long noVotes = voteService.countBySessionAndOption(votingSession.getId(), VotingOption.NO);
        return new VotingResultDTO(yesVotes, noVotes, yesVotes + noVotes);
    }
}
