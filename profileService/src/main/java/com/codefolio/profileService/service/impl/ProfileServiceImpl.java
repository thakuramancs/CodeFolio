package com.codefolio.profileService.service.impl;

import com.codefolio.profileService.model.Profile;
import com.codefolio.profileService.repository.ProfileRepository;
import com.codefolio.profileService.service.ProfileService;
import com.codefolio.profileService.dto.AggregateStats;
import com.codefolio.profileService.Clients.LeetCodeClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    
    private final ProfileRepository profileRepository;
    private final LeetCodeClient leetCodeClient;

    @Override
    public Profile createProfile(String userId, String email, String name) {
        if (profileRepository.existsByUserId(userId)) {
            throw new IllegalStateException("Profile already exists");
        }
        return profileRepository.save(new Profile(userId, email, name));
    }

    @Override
    public Profile getProfile(String userId) {
        return profileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException("Profile not found"));
    }

    @Override
    public Profile updateProfile(String userId, Profile profile) {
        Profile existingProfile = getProfile(userId);
        existingProfile.setName(profile.getName());
        existingProfile.setEmail(profile.getEmail());
        existingProfile.setLastUpdated(LocalDateTime.now());
        return profileRepository.save(existingProfile);
    }

    @Override
    public void deleteProfile(String userId) {
        profileRepository.delete(getProfile(userId));
    }

    @Override
    public Map<String, Object> getLeetCodeProfile(String username) {
        return leetCodeClient.getUserProfile(username);
    }

    @Override
    public Map<String, Object> getCodeforcesProfile(String username) {
        // Placeholder for Codeforces integration
        Map<String, Object> mockProfile = new HashMap<>();
        mockProfile.put("message", "Codeforces integration will be implemented later");
        mockProfile.put("username", username);
        return mockProfile;
    }

    @Override
    public Map<String, Object> getCodeChefProfile(String username) {
        // Placeholder for CodeChef integration
        Map<String, Object> mockProfile = new HashMap<>();
        mockProfile.put("message", "CodeChef integration will be implemented later");
        mockProfile.put("username", username);
        return mockProfile;
    }

    @Override
    public Map<String, Object> getHackerRankProfile(String username) {
        // Placeholder for HackerRank integration
        Map<String, Object> mockProfile = new HashMap<>();
        mockProfile.put("message", "HackerRank integration will be implemented later");
        mockProfile.put("username", username);
        return mockProfile;
    }

    @Override
    public Map<String, Object> getGeeksForGeeksProfile(String username) {
        // Placeholder for GeeksForGeeks integration
        Map<String, Object> mockProfile = new HashMap<>();
        mockProfile.put("message", "GeeksForGeeks integration will be implemented later");
        mockProfile.put("username", username);
        return mockProfile;
    }

    @Override
    public Profile updatePlatformUsername(String userId, String platform, String username) {
        Profile profile = getProfile(userId);
        
        switch (platform.toLowerCase()) {
            case "leetcode" -> {
                profile.setLeetcodeUsername(username);
                updateLeetCodeStats(profile);
            }
            case "codeforces" -> profile.setCodeforcesUsername(username);
            case "codechef" -> profile.setCodechefUsername(username);
            case "hackerrank" -> profile.setHackerrankUsername(username);
            case "gfg" -> profile.setGfgUsername(username);
            default -> throw new IllegalArgumentException("Unsupported platform");
        }
        
        profile.setLastUpdated(LocalDateTime.now());
        return profileRepository.save(profile);
    }

    @Override
    public AggregateStats getAggregateStats(String userId) {
        Profile profile = getProfile(userId);
        AggregateStats stats = new AggregateStats();
        
        stats.setTotalProblemsSolved(profile.getTotalProblemsSolved());
        stats.setTotalContestsJoined(profile.getTotalContestsJoined());
        stats.setLeetcodeProblemsSolved(profile.getLeetcodeTotalSolved());
        return stats;
    }

    private void updateLeetCodeStats(Profile profile) {
        if (profile.getLeetcodeUsername() == null) return;

        Map<String, Object> stats = leetCodeClient.getUserProfile(profile.getLeetcodeUsername());
        
        profile.setLeetcodeTotalSolved(
            ((Integer) stats.get("easySolved")) +
            ((Integer) stats.get("mediumSolved")) +
            ((Integer) stats.get("hardSolved"))
        );
        profile.setLeetcodeEasySolved((Integer) stats.get("easySolved"));
        profile.setLeetcodeMediumSolved((Integer) stats.get("mediumSolved"));
        profile.setLeetcodeHardSolved((Integer) stats.get("hardSolved"));
        profile.setLeetcodeRating((Integer) stats.get("rating"));
        profile.setLeetcodeContestsJoined((Integer) stats.get("contestsJoined"));
        
        profile.setTotalProblemsSolved(profile.getLeetcodeTotalSolved());
        profile.setTotalContestsJoined(profile.getLeetcodeContestsJoined());
    }
} 