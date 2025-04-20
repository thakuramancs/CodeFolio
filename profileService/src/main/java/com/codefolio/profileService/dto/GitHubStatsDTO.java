package com.codefolio.profileService.dto;

import lombok.Data;
import java.util.Map;
import java.util.List;

@Data
public class GitHubStatsDTO {
    private int publicRepos;
    private int followers;
    private int following;
    private int totalStars;
    private int totalContributions;
    private int totalActiveDays;
    private int maxStreak;
    private int currentStreak;
    private Map<String, Double> languages; // Language name -> percentage
    private int prs;
    private int issues;
    private int commits;
    private List<ContributionDay> contributionCalendar;

    @Data
    public static class ContributionDay {
        private String date;
        private int count;
    }
} 