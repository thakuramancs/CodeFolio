package com.codefolio.contestService.service;

import com.codefolio.contestService.Clients.CodeforcesClient;
import com.codefolio.contestService.model.Contest;
import com.codefolio.contestService.model.CodeforcesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import com.codefolio.contestService.model.CodeforcesContest;

@Service
public class ContestServiceImpl implements ContestService {
    private final CodeforcesClient codeforcesClient;

    @Autowired
    public ContestServiceImpl(CodeforcesClient codeforcesClient) {
        this.codeforcesClient = codeforcesClient;
    }

    @Override
    public List<Contest> getUpcomingContests() {
        CodeforcesResponse response = codeforcesClient.getContests();
        return response.getResult().stream()
            .filter((CodeforcesContest contest) -> contest.getPhase().equals("BEFORE"))
            .map(contest -> new Contest(
                contest.getId(),
                contest.getName(),
                "Codeforces",
                contest.getStartTimeSeconds() * 1000,
                contest.getDurationSeconds() * 1000,
                "https://codeforces.com/contests/" + contest.getId()
            ))
            .collect(Collectors.toList());
    }

    @Override
    public List<Contest> getOngoingContests() {
        CodeforcesResponse response = codeforcesClient.getContests();
        return response.getResult().stream()
            .filter(contest -> contest.getPhase().equals("CODING"))
            .map(contest -> new Contest(
                contest.getId(),
                contest.getName(),
                "Codeforces",
                contest.getStartTimeSeconds() * 1000,
                contest.getDurationSeconds() * 1000,
                "https://codeforces.com/contests/" + contest.getId()
            ))
            .collect(Collectors.toList());
    }
} 