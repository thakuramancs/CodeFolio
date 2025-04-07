package com.codefolio.profileService.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "profiles")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String userId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    // LeetCode Stats
    private String leetcodeUsername;
    private Integer leetcodeTotalSolved;
    private Integer leetcodeEasySolved;
    private Integer leetcodeMediumSolved;
    private Integer leetcodeHardSolved;
    private Integer leetcodeContestsJoined;
    private Integer leetcodeRating;
    private Integer leetcodeGlobalRank;
    @Column(columnDefinition = "TEXT")
    private String leetcodeTopicStats;  // JSON string: {"arrays": 10, "strings": 15, ...}

    // CodeForces Stats
    private String codeforcesUsername;
    private Integer codeforcesProblemsSolved;
    private Integer codeforcesContestsJoined;
    private Integer codeforcesRating;
    private Integer codeforcesMaxRating;
    private String codeforcesRank;
    @Column(columnDefinition = "TEXT")
    private String codeforcesTopicStats;

    // CodeChef Stats
    private String codechefUsername;
    private Integer codechefProblemsSolved;
    private Integer codechefContestsJoined;
    private Integer codechefRating;
    private Integer codechefMaxRating;
    private String codechefStars;
    @Column(columnDefinition = "TEXT")
    private String codechefTopicStats;

    // HackerRank Stats
    private String hackerrankUsername;
    private Integer hackerrankProblemsSolved;
    private Integer hackerrankContestsJoined;
    private Integer hackerrankPoints;
    private String hackerrankLevel;
    @Column(columnDefinition = "TEXT")
    private String hackerrankTopicStats;

    // GeeksForGeeks Stats
    private String gfgUsername;
    private Integer gfgProblemsSolved;
    private Integer gfgEasySolved;
    private Integer gfgMediumSolved;
    private Integer gfgHardSolved;
    private Integer gfgContestsJoined;
    private Integer gfgScore;
    private String gfgRank;
    @Column(columnDefinition = "TEXT")
    private String gfgTopicStats;

    private LocalDateTime lastUpdated;

    // Aggregate stats across all platforms
    private Integer totalProblemsSolved;
    private Integer totalContestsJoined;
    @Column(columnDefinition = "TEXT")
    private String aggregateTopicStats;  // Combined stats across platforms
    @Column(columnDefinition = "TEXT")
    private String aggregateDifficultyStats;  // Combined difficulty stats

    public Profile(String userId, String email, String name) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.lastUpdated = LocalDateTime.now();
        initializeStats();
    }

    private void initializeStats() {
        // Initialize all numeric fields to 0
        this.leetcodeTotalSolved = 0;
        this.leetcodeEasySolved = 0;
        this.leetcodeMediumSolved = 0;
        this.leetcodeHardSolved = 0;
        this.leetcodeContestsJoined = 0;
        this.leetcodeRating = 0;
        this.leetcodeGlobalRank = 0;

        this.codeforcesProblemsSolved = 0;
        this.codeforcesContestsJoined = 0;
        this.codeforcesRating = 0;
        this.codeforcesMaxRating = 0;

        this.codechefProblemsSolved = 0;
        this.codechefContestsJoined = 0;
        this.codechefRating = 0;
        this.codechefMaxRating = 0;

        this.hackerrankProblemsSolved = 0;
        this.hackerrankContestsJoined = 0;
        this.hackerrankPoints = 0;

        this.gfgProblemsSolved = 0;
        this.gfgEasySolved = 0;
        this.gfgMediumSolved = 0;
        this.gfgHardSolved = 0;
        this.gfgContestsJoined = 0;
        this.gfgScore = 0;

        this.totalProblemsSolved = 0;
        this.totalContestsJoined = 0;

        // Initialize JSON strings as empty objects
        String emptyStats = "{}";
        this.leetcodeTopicStats = emptyStats;
        this.codeforcesTopicStats = emptyStats;
        this.codechefTopicStats = emptyStats;
        this.hackerrankTopicStats = emptyStats;
        this.gfgTopicStats = emptyStats;
        this.aggregateTopicStats = emptyStats;
        this.aggregateDifficultyStats = emptyStats;
    }
} 