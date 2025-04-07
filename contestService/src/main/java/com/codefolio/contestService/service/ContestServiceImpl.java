package com.codefolio.contestService.service;

import com.codefolio.contestService.Clients.*;
import com.codefolio.contestService.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Comparator;
import java.time.Instant;

@Service
public class ContestServiceImpl implements ContestService {
    private static final Logger logger = LoggerFactory.getLogger(ContestServiceImpl.class);
    
    private final CodeforcesClient codeforcesClient;
    private final CodeChefClient codeChefClient;
    private final LeetCodeClient leetCodeClient;
    private final GFGClient gfgClient;
    private final HackerRankClient hackerRankClient;

    @Autowired
    public ContestServiceImpl(CodeforcesClient codeforcesClient, CodeChefClient codeChefClient, 
                            LeetCodeClient leetCodeClient, GFGClient gfgClient, 
                            HackerRankClient hackerRankClient) {
        this.codeforcesClient = codeforcesClient;
        this.codeChefClient = codeChefClient;
        this.leetCodeClient = leetCodeClient;
        this.gfgClient = gfgClient;
        this.hackerRankClient = hackerRankClient;
    }

    @Override
    public List<Contest> getActiveContests() {
        List<Contest> contests = new ArrayList<>();
        fetchContests(contests, true);
        return sortContests(contests);
    }

    @Override
    public List<Contest> getUpcomingContests() {
        List<Contest> contests = new ArrayList<>();
        fetchContests(contests, false);
        return sortContests(contests);
    }

    @Override
    public List<Contest> getAllContests() {
        List<Contest> contests = new ArrayList<>();
        fetchContests(contests, null);
        return sortContests(contests);
    }

    private List<Contest> sortContests(List<Contest> contests) {
        return contests.stream()
            .sorted(Comparator.comparingLong(Contest::getStartTime))
            .collect(Collectors.toList());
    }

    private void fetchContests(List<Contest> contests, Boolean isActive) {
        // Codeforces
        try {
            CodeforcesResponse cfResponse = codeforcesClient.getContests();
            if (cfResponse != null && cfResponse.getResult() != null) {
                contests.addAll(cfResponse.getResult().stream()
                    .filter(contest -> filterByPhase(contest.getPhase(), isActive))
                    .map(contest -> new Contest(
                        contest.getId(),
                        contest.getName(),
                        "Codeforces",
                        contest.getStartTimeSeconds() * 1000,
                        contest.getDurationSeconds() * 1000,
                        "https://codeforces.com/contests/" + contest.getId(),
                        "",
                        isActive ? "ACTIVE" : "UPCOMING"
                    ))
                    .collect(Collectors.toList()));
            }
        } catch (Exception e) {
            logger.error("Error fetching Codeforces contests: {}", e.getMessage());
        }

        // CodeChef
        try {
            CodeChefResponse ccResponse = codeChefClient.getActiveContests();
            if (ccResponse != null && ccResponse.getActiveContests() != null) {
                contests.addAll(ccResponse.getActiveContests().stream()
                    .filter(contest -> {
                        long currentTime = System.currentTimeMillis();
                        if (isActive) {
                            // For active contests: current time should be between start and end time
                            return currentTime >= contest.getStartTime() && 
                                   currentTime <= (contest.getStartTime() + contest.getDuration());
                        } else {
                            // For upcoming contests: start time should be in the future
                            return currentTime < contest.getStartTime();
                        }
                    })
                    .collect(Collectors.toList()));
                logger.info("Fetched {} {} CodeChef contests", 
                    contests.size(), isActive ? "active" : "upcoming");
            }
        } catch (Exception e) {
            logger.error("Error fetching CodeChef contests: {}", e.getMessage());
        }

        // LeetCode
        try {
            String query = "{\"query\": \"{ allContests { title startTime duration titleSlug } }\"}";
            LeetCodeResponse lcResponse = leetCodeClient.getContests(query, "Mozilla/5.0");
            if (lcResponse != null && lcResponse.getData() != null && lcResponse.getData().getAllContests() != null) {
                long currentTime = System.currentTimeMillis() / 1000;
                contests.addAll(lcResponse.getData().getAllContests().stream()
                    .filter(contest -> {
                        long contestStart = contest.getStartTime();
                        long contestEnd = contestStart + (contest.getDuration() / 1000);
                        if (isActive) {
                            // For active contests: current time should be between start and end time
                            return currentTime >= contestStart && currentTime <= contestEnd;
                        } else {
                            // For upcoming contests: start time should be in the future
                            return currentTime < contestStart;
                        }
                    })
                    .map(contest -> new Contest(
                        contest.getTitleSlug().hashCode(),
                        contest.getTitle(),
                        "LeetCode",
                        contest.getStartTime() * 1000,
                        contest.getDuration(),
                        "https://leetcode.com/contest/" + contest.getTitleSlug(),
                        "",
                        isActive ? "ACTIVE" : "UPCOMING"
                    ))
                    .collect(Collectors.toList()));
                logger.info("Fetched {} {} LeetCode contests", 
                    contests.size(), isActive ? "active" : "upcoming");
            }
        } catch (Exception e) {
            logger.error("Error fetching LeetCode contests: {}", e.getMessage());
        }

        // GeeksForGeeks
        try {
            GFGResponse gfgResponse = gfgClient.getActiveContests();
            if (gfgResponse != null && gfgResponse.getActiveContests() != null) {
                contests.addAll(gfgResponse.getActiveContests().stream()
                    .filter(contest -> isActive == null || contest.getStatus().equals(isActive ? "ACTIVE" : "UPCOMING"))
                    .collect(Collectors.toList()));
            }
        } catch (Exception e) {
            logger.error("Error fetching GFG contests: {}", e.getMessage());
        }

        // HackerRank
        try {
            HackerRankResponse hrResponse = isActive ? hackerRankClient.getActiveContests() : hackerRankClient.getUpcomingContests();
            if (hrResponse != null && hrResponse.getModels() != null) {
                contests.addAll(hrResponse.getModels().stream()
                    .map(contest -> new Contest(
                        contest.getId(),
                        contest.getName(),
                        "HackerRank",
                        contest.getStartTime(),
                        contest.getDuration(),
                        contest.getUrl(),
                        contest.getDescription(),
                        isActive ? "ACTIVE" : "UPCOMING"
                    ))
                    .collect(Collectors.toList()));
            }
        } catch (Exception e) {
            logger.error("Error fetching HackerRank contests: {}", e.getMessage());
        }
    }

    private boolean filterByPhase(String phase, Boolean isActive) {
        if (isActive == null) return true;
        return isActive ? phase.equals("CODING") : phase.equals("BEFORE");
    }

    private boolean filterByTime(long startTime, long currentTime, Boolean isActive) {
        if (isActive == null) return true;
        return isActive ? startTime <= currentTime : startTime > currentTime;
    }
} 