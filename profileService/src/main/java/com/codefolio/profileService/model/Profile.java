package com.codefolio.profileService.model;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    // Platform usernames
    private String leetcodeUsername;
    private String codeforcesUsername;
    private String codechefUsername;
    private String hackerrankUsername;
    private String geeksforgeeksUsername;

    // LeetCode specific fields
    private Integer leetcodeTotalSolved;
    private Integer leetcodeEasySolved;
    private Integer leetcodeMediumSolved;
    private Integer leetcodeHardSolved;
    private Integer leetcodeContestsJoined;
    private Integer leetcodeRating;

    // Aggregate stats
    private Integer totalProblemsSolved;
    private Integer totalContestsJoined;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastUpdated;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        initializeStats();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        lastUpdated = LocalDateTime.now();
    }

    public Profile() {
        initializeStats();
    }

    public Profile(String userId, String email, String name) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        initializeStats();
    }

    private void initializeStats() {
        this.leetcodeTotalSolved = 0;
        this.leetcodeEasySolved = 0;
        this.leetcodeMediumSolved = 0;
        this.leetcodeHardSolved = 0;
        this.leetcodeContestsJoined = 0;
        this.leetcodeRating = 0;
        this.totalProblemsSolved = 0;
        this.totalContestsJoined = 0;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLeetcodeUsername() { return leetcodeUsername; }
    public void setLeetcodeUsername(String leetcodeUsername) { this.leetcodeUsername = leetcodeUsername; }

    public String getCodeforcesUsername() { return codeforcesUsername; }
    public void setCodeforcesUsername(String codeforcesUsername) { this.codeforcesUsername = codeforcesUsername; }

    public String getCodechefUsername() { return codechefUsername; }
    public void setCodechefUsername(String codechefUsername) { this.codechefUsername = codechefUsername; }

    public String getHackerrankUsername() { return hackerrankUsername; }
    public void setHackerrankUsername(String hackerrankUsername) { this.hackerrankUsername = hackerrankUsername; }

    public String getGeeksforgeeksUsername() { return geeksforgeeksUsername; }
    public void setGeeksforgeeksUsername(String geeksforgeeksUsername) { this.geeksforgeeksUsername = geeksforgeeksUsername; }

    public Integer getLeetcodeTotalSolved() { return leetcodeTotalSolved; }
    public void setLeetcodeTotalSolved(Integer leetcodeTotalSolved) { this.leetcodeTotalSolved = leetcodeTotalSolved; }

    public Integer getLeetcodeEasySolved() { return leetcodeEasySolved; }
    public void setLeetcodeEasySolved(Integer leetcodeEasySolved) { this.leetcodeEasySolved = leetcodeEasySolved; }

    public Integer getLeetcodeMediumSolved() { return leetcodeMediumSolved; }
    public void setLeetcodeMediumSolved(Integer leetcodeMediumSolved) { this.leetcodeMediumSolved = leetcodeMediumSolved; }

    public Integer getLeetcodeHardSolved() { return leetcodeHardSolved; }
    public void setLeetcodeHardSolved(Integer leetcodeHardSolved) { this.leetcodeHardSolved = leetcodeHardSolved; }

    public Integer getLeetcodeContestsJoined() { return leetcodeContestsJoined; }
    public void setLeetcodeContestsJoined(Integer leetcodeContestsJoined) { this.leetcodeContestsJoined = leetcodeContestsJoined; }

    public Integer getLeetcodeRating() { return leetcodeRating; }
    public void setLeetcodeRating(Integer leetcodeRating) { this.leetcodeRating = leetcodeRating; }

    public Integer getTotalProblemsSolved() { return totalProblemsSolved; }
    public void setTotalProblemsSolved(Integer totalProblemsSolved) { this.totalProblemsSolved = totalProblemsSolved; }

    public Integer getTotalContestsJoined() { return totalContestsJoined; }
    public void setTotalContestsJoined(Integer totalContestsJoined) { this.totalContestsJoined = totalContestsJoined; }

    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
} 