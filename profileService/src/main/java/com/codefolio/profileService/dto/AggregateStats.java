package com.codefolio.profileService.dto;

import lombok.Data;
import java.util.Map;

@Data
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
} 