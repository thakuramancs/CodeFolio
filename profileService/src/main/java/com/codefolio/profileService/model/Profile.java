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

    // LeetCode stats
    private Integer leetcodeTotalSolved = 0;
    private Integer leetcodeEasySolved = 0;
    private Integer leetcodeMediumSolved = 0;
    private Integer leetcodeHardSolved = 0;
    private Integer leetcodeRating = 0;

    // CodeForces stats
    private Integer codeforcesRating = 0;
    private String codeforcesRank;
    private Integer codeforcesSolvedCount = 0;
    private Integer codeforcesContestCount = 0;

    private LocalDateTime lastUpdated;

    @PrePersist
    protected void onCreate() {
        lastUpdated = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdated = LocalDateTime.now();
    }

    public Profile() {}

    public Profile(String userId, String email, String name) {
        this.userId = userId;
        this.email = email;
        this.name = name;
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

    public Integer getLeetcodeRating() { return leetcodeRating; }
    public void setLeetcodeRating(Integer leetcodeRating) { this.leetcodeRating = leetcodeRating; }

    public Integer getCodeforcesRating() { return codeforcesRating; }
    public void setCodeforcesRating(Integer codeforcesRating) { this.codeforcesRating = codeforcesRating; }

    public String getCodeforcesRank() { return codeforcesRank; }
    public void setCodeforcesRank(String codeforcesRank) { this.codeforcesRank = codeforcesRank; }

    public Integer getCodeforcesSolvedCount() { return codeforcesSolvedCount; }
    public void setCodeforcesSolvedCount(Integer codeforcesSolvedCount) { this.codeforcesSolvedCount = codeforcesSolvedCount; }

    public Integer getCodeforcesContestCount() { return codeforcesContestCount; }
    public void setCodeforcesContestCount(Integer codeforcesContestCount) { this.codeforcesContestCount = codeforcesContestCount; }

    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
} 