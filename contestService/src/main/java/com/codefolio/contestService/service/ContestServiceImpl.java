package com.codefolio.contestService.service;

import com.codefolio.contestService.Clients.CodeforcesClient;
import com.codefolio.contestService.Clients.CodeChefClient;
import com.codefolio.contestService.Clients.LeetCodeClient;
import com.codefolio.contestService.Clients.GFGClient;
import com.codefolio.contestService.Clients.HackerRankClient;
import com.codefolio.contestService.model.Contest;
import com.codefolio.contestService.model.CodeforcesResponse;
import com.codefolio.contestService.model.CodeChefResponse;
import com.codefolio.contestService.model.LeetCodeResponse;
import com.codefolio.contestService.model.GFGResponse;
import com.codefolio.contestService.model.HackerRankResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Comparator;
import java.time.Instant;
import com.codefolio.contestService.model.CodeChefContestList;

@Service
public class ContestServiceImpl implements ContestService {
    private final CodeforcesClient codeforcesClient;
    private final CodeChefClient codeChefClient;
    private final LeetCodeClient leetCodeClient;
    private final GFGClient gfgClient;
    private final HackerRankClient hackerRankClient;

    @Autowired
    public ContestServiceImpl(CodeforcesClient codeforcesClient, CodeChefClient codeChefClient, LeetCodeClient leetCodeClient, GFGClient gfgClient, HackerRankClient hackerRankClient) {
        this.codeforcesClient = codeforcesClient;
        this.codeChefClient = codeChefClient;
        this.leetCodeClient = leetCodeClient;
        this.gfgClient = gfgClient;
        this.hackerRankClient = hackerRankClient;
    }

    @Override
    public List<Contest> getUpcomingContests() {
        List<Contest> contests = new ArrayList<>();
        
        // Add Codeforces contests
        try {
            CodeforcesResponse cfResponse = codeforcesClient.getContests();
            if (cfResponse != null && cfResponse.getResult() != null) {
                contests.addAll(cfResponse.getResult().stream()
                    .filter(contest -> contest.getPhase().equals("BEFORE"))
                    .map(contest -> new Contest(
                        contest.getId(),
                        contest.getName(),
                        "Codeforces",
                        contest.getStartTimeSeconds() * 1000,
                        contest.getDurationSeconds() * 1000,
                        "https://codeforces.com/contests/" + contest.getId()
                    ))
                    .collect(Collectors.toList()));
            }
        } catch (Exception e) {
            // Handle error
        }

        // Add CodeChef contests
        try {
            CodeChefResponse ccResponse = codeChefClient.getContests();
            if (ccResponse != null && ccResponse.getFutureContests() != null) {
                contests.addAll(ccResponse.getFutureContests().stream()
                    .map(contest -> new Contest(
                        contest.getCode().hashCode(),
                        contest.getName(),
                        "CodeChef",
                        parseDate(contest.getStartDate()),
                        parseDate(contest.getEndDate()) - parseDate(contest.getStartDate()),
                        "https://www.codechef.com/" + contest.getCode()
                    ))
                    .collect(Collectors.toList()));
            }
        } catch (Exception e) {
            // Handle error
        }

        // Add LeetCode contests
        try {
            String query = "{\"query\": \"{ allContests { title startTime duration titleSlug } }\"}";
            LeetCodeResponse lcResponse = leetCodeClient.getContests(query, "Mozilla/5.0");
            if (lcResponse != null && lcResponse.getData() != null && lcResponse.getData().getAllContests() != null) {
                long currentTime = System.currentTimeMillis() / 1000;
                contests.addAll(lcResponse.getData().getAllContests().stream()
                    .filter(contest -> contest.getStartTime() > currentTime)
                    .map(contest -> new Contest(
                        contest.getTitleSlug().hashCode(),
                        contest.getTitle(),
                        "LeetCode",
                        contest.getStartTime() * 1000,
                        contest.getDuration() * 1000,
                        "https://leetcode.com/contest/" + contest.getTitleSlug()
                    ))
                    .collect(Collectors.toList()));
            }
        } catch (Exception e) {
            // Handle error
        }

        // Add GeeksForGeeks contests
        try {
            GFGResponse gfgResponse = gfgClient.getActiveContests();
            if (gfgResponse != null && gfgResponse.getActiveContests() != null) {
                contests.addAll(gfgResponse.getActiveContests().stream()
                    .map(contest -> new Contest(
                        contest.getName().hashCode(),
                        contest.getName(),
                        "GeeksForGeeks",
                        contest.getStartTime(),
                        contest.getDuration(),
                        contest.getUrl()
                    ))
                    .collect(Collectors.toList()));
            }
        } catch (Exception e) {
            // Handle error
        }

        // Add HackerRank contests
        try {
            HackerRankResponse hrResponse = hackerRankClient.getUpcomingContests();
            if (hrResponse != null && hrResponse.getModels() != null) {
                contests.addAll(hrResponse.getModels().stream()
                    .map(contest -> new Contest(
                        contest.getId(),
                        contest.getName(),
                        "HackerRank",
                        contest.getStartTime(),
                        contest.getDuration(),
                        contest.getUrl()
                    ))
                    .collect(Collectors.toList()));
            }
        } catch (Exception e) {
            // Handle error
        }

        return contests.stream()
            .sorted(Comparator.comparingLong(Contest::getStartTime))
            .collect(Collectors.toList());
    }

    @Override
    public List<Contest> getOngoingContests() {
        List<Contest> contests = new ArrayList<>();
        
        // Add Codeforces contests
        try {
            CodeforcesResponse cfResponse = codeforcesClient.getContests();
            if (cfResponse != null && cfResponse.getResult() != null) {
                contests.addAll(cfResponse.getResult().stream()
                    .filter(contest -> contest.getPhase().equals("CODING"))
                    .map(contest -> new Contest(
                        contest.getId(),
                        contest.getName(),
                        "Codeforces",
                        contest.getStartTimeSeconds() * 1000,
                        contest.getDurationSeconds() * 1000,
                        "https://codeforces.com/contests/" + contest.getId()
                    ))
                    .collect(Collectors.toList()));
            }
        } catch (Exception e) {
            // Handle error
        }

        // Add CodeChef contests
        try {
            CodeChefResponse ccResponse = codeChefClient.getContests();
            if (ccResponse != null && ccResponse.getPresentContests() != null) {
                contests.addAll(ccResponse.getPresentContests().stream()
                    .map(contest -> new Contest(
                        contest.getCode().hashCode(),
                        contest.getName(),
                        "CodeChef",
                        parseDate(contest.getStartDate()),
                        parseDate(contest.getEndDate()) - parseDate(contest.getStartDate()),
                        "https://www.codechef.com/" + contest.getCode()
                    ))
                    .collect(Collectors.toList()));
            }
        } catch (Exception e) {
            // Handle error
        }

        // Add GeeksForGeeks contests
        try {
            GFGResponse gfgResponse = gfgClient.getActiveContests();
            if (gfgResponse != null && gfgResponse.getActiveContests() != null) {
                contests.addAll(gfgResponse.getActiveContests().stream()
                    .map(contest -> new Contest(
                        contest.getName().hashCode(),
                        contest.getName(),
                        "GeeksForGeeks",
                        contest.getStartTime(),
                        contest.getDuration(),
                        contest.getUrl()
                    ))
                    .collect(Collectors.toList()));
            }
        } catch (Exception e) {
            // Handle error
        }

        // Add HackerRank contests
        try {
            HackerRankResponse hrResponse = hackerRankClient.getActiveContests();
            if (hrResponse != null && hrResponse.getModels() != null) {
                contests.addAll(hrResponse.getModels().stream()
                    .map(contest -> new Contest(
                        contest.getId(),
                        contest.getName(),
                        "HackerRank",
                        contest.getStartTime(),
                        contest.getDuration(),
                        contest.getUrl()
                    ))
                    .collect(Collectors.toList()));
            }
        } catch (Exception e) {
            // Handle error
        }

        return contests.stream()
            .sorted(Comparator.comparingLong(Contest::getStartTime))
            .collect(Collectors.toList());
    }

    private long parseDate(String dateStr) {
        try {
            return Instant.parse(dateStr).toEpochMilli();
        } catch (Exception e) {
            return System.currentTimeMillis();
        }
    }
} 