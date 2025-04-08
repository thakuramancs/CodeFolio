package com.codefolio.profileService.dto;

import java.util.Map;

public class AggregateStats {
    private int totalProblemsSolved;
    private int totalContestsJoined;
    private Map<String, Integer> topicWiseTotal;  // e.g., {"arrays": 50, "strings": 30}
    private Map<String, Integer> difficultyWiseTotal;  // e.g., {"easy": 100, "medium": 50, "hard": 20}
    
    // Platform-specific totals
    private int leetcodeProblemsSolved;
    private int codeforcesProblemsSolved;
    private int codechefProblemsSolved;
    private int hackerrankProblemsSolved;
    private int gfgProblemsSolved;

    // Getters and Setters
    public int getTotalProblemsSolved() {
        return totalProblemsSolved;
    }

    public void setTotalProblemsSolved(int totalProblemsSolved) {
        this.totalProblemsSolved = totalProblemsSolved;
    }

    public int getTotalContestsJoined() {
        return totalContestsJoined;
    }

    public void setTotalContestsJoined(int totalContestsJoined) {
        this.totalContestsJoined = totalContestsJoined;
    }

    public Map<String, Integer> getTopicWiseTotal() {
        return topicWiseTotal;
    }

    public void setTopicWiseTotal(Map<String, Integer> topicWiseTotal) {
        this.topicWiseTotal = topicWiseTotal;
    }

    public Map<String, Integer> getDifficultyWiseTotal() {
        return difficultyWiseTotal;
    }

    public void setDifficultyWiseTotal(Map<String, Integer> difficultyWiseTotal) {
        this.difficultyWiseTotal = difficultyWiseTotal;
    }

    public int getLeetcodeProblemsSolved() {
        return leetcodeProblemsSolved;
    }

    public void setLeetcodeProblemsSolved(int leetcodeProblemsSolved) {
        this.leetcodeProblemsSolved = leetcodeProblemsSolved;
    }

    public int getCodeforcesProblemsSolved() {
        return codeforcesProblemsSolved;
    }

    public void setCodeforcesProblemsSolved(int codeforcesProblemsSolved) {
        this.codeforcesProblemsSolved = codeforcesProblemsSolved;
    }

    public int getCodechefProblemsSolved() {
        return codechefProblemsSolved;
    }

    public void setCodechefProblemsSolved(int codechefProblemsSolved) {
        this.codechefProblemsSolved = codechefProblemsSolved;
    }

    public int getHackerrankProblemsSolved() {
        return hackerrankProblemsSolved;
    }

    public void setHackerrankProblemsSolved(int hackerrankProblemsSolved) {
        this.hackerrankProblemsSolved = hackerrankProblemsSolved;
    }

    public int getGfgProblemsSolved() {
        return gfgProblemsSolved;
    }

    public void setGfgProblemsSolved(int gfgProblemsSolved) {
        this.gfgProblemsSolved = gfgProblemsSolved;
    }
} 