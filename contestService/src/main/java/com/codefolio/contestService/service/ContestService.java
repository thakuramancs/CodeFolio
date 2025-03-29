package com.codefolio.contestService.service;

import com.codefolio.contestService.Clients.CodeforcesClient;
import com.codefolio.contestService.model.Contest;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContestService {
    private final CodeforcesClient codeforcesClient;

    public ContestService(CodeforcesClient codeforcesClient) {
        this.codeforcesClient = codeforcesClient;
    }

    public List<Contest> getUpcomingContests() {
        return codeforcesClient.getContests().getResult().stream()
            .filter(contest -> "BEFORE".equals(contest.getPhase()))
            .collect(Collectors.toList());
    }

    public List<Contest> getOngoingContests() {
        return codeforcesClient.getContests().getResult().stream()
            .filter(contest -> "CODING".equals(contest.getPhase()))
            .collect(Collectors.toList());
    }
} 