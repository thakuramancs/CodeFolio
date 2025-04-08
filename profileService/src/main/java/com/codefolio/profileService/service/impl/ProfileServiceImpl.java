package com.codefolio.profileService.service.impl;

import com.codefolio.profileService.model.Profile;
import com.codefolio.profileService.repository.ProfileRepository;
import com.codefolio.profileService.service.ProfileService;
import com.codefolio.profileService.dto.AggregateStats;
import com.codefolio.profileService.client.LeetCodeClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.json.JSONObject;
import org.json.JSONArray;
import java.time.LocalDateTime;
import java.util.Map;

@Service("profileService")
public class ProfileServiceImpl implements ProfileService {
    
    private static final Logger log = LoggerFactory.getLogger(ProfileServiceImpl.class);
    private final ProfileRepository profileRepository;
    private final LeetCodeClient leetCodeClient;

    public ProfileServiceImpl(ProfileRepository profileRepository, LeetCodeClient leetCodeClient) {
        this.profileRepository = profileRepository;
        this.leetCodeClient = leetCodeClient;
    }

    @Override
    public Profile createProfile(String userId, String email, String name) {
        log.info("Creating profile for user: {}", userId);
        if (profileRepository.findByUserId(userId).isPresent()) {
            log.error("Profile already exists for user: {}", userId);
            throw new IllegalStateException("Profile already exists");
        }
        Profile profile = new Profile(userId, email, name);
        return profileRepository.save(profile);
    }

    @Override
    public Profile getProfile(String userId) {
        log.info("Fetching profile for user: {}", userId);
        return profileRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.error("Profile not found for user: {}", userId);
                    return new IllegalStateException("Profile not found");
                });
    }

    @Override
    public Profile updateProfile(String userId, Profile profile) {
        log.info("Updating profile for user: {}", userId);
        Profile existingProfile = getProfile(userId);
        
        // Update basic info
        existingProfile.setName(profile.getName());
        existingProfile.setEmail(profile.getEmail());
        
        // Update platform usernames
        existingProfile.setLeetcodeUsername(profile.getLeetcodeUsername());
        existingProfile.setCodeforcesUsername(profile.getCodeforcesUsername());
        existingProfile.setCodechefUsername(profile.getCodechefUsername());
        existingProfile.setHackerrankUsername(profile.getHackerrankUsername());
        existingProfile.setGeeksforgeeksUsername(profile.getGeeksforgeeksUsername());
        
        // If LeetCode username has changed, update LeetCode stats
        if (hasLeetCodeUsernameChanged(existingProfile.getLeetcodeUsername(), profile.getLeetcodeUsername())) {
            try {
                updateLeetCodeStats(existingProfile);
            } catch (Exception e) {
                log.warn("Failed to update LeetCode stats: {}", e.getMessage());
                // Continue with profile update even if stats update fails
            }
        }
        
        existingProfile.setLastUpdated(LocalDateTime.now());
        return profileRepository.save(existingProfile);
    }

    private boolean hasLeetCodeUsernameChanged(String oldUsername, String newUsername) {
        if (oldUsername == null && newUsername == null) return false;
        if (oldUsername == null || newUsername == null) return true;
        return !oldUsername.equals(newUsername);
    }

    @Override
    public void deleteProfile(String userId) {
        log.info("Deleting profile for user: {}", userId);
        profileRepository.delete(getProfile(userId));
    }

    @Override
    public Map<String, Object> getLeetCodeProfile(String username) {
        log.info("Fetching LeetCode profile for username: {}", username);
        try {
            JSONObject response = leetCodeClient.getUserProfile(username);
            
            if (response.has("errors")) {
                log.error("LeetCode API returned errors: {}", response.getJSONArray("errors"));
                throw new RuntimeException("LeetCode API error: " + response.getJSONArray("errors").toString());
            }
            
            if (!response.has("data") || !response.getJSONObject("data").has("matchedUser")) {
                log.error("Invalid response format from LeetCode API: {}", response);
                throw new RuntimeException("Invalid response from LeetCode API");
            }
            
            return response.toMap();
        } catch (Exception e) {
            log.error("Error fetching LeetCode profile for username: {}", username, e);
            throw new RuntimeException("Failed to fetch LeetCode profile: " + e.getMessage(), e);
        }
    }

    @Override
    public AggregateStats getAggregateStats(String userId) {
        log.info("Fetching aggregate stats for user: {}", userId);
        Profile profile = getProfile(userId);
        AggregateStats stats = new AggregateStats();
        
        stats.setTotalProblemsSolved(profile.getTotalProblemsSolved());
        stats.setTotalContestsJoined(profile.getTotalContestsJoined());
        stats.setLeetcodeProblemsSolved(profile.getLeetcodeTotalSolved());
        return stats;
    }

    @Override
    public Profile updateLeetCodeProfile(String userId, String username) {
        log.info("Updating LeetCode profile for user: {}", userId);
        if (username == null || username.trim().isEmpty()) {
            log.error("Invalid LeetCode username provided");
            throw new IllegalArgumentException("LeetCode username cannot be empty");
        }

        Profile profile = getProfile(userId);
        profile.setLeetcodeUsername(username);
        
        try {
            updateLeetCodeStats(profile);
            return profileRepository.save(profile);
        } catch (Exception e) {
            log.error("Failed to update LeetCode profile for user: {}", userId, e);
            throw new RuntimeException("Failed to update LeetCode profile: " + e.getMessage(), e);
        }
    }

    private void updateLeetCodeStats(Profile profile) {
        if (profile.getLeetcodeUsername() == null) {
            log.warn("LeetCode username not set for user: {}", profile.getUserId());
            return;
        }

        try {
            JSONObject response = leetCodeClient.getUserProfile(profile.getLeetcodeUsername());
            
            if (response.has("errors")) {
                throw new RuntimeException("LeetCode API error: " + response.getJSONArray("errors").toString());
            }
            
            JSONObject matchedUser = response.getJSONObject("data").getJSONObject("matchedUser");
            JSONObject submitStats = matchedUser.getJSONObject("submitStats");
            JSONArray acSubmissionNum = submitStats.getJSONArray("acSubmissionNum");
            
            int totalSolved = 0;
            int easySolved = 0;
            int mediumSolved = 0;
            int hardSolved = 0;
            
            for (int i = 0; i < acSubmissionNum.length(); i++) {
                JSONObject submission = acSubmissionNum.getJSONObject(i);
                String difficulty = submission.getString("difficulty");
                int count = submission.getInt("count");
                
                switch (difficulty) {
                    case "All":
                        totalSolved = count;
                        break;
                    case "Easy":
                        easySolved = count;
                        break;
                    case "Medium":
                        mediumSolved = count;
                        break;
                    case "Hard":
                        hardSolved = count;
                        break;
                }
            }
            
            profile.setLeetcodeTotalSolved(totalSolved);
            profile.setLeetcodeEasySolved(easySolved);
            profile.setLeetcodeMediumSolved(mediumSolved);
            profile.setLeetcodeHardSolved(hardSolved);
            
            // Update profile ranking if available
            if (matchedUser.has("profile")) {
                JSONObject profileData = matchedUser.getJSONObject("profile");
                if (profileData.has("ranking")) {
                    profile.setLeetcodeRating(profileData.getInt("ranking"));
                }
            }
            
            profile.setTotalProblemsSolved(profile.getLeetcodeTotalSolved());
            
        } catch (Exception e) {
            log.error("Error updating LeetCode stats for user: {}", profile.getUserId(), e);
            throw new RuntimeException("Failed to update LeetCode stats: " + e.getMessage(), e);
        }
    }
} 